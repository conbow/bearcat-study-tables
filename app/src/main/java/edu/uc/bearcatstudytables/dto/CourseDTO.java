package edu.uc.bearcatstudytables.dto;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.UUID;

/**
 * Created by connorbowman on 10/3/17.
 */


public class CourseDTO {

    private String uid;
    private String id;
    private String name;
    private String description;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public CourseDTO() {
        this.name = "";
    }

    public CourseDTO(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
