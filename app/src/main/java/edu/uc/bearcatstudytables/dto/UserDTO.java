package edu.uc.bearcatstudytables.dto;

/**
 * Created by connorbowman on 10/3/17.
 */

public class UserDTO {

    private String id;
    private String email;
    private String name;
    private String password;

    public UserDTO() {
        this.email = "";
        this.password = "";
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
