package edu.uc.bearcatstudytables.dto;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by connorbowman on 10/3/17.
 */

@IgnoreExtraProperties
public class CourseDTO {

    private String uid;
    private String name;
    private String description;

    public CourseDTO() {
        this.name = "";
    }

    public CourseDTO(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
