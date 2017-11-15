package edu.uc.bearcatstudytables.dto;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class ChatMessageDTO {

    public enum Type {TEXT, PHOTO, FILE}

    private String chatId;
    private UserDTO from;
    private Type type;
    private String message = "";
    private String url = "";
    private Date date;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String courseId) {
        this.chatId = courseId;
    }

    public UserDTO getFrom() {
        return from;
    }

    public void setFrom(UserDTO from) {
        this.from = from;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
