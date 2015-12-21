package com.xmas.dao;

import com.xmas.entity.UserMessage;
import org.springframework.data.repository.CrudRepository;

public interface UserMessageRepository extends CrudRepository<UserMessage, Long>{
}
