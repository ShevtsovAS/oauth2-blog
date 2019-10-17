package com.patternmatch.oauth2blog.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authManager;
    private final SecurityProperties securityProperties;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        SecurityProperties.OAuth2Properties.OAuth2Client client = securityProperties.getOauth2().getClient();
        clients
                .inMemory()
                .withClient(client.getId())
                .secret(client.getSecret())
                .authorizedGrantTypes(client.getGrantTypes())
                .scopes(client.getScopes())
                .accessTokenValiditySeconds(client.getAccessTokenValiditySeconds())
                .refreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore()).authenticationManager(authManager);
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }
}
