package edu.uc.bearcatstudytables.viewmodel;

import edu.uc.bearcatstudytables.dto.UserDTO;

/**
 * Created by connorbowman on 10/5/17.
 */

public class AuthViewModel extends SingleTaskViewModel {

    private UserDTO user = new UserDTO();

    public AuthViewModel() {
        user = new UserDTO();
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
