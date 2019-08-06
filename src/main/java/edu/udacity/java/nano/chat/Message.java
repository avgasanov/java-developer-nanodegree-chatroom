package edu.udacity.java.nano.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * WebSocket message model
 */
public class Message {
    // TODO: add message model.

    private String type;

    @JsonProperty("msg")
    private String msg;

    private String username;

    private Integer onlineCount;

    public Message() {
    }

    public Message(String type, String msg, String username) {
        this.type = type;
        this.msg = msg;
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(Integer onlineCount) {
        this.onlineCount = onlineCount;
    }
}
