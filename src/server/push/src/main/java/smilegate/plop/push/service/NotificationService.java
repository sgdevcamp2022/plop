package smilegate.plop.push.service;

import com.google.api.services.storage.Storage;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import smilegate.plop.push.dto.request.RequestMessage;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationService {
    @Value("${fcm.key.path}")
    private String FCM_PRIVATE_KEY_PATH;

    @Value("${fcm.key.scope")
    private String firebaseScope;

    @PostConstruct
    public void init() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(
                            GoogleCredentials.fromStream(new ClassPathResource(FCM_PRIVATE_KEY_PATH).getInputStream())
                                    .createScoped(List.of(firebaseScope)))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialiezed");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }
//비동기 처리 ?
    public void sendByTokenList(List<String> tokenList, RequestMessage message) {
        List<Message> messages = tokenList.stream().map(token-> Message.builder()
                .putData("time", LocalDateTime.now().toString())
                .setNotification(new Notification(message.getTitle(),message.getBody()))
                .setToken(token)
                .build()).collect(Collectors.toList());

        BatchResponse response;
        try {
            //알림 발송
            response = FirebaseMessaging.getInstance().sendAll(messages);

            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<String> failedTokens = new ArrayList<>();

                for (int i = 0; i< responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        failedTokens.add(tokenList.get(i));
                    }
                }
            }

        } catch (FirebaseMessagingException e ) {
            log.error("can not send to memberList push message. error info : {}", e.getMessage());
        }
    }
}

