package edu.uc.bearcatstudytables.dto;

import java.util.Date;

/**
 * Created by connorbowman on 10/3/17.
 */

public class ChatMessageDTO {

    private String courseId;
    private UserDTO from;
    private String message;
    private Date date;

    public ChatMessageDTO() {
        this.message = "";
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public UserDTO getFrom() {
        return from;
    }

    public void setFrom(UserDTO from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
