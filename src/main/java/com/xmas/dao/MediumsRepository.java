package com.xmas.dao;

import com.xmas.entity.Medium;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface MediumsRepository extends CrudRepository<Medium, Integer>{
    @Query("SELECT * FROM Medium")
    Set<Medium> getAll();
}
