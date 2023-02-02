package smilegate.plop.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("jwt")
public class JwtConfig {
    private String secret;
    private long validity;
//    private void setSecret(String secret) {this.secret = secret;}
//
//    private void setValidity(long validity) {this.validity = validity;}

//    public String getSecret() {return secret;}
//
//    public long getValidity() {return validity;}
}
