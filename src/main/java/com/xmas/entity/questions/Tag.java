package com.xmas.entity.questions;

import javax.persistence.*;

@Entity
@Table(name="tags")
public class Tag {

    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
