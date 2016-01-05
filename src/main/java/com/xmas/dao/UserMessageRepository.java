package com.xmas.dao;

import com.xmas.entity.push.UserMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserMessageRepository extends CrudRepository<UserMessage, Long>{
    @Query("SELECT userMessage FROM UserMessage userMessage WHERE userMessage.user.guid = ?1 AND userMessage.accepted = false")
    List<UserMessage> getUnRead(Long guid);
}
