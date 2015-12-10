package com.xmas.dao;

import com.xmas.entity.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long>{
}
