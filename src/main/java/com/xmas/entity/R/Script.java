package com.xmas.entity.R;

import javax.persistence.*;

@Entity
@Table(name = "scripts")
public class Script {

    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private String name;
    @Column
    private String scriptFileName;
    @Column
    private String description;


    public Script() {
    }

    public Script(String scriptFileName) {
        this.scriptFileName = scriptFileName;
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

    public String getScriptFileName() {
        return scriptFileName;
    }

    public void setScriptFileName(String scriptFileName) {
        this.scriptFileName = scriptFileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
