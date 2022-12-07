package com.selett.server.jpa;

import com.selett.server.jpa.repository.CoverLetterRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CoverLetterEntityTest {

    @Autowired
    private CoverLetterRepository coverLettersRepository;

    @Test
    public void searchAll() {
        coverLettersRepository.findAll().forEach(System.out::println);
    }

}
