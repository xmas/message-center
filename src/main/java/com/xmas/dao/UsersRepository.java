package com.xmas.dao;

import com.xmas.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<User, Long>{
    @Query("SELECT user FROM User user WHERE user.GUID = ?1")
    Optional<User> getUserByGUID(Long GUID);

    @Query("SELECT user from User user WHERE user.name = ?1")
    Optional<User> getUserByName(String name);
}
