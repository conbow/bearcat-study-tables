package edu.uc.bearcatstudytables.dto;

import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class ChatMessageDTO {

    public enum Type {TEXT, PHOTO, FILE}

    private String chatId;
    private UserDTO from;
    private Type type;
    private String message = "";
    private Uri localFileUri;
    private String fileUrl = "";
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

    @Exclude
    public Uri getLocalFileUri() {
        return localFileUri;
    }

    public void setLocalFileUri(Uri localFileUri) {
        this.localFileUri = localFileUri;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
