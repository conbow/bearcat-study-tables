package edu.uc.bearcatstudytables.dto;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by connorbowman on 10/3/17.
 */

@IgnoreExtraProperties
public class UserDTO {

    public enum types {STUDENT, INSTRUCTOR}

    private String id;
    private String email;
    private String name;
    private String password;
    private String photoUrl;
    private byte[] photo;
    private String type;

    public UserDTO() {
        id = "";
        email = "";
        name = "";
        password = "";
        photoUrl = "";
        type = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Exclude
    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserDTO) {
            UserDTO user = (UserDTO) obj;
            return (id == null || id.equals(user.getId())
                    && (email == null || email.equals(user.getEmail()))
                    && (name == null || name.equals(user.getName()))
                    && (photoUrl == null || photoUrl.equals(user.getPhotoUrl())));
        }
        return super.equals(obj);
    }
}
