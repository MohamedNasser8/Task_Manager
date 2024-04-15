package com.task_manager.task.dao;

import com.task_manager.task.models.Token;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
@SpringBootTest
class TokenRepTest {
    @Autowired
    TokenRep tokenRep;
    @Test
    void name() {
        tokenRep.deleteById(Long.parseLong("402"));
    }
}