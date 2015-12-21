package com.xmas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "users")
@SuppressWarnings("UnusedDeclaration")
public class User {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer id;

    @Column
    private String name;

    @Column
    @NotNull
    private Long guid;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Device> devices;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<UserMessage> userMessages;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getGuid() {
        return guid;
    }

    public void setGuid(Long guid) {
        this.guid = guid;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public List<UserMessage> getUserMessages() {
        return userMessages;
    }

    public void setUserMessages(List<UserMessage> userMessages) {
        this.userMessages = userMessages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return guid.equals(user.guid);

    }

    @Override
    public int hashCode() {
        return guid.hashCode();
    }
}
