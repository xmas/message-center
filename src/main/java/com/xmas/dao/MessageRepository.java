package com.xmas.dao;

import com.xmas.entity.push.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MessageRepository extends CrudRepository<Message, Long>{
    @Query("SELECT message FROM Message message WHERE message.id = ?1")
    Optional<Message> get(Long id);

}
