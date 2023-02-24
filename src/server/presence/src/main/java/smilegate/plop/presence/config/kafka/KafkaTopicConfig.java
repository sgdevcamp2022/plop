package smilegate.plop.presence.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig  {
    @Value("${kafka.topic.name}")
    private String preTopicName;

    @Bean
    public NewTopic getTopic(){
        return TopicBuilder.name(preTopicName).build();
    }
}
