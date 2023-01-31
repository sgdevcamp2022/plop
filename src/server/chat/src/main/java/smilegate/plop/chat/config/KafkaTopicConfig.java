package smilegate.plop.chat.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${kafka.topic.dm.name}")
    private String topicName;

    @Value("${kafka.topic.group.name}")
    private String groupTopicName;

    @Bean
    public KafkaAdmin kafkaAdmin(){
        Map<String,Object> configurations = new HashMap<>();
        configurations.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapAddress);
        return new KafkaAdmin(configurations);
    }

    @Bean
    public NewTopic topic(){
        return new NewTopic(topicName,1,(short)1);
    }

    @Bean
    public NewTopic groupTopic(){
        return TopicBuilder.name(groupTopicName)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
