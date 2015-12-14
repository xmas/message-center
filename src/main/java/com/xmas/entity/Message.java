package com.xmas.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "messages")
@SuppressWarnings("UnusedDeclaration")
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String message;

    @Column
    private String title;

    @Column
    private String subTitle;

    @Column
    private String icon;

    @Column
    private LocalDateTime created;

    @Column
    private LocalDateTime expiration;

    @Column
    private String notificationAppURL;

    @Column
    private String mimeType;

    @Column
    private String messageType;

    @Column
    private boolean accepted;

    @ManyToMany
    @JoinTable(name = "messages_mediums",
            joinColumns = @JoinColumn(name = "messageId"),
            inverseJoinColumns = @JoinColumn(name = "mediumId"))
    @Column(name = "medium", nullable = false)
    private Set<Medium> mediums;

    @ManyToMany
    @JoinTable(name = "user_message",
            joinColumns = @JoinColumn(name = "messageId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private List<User> users;

    public Message() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public String getNotificationAppURL() {
        return notificationAppURL;
    }

    public void setNotificationAppURL(String notificationAppURL) {
        this.notificationAppURL = notificationAppURL;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Set<Medium> getMediums() {
        return mediums;
    }

    public void setMediums(Set<Medium> mediums) {
        this.mediums = mediums;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
