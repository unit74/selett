package com.selett.server.jpa.repository;

import com.selett.server.jpa.mapper.LicenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LicenseRepository extends JpaRepository<LicenseEntity, Integer> {
    List<LicenseEntity> findAllByUserId(Integer userId);
}