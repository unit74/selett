package com.selett.server.jpa.repository;

import com.selett.server.jpa.mapper.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Integer> {
    UserInfoEntity findByIdentificationAndPassword(String identification, String password);
    Boolean existsByIdentification(String identification);
    Boolean existsByEmail(String email);
    Optional<UserInfoEntity> findByIdentification(String identification);
}