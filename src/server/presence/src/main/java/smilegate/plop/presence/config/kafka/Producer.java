package smilegate.plop.presence.config.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Service
@RequiredArgsConstructor
public class Producer {

    @Value("${kafka.topic.name}")
    private String preTopicName;

    private final KafkaTemplate<String, PresenceMessage> kafkaTemplate;

    public void sendMessage(PresenceMessage presenceMessage){
        Message<PresenceMessage> message = MessageBuilder
                .withPayload(presenceMessage)
                .setHeader(KafkaHeaders.TOPIC, preTopicName)
                .build();
        ListenableFuture<SendResult<String, PresenceMessage>> send = kafkaTemplate.send(message);
        send.addCallback(new ListenableFutureCallback<SendResult<String, PresenceMessage>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("{}에게 전송 실패",presenceMessage.getUser_id());
            }

            @Override
            public void onSuccess(SendResult<String, PresenceMessage> result) {}
        });
    }
}
