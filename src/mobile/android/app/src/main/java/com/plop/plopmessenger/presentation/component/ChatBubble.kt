package com.plop.plopmessenger.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.plop.plopmessenger.domain.model.Message
import com.plop.plopmessenger.domain.model.MessageType
import com.plop.plopmessenger.util.SymbolAnnotationType
import com.plop.plopmessenger.util.messageFormatter
import com.plop.plopmessenger.R
import com.plop.plopmessenger.domain.model.ChatRoom
import com.plop.plopmessenger.domain.model.Member
import com.plop.plopmessenger.presentation.viewmodel.ChatState


object ChatItemBubbleValue {
    val firstBubbleShape = RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp)
    val firstMyBubbleShape = RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp)
    val lastBubbleShape = RoundedCornerShape(4.dp, 18.dp, 18.dp, 18.dp)
    val lastMyBubbleShape = RoundedCornerShape(18.dp, 4.dp, 18.dp, 18.dp)
    val defaultBubbleShape = RoundedCornerShape(4.dp, 18.dp, 18.dp, 4.dp)
    val defaultMyBubbleShape = RoundedCornerShape(18.dp, 4.dp, 4.dp, 18.dp)
    val betweenBubbleShape = RoundedCornerShape(4.dp, 18.dp, 18.dp, 4.dp)
    val betweenMyBubbleShape = RoundedCornerShape(18.dp, 4.dp, 4.dp, 18.dp)
    val chatBubblePadding = 10.dp
    val betweenContentAndChat = 44.dp
    val betweenImageAndChat = 12.dp
}

/**
 * author me 변경해야함!!
 * user id를 저장해서 비교하기
 */
@Composable
fun Messages(
    messages: List<Message>,
    onAuthorClick: (String) -> Unit,
    onImageClick: (String) -> Unit,
    onVideoClick: (String) -> Unit,
    isGroupChat: Boolean,
    member: Map<String, Member>,
    modifier: Modifier = Modifier,
    userId : String,
    state: ChatState,
    getNextMessage: () -> Unit,
    content: @Composable () -> Unit
) {

    LazyColumn(
        reverseLayout = true,
        contentPadding =
        WindowInsets.statusBars.add(WindowInsets(top = 90.dp)).asPaddingValues(),
        modifier = modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(messages.size) { index ->
            val prevAuthor = messages.getOrNull(index - 1)?.messageFromId
            val nextAuthor = messages.getOrNull(index + 1)?.messageFromId
            val content = messages[index]
            val prevTimeDiffIsOverMin = if(messages.getOrNull(index - 1) == null) false
            else (content.createdAt.minute - messages.getOrNull(index - 1)?.createdAt!!.minute) >= 1
            val nextTimeDiffIsOverMin = if(messages.getOrNull(index + 1) == null) false
            else (messages.getOrNull(index + 1)?.createdAt!!.minute - content.createdAt.minute) >= 1
            val isFirstMessageByAuthor = (nextAuthor != content.messageFromId || nextTimeDiffIsOverMin)
            val isLastMessageByAuthor = (prevAuthor != content.messageFromId || prevTimeDiffIsOverMin)

            if(index >= state.messages.size - 1 && !state.endReached && !state.isLoading) {
                getNextMessage()
            }

            Message(
                onAuthorClick = onAuthorClick,
                message = content,
                isUserMe = content.messageFromId == userId,
                isFirstMessageByAuthor = isFirstMessageByAuthor,
                isLastMessageByAuthor = isLastMessageByAuthor,
                onImageClick = onImageClick,
                onVideoClick = onVideoClick,
                isGroupChat = isGroupChat,
                member = member,
                modifier = Modifier
            )
        }
        item {
            Spacer(modifier = Modifier.size(ChatItemBubbleValue.betweenContentAndChat))
            content()
        }
    }
}

/**
 * author image 수정해야함!!
 */
@Composable
fun Message(
    onAuthorClick: (String) -> Unit,
    message: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    onImageClick: (String) -> Unit,
    onVideoClick: (String) -> Unit,
    isGroupChat: Boolean,
    member: Map<String, Member>,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        if (isLastMessageByAuthor && !isUserMe) {
            ProfileImage(
                imageURL = member[message.messageFromId]?.profileImg?: "",
                profileSize = ProfileImageValue.MessageAuthorImageSize,
                modifier = Modifier.clickable { onAuthorClick(message.messageFromId) }
            )
        } else {
            Spacer(modifier = Modifier
                .size(ProfileImageValue.MessageAuthorImageSize.dp))
        }
        Spacer(modifier = Modifier.size(ChatItemBubbleValue.betweenImageAndChat))

        AuthorAndTextMessage(
            message = message,
            isUserMe = isUserMe,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isLastMessageByAuthor,
            isGroupChat = isGroupChat,
            onImageClick = onImageClick,
            onVideoClick = onVideoClick,
            members = member,
            modifier = modifier
        )
    }
}

@Composable
fun AuthorAndTextMessage(
    message: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    isGroupChat: Boolean,
    onImageClick: (String) -> Unit,
    onVideoClick: (String) -> Unit,
    members: Map<String, Member>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if(isUserMe) Alignment.End else Alignment.Start
    ) {
        if(isFirstMessageByAuthor && isGroupChat) {
            Text(
                text = members[message.messageFromId]?.nickname?: ""
            )
        }
        ChatItemBubble(
            message = message,
            isUserMe = isUserMe,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isLastMessageByAuthor,
            onImageClick = onImageClick,
            onVideoClick = onVideoClick,
            members = members
        )
    }


}

@Composable
fun ChatItemBubble(
    message: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    members: Map<String, Member>,
    onImageClick: (String) -> Unit,
    onVideoClick: (String) -> Unit
) {
    val backgroundColor = if (isUserMe) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.secondary
    }

    val chatBubbleShape = if(isFirstMessageByAuthor && isLastMessageByAuthor) {
        if(isUserMe) ChatItemBubbleValue.defaultMyBubbleShape
        else ChatItemBubbleValue.defaultBubbleShape
    } else if (isFirstMessageByAuthor) {
        if(isUserMe) ChatItemBubbleValue.firstMyBubbleShape
        else ChatItemBubbleValue.firstBubbleShape
    } else if (isLastMessageByAuthor) {
        if(isUserMe) ChatItemBubbleValue.lastMyBubbleShape
        else ChatItemBubbleValue.lastBubbleShape
    } else {
        if(isUserMe) ChatItemBubbleValue.betweenMyBubbleShape
        else ChatItemBubbleValue.betweenBubbleShape
    }

    Surface(
        color = backgroundColor,
        shape = chatBubbleShape,
    ) {
        when(message.type) {
            MessageType.TEXT -> {
                ClickableMessage(
                    message = message,
                    isUserMe = isUserMe
                )
            }
            MessageType.IMAGE -> {
                AsyncImage(
                    modifier = Modifier.clickable { onImageClick(message.content) },
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(message.content)
                        .crossfade(true)
                        .build(),
                    contentDescription = "profile Image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.blank_profile)
                )
            }
            MessageType.VIDEO -> {
                /**
                 * Todo
                 */
            }
            MessageType.ENTER -> {

            }
        }
    }

}


@Composable
fun ClickableMessage(
    message: Message,
    isUserMe: Boolean
) {
    val uriHandler = LocalUriHandler.current

    val styleMessage = messageFormatter(
        text = message.content,
        isUserMe = isUserMe
    )

    val fontColor = if(isUserMe) MaterialTheme.colors.onPrimary
    else MaterialTheme.colors.onSecondary

    ClickableText(
        text = styleMessage,
        onClick = {
            styleMessage
                .getStringAnnotations(start = it, end = it)
                .firstOrNull()
                ?.let {
                    if(it.tag == SymbolAnnotationType.LINK.name) {
                        uriHandler.openUri(it.item)
                    } else Unit
                }
        },
        modifier = Modifier.padding(ChatItemBubbleValue.chatBubblePadding),
        style = TextStyle(fontSize = 17.sp, color = fontColor),

    )
}