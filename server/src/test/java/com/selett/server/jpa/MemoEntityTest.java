package com.selett.server.jpa;

import com.selett.server.jpa.repository.MemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemoEntityTest {

    @Autowired
    private MemoRepository memoRepository;

    @Test
    public void searchAll() {
        memoRepository.findAll().forEach(System.out::println);
    }

}
