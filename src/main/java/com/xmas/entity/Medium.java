package com.xmas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "mediums")
@SuppressWarnings("UnusedDeclaration")
public class Medium {

    public static final String CHROME = "chrome";
    public static final String SAFARI = "safari";

    public static final Map<String, Integer> IMPLEMENTED_MEDIUMS = new HashMap<String, Integer>(){{
            put(CHROME, 0);
            put(SAFARI, 1);
    }};

    public static final String VALIDATION_MESSAGE_TEMPLATE = "Medium ${validatedValue} is not implemented yet." +
                                                             "Only chrome, safari allowed.";

    @Id
    @JsonIgnore
    private int id;

    @Column
    @Pattern(regexp = "^\\s*chrome\\s*$|^\\s*safary\\s*$", message = VALIDATION_MESSAGE_TEMPLATE )
    private String name;

    public Medium(){}

    public Medium(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @PrePersist
    public void setId(){
        id = IMPLEMENTED_MEDIUMS.get(name.trim().toLowerCase());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim().toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Medium medium = (Medium) o;

        return name.trim().equalsIgnoreCase(medium.name.trim());

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
