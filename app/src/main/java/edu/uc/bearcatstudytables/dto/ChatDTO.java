package edu.uc.bearcatstudytables.dto;

import java.util.List;

/**
 * Created by connorbowman on 10/3/17.
 */

public class ChatDTO {

    private CourseDTO course;
    private List<ChatMessageDTO> messages;

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public List<ChatMessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessageDTO> messages) {
        this.messages = messages;
    }
}
