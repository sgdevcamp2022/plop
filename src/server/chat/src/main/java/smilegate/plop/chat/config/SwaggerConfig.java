package smilegate.plop.chat.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group("v1-plop")
                .pathsToMatch("/chatting/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI(){
        String title = "Plop Chat-service API Docs";
        String description = "Plop Messenger App REST API Document - Chat";
        return new OpenAPI()
                .info(new Info().title(title)
                        .description(description)
                        .version("v0.0.1"));
    }

}
