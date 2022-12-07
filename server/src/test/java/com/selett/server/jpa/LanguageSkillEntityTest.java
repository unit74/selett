package com.selett.server.jpa;

import com.selett.server.jpa.repository.LanguageSkillRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LanguageSkillEntityTest {

    @Autowired
    private LanguageSkillRepository languageSkillRepository;

    @Test
    public void searchAll() {
        languageSkillRepository.findAll().forEach(System.out::println);
    }

}
