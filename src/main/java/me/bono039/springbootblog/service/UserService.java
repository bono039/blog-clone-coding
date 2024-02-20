package me.bono039.springbootblog.service;

import lombok.RequiredArgsConstructor;
import me.bono039.springbootblog.domain.User;
import me.bono039.springbootblog.dto.AddUserRequest;
import me.bono039.springbootblog.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Long save(AddUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                // 1) 패스워드 암호화
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }

    // 유저 ID로 유저 검색해 전달하는 메서드
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    // 이메일 입력받아 users 테이블에서 유저를 찾고, 없으면 예외 발생
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
