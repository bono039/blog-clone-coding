package me.bono039.springbootblog.config.jwt;

import io.jsonwebtoken.Jwts;
import me.bono039.springbootblog.domain.User;
import me.bono039.springbootblog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    // 1) generateToken() 검증 테스트 - 토큰 생성 메서드 테스트
    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        // [given] 토큰에 유저 정보 추가 위한 테스트 유저 생성
        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        // [when] 토큰 제공자의 메서드 호출해 토큰 생성
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        // [then] jjwt 라이브러리 사용해 토큰 복호화.
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    // 2) validToken() 검증 테스트 - 토큰이 유효한지 검증하는 메서드 테스트
    @DisplayName("invalidToken(): 만료된 토큰인 때에 유효성 검증에 실패한다..")
    @Test
    void validToken_invalidToken() {
        // [given] jjwt라이브러리 사용해 이미 만료된 토큰 생성
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        // [when] 유효한 토큰인지 검증 후 결과값 받음
        boolean result = tokenProvider.validToken(token);

        // [then] 유효한 토큰이 아님(false)을 확인
        assertThat(result).isFalse();
    }

    @DisplayName("validToken(): 유효한 토큰인 때에 유효성 검증에 성공한다..")
    @Test
    void validToken_validToken() {
        // [given] jjwt라이브러리 사용해 만료되지 않은 토큰 생성 (14일 후)
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        // [when] 유효한 토큰인지 검증 후 결과값 받음
        boolean result = tokenProvider.validToken(token);

        // [then] 유효한 토큰임(true)을 확인
        assertThat(result).isTrue();
    }

    // 3) getAuthentication() 검증 테스트 - 토큰을 전달받아 인증 정보 담은 객체 Authentication 반환하는 메서드 테스트
    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        // [given] jjwt라이브러리 사용해 토큰 생성
        String userEmail = "user@email.com";

        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        // [when] 인증 객체 반환받음
        Authentication auth = tokenProvider.getAuthentication(token);

        // [then] 반환받은 인증 객체의 유저 이름이 given절의 subejct 값과 같은지 확인
        assertThat(((UserDetails) auth.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    // 4) getUserId() 검증 테스트 - 토큰 기반 유저 ID 가져오는 메서드 테스트
    @DisplayName("getUserId(): 토큰으로 유저ID를 가져올 수 있다.")
    @Test
    void getUserId() {
        // [given] jjwt라이브러리 사용해 토큰 생성하고, 클레임 추가
        Long userId = 1L;

        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        // [when] 유저ID 반환받음
        Long userIdByToken = tokenProvider.getUserId(token);

        // [then] 반환받은 유저 ID가 given절의 유저ID 값과 같은지 확인
        assertThat(userIdByToken).isEqualTo(userId);
    }
}
