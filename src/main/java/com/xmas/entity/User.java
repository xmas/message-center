package com.xmas.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    private Integer id;

    private Set<Device> devices;
}
