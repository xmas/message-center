package com.xmas.dao;

import com.xmas.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Integer>{
    public User getUserbyGUID();
}
