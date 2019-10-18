package com.patternmatch.oauth2blog.resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HelloMvcTest {

    private static final String TEST_USER_NAME = "test@test.com";
    private static final String TEST_USER_PASSWORD = "tester";
    private static final String USER_NAME_PARAM = "username";
    private static final String USER_PASSWORD_PARAM = "password";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String TEST_USER_GRANT_TYPE = "password";
    private static final String OAUTH_TOKEN_URL = "/oauth/token";
    private static final String HTTP_BASIC_USER_NAME = "my-client";
    private static final String HTTP_BASIC_USER_PASSWORD = "my-secret";
    private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldAllowUserWithNoAuthorities() throws Exception {
        mockMvc.perform(get("/api/hello?name=Seb")
                .header(AUTHORIZATION_HEADER, obtainAccessToken())
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.greetings", is("Welcome Seb!")));
    }

    @Test
    public void shouldRejectIfNoAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/hello?name=Seb")
                .accept(MediaType.ALL))
                .andExpect(status().isUnauthorized());
    }

    private String obtainAccessToken() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(USER_NAME_PARAM, TEST_USER_NAME);
        params.add(USER_PASSWORD_PARAM, TEST_USER_PASSWORD);
        params.add(GRANT_TYPE_PARAM, TEST_USER_GRANT_TYPE);

        String resultString = mockMvc.perform(post(OAUTH_TOKEN_URL)
                .params(params)
                .with(httpBasic(HTTP_BASIC_USER_NAME, HTTP_BASIC_USER_PASSWORD))
                .accept(CONTENT_TYPE_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return BEARER + jsonParser.parseMap(resultString).get(ACCESS_TOKEN).toString();
    }
}