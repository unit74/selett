package com.selett.server.jpa.repository;

import com.selett.server.jpa.mapper.LanguageSkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageSkillRepository extends JpaRepository<LanguageSkillEntity, Integer> {
    List<LanguageSkillEntity> findAllByUserId(Integer userId);
}