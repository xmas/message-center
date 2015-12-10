package com.xmas.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    private Long id;

    private String message;

    private String title;

    private String subTitle;

    private String icon;

    private LocalDateTime expiration;

    private String notificationAppURL;

    private MimeType mimeType;

    private String messageType;

    public Message(String message) {
        this.message = message;
    }
}
