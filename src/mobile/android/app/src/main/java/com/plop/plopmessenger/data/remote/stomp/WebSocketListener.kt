package com.plop.plopmessenger.data.remote.stomp

import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plop.plopmessenger.data.local.entity.ChatRoom
import com.plop.plopmessenger.data.local.entity.Member
import com.plop.plopmessenger.data.local.entity.Message
import com.plop.plopmessenger.data.remote.stomp.constants.Commands
import com.plop.plopmessenger.data.remote.stomp.constants.Headers
import com.plop.plopmessenger.domain.model.MessageType
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.MemberRepository
import com.plop.plopmessenger.domain.repository.MessageRepository
import com.plop.plopmessenger.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.WebSocketListener
import okio.ByteString
import java.io.StringReader
import java.time.LocalDateTime
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

const val TERMINATE_MESSAGE_SYMBOL = "\u0000"
const val NORMAL_CLOSURE_STATUS = 1000
val PATTERN_HEADER = Pattern.compile("([^:\\s]+)\\s*:\\s*([^:\\s]+)")

class WebSocketListener @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val chatRoomRepository: ChatRoomRepository,
    private val messageRepository: MessageRepository,
    private val memberRepository: MemberRepository,
    private val userRepository: UserRepository
) : WebSocketListener() {

    private val DEFAULT_ACK = "auto"
    private val SUPPORTED_VERSIONS = "1.1,1.2"

    private var shouldBeConnected: Boolean = false
    private var connected = false
    private lateinit var webSocket: WebSocket
    private var userId: String? = null

    fun connect() {
        if (!connected) {
            Log.d("STOMP", "connect 함수")
            val request = Request.Builder()
                .url("wss://pubwss.bithumb.com/pub/ws")
                .build()
            webSocket = okHttpClient.newWebSocket(request, this)
            connected = true
        }
    }

    fun reconnect() {
        if (shouldBeConnected) {
            close()
            Thread.sleep(300L)
            connect()
        }
    }
    fun close() {
        if (connected) {
            webSocket.close(1000, "")
            connected = false
        }
    }

    private fun handleMessage(message: SocketMessage) {
        when (message.command) {
            Commands.CONNECTED -> {
                Log.d("STOMP 로그", "연결되었다는 메세지")
            }
            Commands.MESSAGE -> {
                Log.d("STOMP 로그", "메세지 받음 : onMessage payload: ${message.payload}, heaaders:${message.headers}, command: ${message.command}")
            }
        }
    }

    fun join(topic: String) {
        val topicId = UUID.randomUUID().toString()

        val headers = HashMap<String, String>()
        headers[Headers.ID] = topicId
        headers[Headers.DESTINATION] = topic
        headers[Headers.ACK] = DEFAULT_ACK
        webSocket.send(compileMessage(SocketMessage(Commands.SUBSCRIBE, headers = headers)))

        Log.d("STOMP 로그", "Subscribed to: $topic id: $topicId")
    }

    fun joinAll() {
        CoroutineScope(Dispatchers.IO).launch {
            chatRoomRepository.loadChatRoomIdList().collect() { result ->
                result.forEach {
                    join(it)
                }
            }
            userRepository.getUserId().collect() {
                join(it)
                userId = it
            }
        }
    }


    fun saveMessage(message: Message) {
        CoroutineScope(Dispatchers.IO).launch {
            messageRepository.insertMessage(message)
        }
    }

    fun saveNewChatRoom(chatRoom: ChatRoom) {
        CoroutineScope(Dispatchers.IO).launch {
            chatRoomRepository.insertChatRoom(chatRoom)
        }
    }

    fun saveMembers(member: Member) {
        CoroutineScope(Dispatchers.IO).launch {
            memberRepository.insertMember(member)
        }
    }

    fun updateChatRoom(roomId: String, content: String) {
        CoroutineScope(Dispatchers.IO).launch {
            chatRoomRepository.updateChatRoomContentById(roomId, content, LocalDateTime.now())
        }
    }


    fun send(topic: String, msg: SocketMessage) {
        val result = webSocket.send(compileMessage(msg))
    }

    fun sendMessage(roomId: String, messageType: MessageType, content: String, payload: String?) {
        /** TODO 메세지 보내는 함수 토픽 쪽 다시보기! **/
        val headers = HashMap<String, String>()
        //headers[Headers.DESTINATION] = topic
        headers[Headers.ROOM_ID] = roomId
        headers[Headers.SENDER_ID] = userId?: ""
        headers[Headers.MESSAGE_TYPE] = messageType.toString()
        headers[Headers.CONTENT] = content

        if(userId != null) send(roomId, SocketMessage(Commands.SEND, headers = headers, payload = payload))
    }

    private fun compileMessage(message: SocketMessage): String {
        val builder = StringBuilder()

        if (message.command != null)
            builder.append(message.command).append('\n')

        for ((key, value) in message.headers)
            builder.append(key).append(':').append(value).append('\n')
        builder.append('\n')

        if (message.payload != null)
            builder.append(message.payload).append("\n\n")

        builder.append(TERMINATE_MESSAGE_SYMBOL)

        return builder.toString()
    }

    private fun parseMessage(data: String?): SocketMessage {

        if (data.isNullOrBlank())
            return SocketMessage(Commands.UNKNOWN)

        val reader = Scanner(StringReader(data))
        reader.useDelimiter("\\n")
        val command = reader.next()
        val headers = HashMap<String, String>()

        while (reader.hasNext(PATTERN_HEADER)) {
            val matcher = PATTERN_HEADER.matcher(reader.next())
            matcher.find()
            headers.put(matcher.group(1), matcher.group(2))
        }

        reader.skip("\\s")

        reader.useDelimiter(TERMINATE_MESSAGE_SYMBOL)
        val payload = if (reader.hasNext()) reader.next() else null

        return SocketMessage(command, payload!!, headers)
    }

    override fun onOpen(socket: WebSocket, response: Response) {
        webSocket = socket
        Log.d("STOMP 로그", "onOpen 실행")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("STOMP 로그", "onMessage with byte 실행")
        handleMessage(parseMessage(bytes.toString()))
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("STOMP 로그", "onMessage with text $text")
        handleMessage(parseMessage(text))
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("STOMP 로그","Closing : $code / $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        webSocket.cancel()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("STOMP 로그", "onFailure 실행 ${response}")
        connect()
    }
}