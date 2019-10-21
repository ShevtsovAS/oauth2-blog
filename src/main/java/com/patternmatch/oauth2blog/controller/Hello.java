package com.patternmatch.oauth2blog.controller;

import com.patternmatch.oauth2blog.model.Welcome;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(
        value = {"/api/hello"},
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class Hello {

    @GetMapping
    public ResponseEntity<Welcome> greetings(Principal principal) {
        return ResponseEntity.ok(new Welcome(principal.getName()));
    }
}
