package com.selett.server.login.service;

import com.selett.server.jpa.mapper.UserInfoEntity;
import com.selett.server.jpa.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoginServiceTest {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    public void searchAll() {
        userInfoRepository.findAll().forEach(System.out::println);
    }

    @Test
    public void exist() {
//        System.out.println(userInfoRepository.existsByIdentification("admin"));
//        System.out.println(userInfoRepository.existsByIdentificationAndPassword("admin", "selettadmin"));
        UserInfoEntity userInfoEntity = userInfoRepository.findByIdentificationAndPassword("admin", "selettadmin");
    }
}