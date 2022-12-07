package com.selett.server.jpa.repository;

import com.selett.server.jpa.mapper.ListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListRepository extends JpaRepository<ListEntity, Integer> {
    List<ListEntity> findAllByUserId(Integer userId);
    Boolean existsByUserIdAndTitle(Integer userId, String title);
    ListEntity findByUserIdAndNext(Integer userId, Integer next);
    Long countByUserId(Integer userId);
    ListEntity findByListId(Integer listId);
}
