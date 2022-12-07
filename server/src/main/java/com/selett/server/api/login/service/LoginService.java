package com.selett.server.api.login.service;

import com.selett.server.api.login.dto.LoginResponse;
import com.selett.server.jpa.mapper.UserInfoEntity;
import com.selett.server.jpa.repository.UserInfoRepository;
import com.selett.server.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(String identification, String password) {
        UserInfoEntity user = userInfoRepository.findByIdentification(identification)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 아이디입니다."));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(user.getUserId());
        loginResponse.setToken(jwtTokenProvider.createToken(user.getIdentification(), user.getRoles()));

        return loginResponse;
    }
}
