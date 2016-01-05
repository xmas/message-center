package com.xmas.entity.questions;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToMany
    @JoinTable(name = "messages_tags",
            joinColumns = @JoinColumn(name = "message"),
            inverseJoinColumns = @JoinColumn(name = "tag"))
    private Collection<Tag> tags;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }
}
