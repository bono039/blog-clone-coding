package me.bono039.springbootblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.bono039.springbootblog.config.jwt.JwtFactory;
import me.bono039.springbootblog.config.jwt.JwtProperties;
import me.bono039.springbootblog.domain.RefreshToken;
import me.bono039.springbootblog.domain.User;
import me.bono039.springbootblog.dto.CreateAccessTokenRequest;
import me.bono039.springbootblog.repository.RefreshTokenRepository;
import me.bono039.springbootblog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();

        userRepository.deleteAll();
    }

    @DisplayName("createNewAccessToken: 새 액세스 토큰 발급")
    @Test
    public void createNewAccesToken() throws Exception {
        // [given]
        final String url = "/api/token";

        // 테스트 유저 생성하기
        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        // jjwt 라이브러리 사용해 리프레시 토큰 생성
        String refreshToken = JwtFactory.builder()
                .claims(Map.of("id", testUser.getId()))
                .build()
                .createToken(jwtProperties);

        // 만든 리프레시 토큰 DB에 저장하기
        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));

        // 토큰 생성 API의 요청 본문에 리프레시 토큰 포함해 요청 객체 생성하기
        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        request.setRefreshToken(refreshToken);

        final String requestBody = objectMapper.writeValueAsString(request);


        // [when]
        // 토큰 추가 API에 JSON 타입으로 요청 보내고, given 절에서 미리 만들어둔 객체를 요청 본문으로 함께 전송
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // [then]
        // 응답 코드가 201 Created인지 확인하고, 응답으로 온 액세스 토큰이 비어있지 않은지 확인
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }
}
