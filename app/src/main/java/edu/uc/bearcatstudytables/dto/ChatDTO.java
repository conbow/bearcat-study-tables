package edu.uc.bearcatstudytables.dto;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

/**
 * Created by connorbowman on 10/3/17.
 */

@IgnoreExtraProperties
public class ChatDTO {

    public enum types {COURSE, GROUP}

    private String name;
    private String description;
    private String type;
    private HashMap<String, Boolean> members;
    private HashMap<String, ChatMessageDTO> messages;

    public ChatDTO() {
        name = "";
        description = "";
        type = "";
        members = new HashMap<>();
        messages = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, Boolean> getMembers() {
        return members;
    }

    public void setMembers(HashMap<String, Boolean> members) {
        this.members = members;
    }

    public HashMap<String, ChatMessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(HashMap<String, ChatMessageDTO> messages) {
        this.messages = messages;
    }
}
