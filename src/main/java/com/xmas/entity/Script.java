package com.xmas.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "scripts")
public class Script {

    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private String scriptFileName;
    @ElementCollection
    @CollectionTable(name = "script_args")
    private List<String> arguments;
    @Column
    private String outFileName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScriptFileName() {
        return scriptFileName;
    }

    public void setScriptFileName(String scriptFileName) {
        this.scriptFileName = scriptFileName;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public String getOutFileName() {
        return outFileName;
    }

    public void setOutFileName(String outFileName) {
        this.outFileName = outFileName;
    }
}
