package com.xmas.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "mediumId")
    @NotNull(message = "Device type must be presented")
    private Medium medium;

    @Column
    @NotNull(message = "Token for devise must be presented.")
    private String token;

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
