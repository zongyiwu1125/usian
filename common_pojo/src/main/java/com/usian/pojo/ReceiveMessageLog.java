package com.usian.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "receive_message_log")
public class ReceiveMessageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
