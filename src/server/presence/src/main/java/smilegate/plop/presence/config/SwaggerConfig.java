package smilegate.plop.presence.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group("v1-plop")
                .pathsToMatch("/presence/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI(){
        String title = "Plop Presence-service API Docs";
        String description = "Plop Messenger App REST API Document - Presence";
        return new OpenAPI()
                .info(new Info().title(title)
                        .description(description)
                        .version("v0.0.1"));
    }

}
