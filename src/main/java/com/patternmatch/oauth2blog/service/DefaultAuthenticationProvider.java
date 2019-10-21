package com.patternmatch.oauth2blog.service;

import com.patternmatch.oauth2blog.config.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@RequiredArgsConstructor
public class DefaultAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        final String userEmail = StringUtils.trimToEmpty(authentication.getName());
        final String userPassword = Optional.ofNullable(authentication.getCredentials())
                .map(o -> StringUtils.trimToEmpty(o.toString()))
                .orElse(EMPTY);

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            return null;
        }

        String validUserEmail = securityProperties.getUser().getName();
        String validUserPassword = securityProperties.getUser().getPassword();

        if (userEmail.equalsIgnoreCase(validUserEmail)
                && passwordEncoder.matches(userPassword, validUserPassword)) {
            return new UsernamePasswordAuthenticationToken(
                    userEmail, userPassword, getAuthority());
        }

        throw new UsernameNotFoundException("Invalid username or password.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private Collection<? extends GrantedAuthority> getAuthority() {
        return Collections.emptyList();
    }
}
