package com.selett.server.jpa;

import com.selett.server.jpa.repository.AwardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AwardEntityTest {

    @Autowired
    private AwardRepository awardRepository;

    @Test
    public void searchAll() {
        awardRepository.findAll().forEach(System.out::println);
    }

}
