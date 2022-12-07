package com.selett.server.jpa.repository;

import com.selett.server.jpa.mapper.MemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<MemoEntity, Integer> {
    MemoEntity findByUserId(Integer userId);
}