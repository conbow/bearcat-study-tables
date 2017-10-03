package edu.uc.bearcatstudytables.dto;

import java.util.List;

/**
 * Created by connorbowman on 10/3/17.
 */

public class ChatDTO {

    private CourseDTO course;
    private List<ChatMessage> messages;

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
