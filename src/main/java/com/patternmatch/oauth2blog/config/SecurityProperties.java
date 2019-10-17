package com.patternmatch.oauth2blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "spring.security")
public class SecurityProperties {
    OAuth2Properties oauth2 = new OAuth2Properties();
    UserProperties user = new UserProperties();

    @Data
    static class OAuth2Properties {
        private OAuth2Client client = new OAuth2Client();

        @Data
        static class OAuth2Client {
            private String id;
            private String secret;
            private String[] grantTypes;
            private String[] scopes;
            private int accessTokenValiditySeconds;
            private int refreshTokenValiditySeconds;
        }
    }

    @Data
    static class UserProperties {
        private String name;
        private String password;
        private List<GrantedAuthority> authorities = new ArrayList<>();
    }
}
