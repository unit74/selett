package com.selett.server.jpa;

import com.selett.server.jpa.repository.EducationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EducationEntityTest {

    @Autowired
    private EducationRepository educationRepository;

    @Test
    public void searchAll() {
        educationRepository.findAll().forEach(System.out::println);
    }

}
