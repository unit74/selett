package com.selett.server.api.register.service;

import com.selett.server.api.main.service.MainService;
import com.selett.server.api.profile.service.ProfileService;
import com.selett.server.jpa.mapper.UserInfoEntity;
import com.selett.server.jpa.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RegisterService {
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final MainService mainService;
    private final ProfileService profileService;

    public void register(String identification, String password, String name, String email) {
        if(userInfoRepository.existsByIdentification(identification)) {
            throw new IllegalArgumentException("중복된 아이디입니다.");
        }

        if(userInfoRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }

        UserInfoEntity user = new UserInfoEntity();
        user.setIdentification(identification);
        user.setPassword(password);
        user.encodePassword(passwordEncoder);
        user.setName(name);
        user.setEmail(email);
        user.setRoles(List.of("ROLE_USER"));

        UserInfoEntity newUser = userInfoRepository.saveAndFlush(user);

        mainService.createList(newUser.getUserId(), "새 폴더");
        profileService.postProfileMemo(null, newUser.getUserId());
    }

    public void changePassword(Integer userId, String currentPassword, String newPassword) {
        UserInfoEntity user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 아이디입니다."));

        if(!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        user.setPassword(newPassword);
        user.encodePassword(passwordEncoder);
        userInfoRepository.saveAndFlush(user);
    }
}
