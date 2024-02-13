package me.bono039.springbootblog.controller;

import lombok.RequiredArgsConstructor;
import me.bono039.springbootblog.dto.CreateAccessTokenRequest;
import me.bono039.springbootblog.dto.CreateAccessTokenResponse;
import me.bono039.springbootblog.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {

    private final TokenService tokenService;

    // 토큰 서비스에서 리프레시 토큰을 기반으로 새 액세스 토큰을 만드는 메서드
    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
