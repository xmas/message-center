package com.xmas.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "devices")
public class Device {

    private Integer id;

    private String name;

    private String token;
}
