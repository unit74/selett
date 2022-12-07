package com.selett.server.jpa;

import com.selett.server.jpa.repository.ListRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ListEntityTest {

    @Autowired
    private ListRepository listRepository;

    @Test
    public void searchAll() {
        listRepository.findAll().forEach(System.out::println);
    }

    @Test
    public void save() {
//        ListEntity listEntity = new ListEntity();
//
//        listEntity.setTitle("test");
//        listEntity.setPrev(3);
//        listEntity.setNext(4);
//        listEntity.setUserId(1);
//
//        listRepository.save(listEntity);
//        searchAll();
//        ListEntity listEntity = new ListEntity();
//        listEntity.setTitle("조금 긴 String 저장해보기");
//        listEntity.setPosition(2);
//        listEntity.setUserId(1);
//
//        listRepository.save(listEntity);
//        searchAll();
    }
}
