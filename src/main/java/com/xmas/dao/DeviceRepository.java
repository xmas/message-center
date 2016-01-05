package com.xmas.dao;

import com.xmas.entity.push.Device;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface DeviceRepository extends CrudRepository<Device, Integer>{
    @Query("SELECT device FROM Device device")
    Set<Device> getAll();
}
