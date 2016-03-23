package com.xmas.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="tags")
@Data
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private String name;

    public Tag(String name) {
        this.name = name;
    }

}
