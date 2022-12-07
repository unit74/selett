package com.selett.server.jpa.repository;

import com.selett.server.jpa.mapper.CoverLetterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoverLetterRepository extends JpaRepository<CoverLetterEntity, Integer> {
    List<CoverLetterEntity> findAllByListId(Integer listId);
    Boolean existsByListIdAndTitle(Integer listId, String title);
    Long countByListId(Integer listId);
    CoverLetterEntity findByListIdAndNext(Integer listId, Integer Next);
}