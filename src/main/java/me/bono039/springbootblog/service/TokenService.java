package me.bono039.springbootblog.service;

import lombok.RequiredArgsConstructor;
import me.bono039.springbootblog.config.jwt.TokenProvider;
import me.bono039.springbootblog.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    // 전달받은 리프레시 토큰으로 유효성 검사하고, 유효한 토큰일 때 리프레시 토큰으로 사용자 ID 찾는 메서드
    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected Token");
        }

        // 사용자 ID로 사용자 찾은 후, 토큰 제공자의 generateToken() 메서드 호출해 새 액세스 토큰 생성
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
