package com.xmas.util;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduledCrudRepository<T> extends CrudRepository<T, Long> {

    List<T> getScheduled();
}
