package edu.uc.bearcatstudytables.service;

import java.util.List;

import edu.uc.bearcatstudytables.dto.UserDTO;

/**
 * Created by connorbowman on 10/3/17.
 */

public interface IUserService {

    void login(UserDTO user);
    void logout();
    boolean isLoggedIn();
    UserDTO create(UserDTO user);
    UserDTO update(UserDTO user);
    UserDTO fetch();
    List<UserDTO> fetchAll();
    void close();
}
