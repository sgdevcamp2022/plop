package com.plop.plopmessenger.data.remote.stomp

import android.util.Log
import com.plop.plopmessenger.data.remote.stomp.constants.Commands
import com.plop.plopmessenger.data.remote.stomp.constants.Headers
import okhttp3.*
import okhttp3.WebSocketListener
import okio.ByteString
import java.io.StringReader
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

const val TERMINATE_MESSAGE_SYMBOL = "\u0000"
const val NORMAL_CLOSURE_STATUS = 1000
val PATTERN_HEADER = Pattern.compile("([^:\\s]+)\\s*:\\s*([^:\\s]+)")

class WebSocketListener @Inject constructor(
    private val okHttpClient: OkHttpClient
) : WebSocketListener() {

    private val DEFAULT_ACK = "auto"
    private val SUPPORTED_VERSIONS = "1.1,1.2"

    private var shouldBeConnected: Boolean = false
    private var connected = false
    private lateinit var webSocket: WebSocket

//    init {
//        join("chatroomId1")
//    }

    fun connect() {
        if (!connected) {
            Log.d("STOMP", "connect 함수")
            val request = Request.Builder()
                .url("wss://pubwss.bithumb.com/pub/ws")
                .build()
            webSocket = okHttpClient.newWebSocket(request, this)
            connected = true
        } else {
        }
    }


//    private fun handleMessage(message: SocketMessage) {
//        when (message.command) {
//            Commands.CONNECTED -> {
//                Log.d("STOMP 로그", "연결되었다는 메세지")
//            }
//            Commands.MESSAGE -> {
//                Log.d("STOMP 로그", "메세지 받음 : onMessage payload: ${message.payload}, heaaders:${message.headers}, command: ${message.command}")
//            }
//        }
//    }

    fun join(topic: String) {
        val topicId = UUID.randomUUID().toString()

//        val headers = HashMap<String, String>()
//        headers[Headers.ID] = topicId
//        headers[Headers.DESTINATION] = topic
//        headers[Headers.ACK] = DEFAULT_ACK
//        webSocket.send(compileMessage(SocketMessage(Commands.SUBSCRIBE, headers = headers)))
        webSocket.send("{\"type\":\"ticker\", \"symbols\": [\"BTC_KRW\"], \"tickTypes\": [\"30M\"]}")

        Log.d("STOMP 로그", "Subscribed to: $topic id: $topicId")
    }

    private fun reconnect() {
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

    fun send(topic: String, msg: String) {
        val headers = HashMap<String, String>()
        headers[Headers.DESTINATION] = topic
        val result = webSocket.send(compileMessage(SocketMessage(Commands.SEND, headers = headers, payload = msg)))
        Log.d("STOMP 로그", "Send $result")
    }

    override fun onOpen(socket: WebSocket, response: Response) {
        //socket.close(NORMAL_CLOSURE_STATUS, null) //없을 경우 끊임없이 서버와 통신함
        webSocket = socket
//        val headers = HashMap<String, String>()
//        headers[Headers.VERSION] = SUPPORTED_VERSIONS
//        socket.send(compileMessage(SocketMessage(Commands.CONNECT, headers = headers)))
        Log.d("STOMP 로그", "onOpen 실행")
    }


    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("STOMP 로그", "onMessage with byte 실행")
        //handleMessage(parseMessage(bytes.toString()))
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("STOMP 로그", "onMessage with text $text")
        //handleMessage(parseMessage(text))
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

    //Message to Text for send websocket
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
}