package com.xmas.entity.push;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "devices")
@SuppressWarnings("UnusedDeclaration")
public class Device {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "mediumId")
    @NotNull(message = "Device type must be presented")
    private Medium medium;

    @Column(unique = true)
    @NotNull(message = "Token for devise must be presented.")
    private String token;

    @Column
    private String ip;

    @Column
    private String location;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        return medium.equals(device.medium) && token.equals(device.token);
    }

    @Override
    public int hashCode() {
        int result = medium.hashCode();
        result = 31 * result + token.hashCode();
        return result;
    }
}
