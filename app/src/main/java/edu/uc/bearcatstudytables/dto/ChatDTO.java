package edu.uc.bearcatstudytables.dto;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class ChatDTO {

    public enum Type {COURSE, GROUP}

    private Type type;
    private String name = "";
    private String description = "";
    private HashMap<String, Boolean> members = new HashMap<>();
    private HashMap<String, ChatMessageDTO> messages = new HashMap<>();

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
