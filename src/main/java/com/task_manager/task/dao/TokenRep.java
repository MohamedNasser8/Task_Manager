package com.task_manager.task.dao;

import com.task_manager.task.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRep extends JpaRepository<Token, Long> {

}
