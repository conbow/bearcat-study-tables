package edu.uc.bearcatstudytables.service;

import java.util.List;

import edu.uc.bearcatstudytables.dto.UserDTO;

/**
 * Created by connorbowman on 10/3/17.
 */

public class UserServiceStub implements IUserService {

    @Override
    public void login(UserDTO user) {

    }

    @Override
    public void logout() {

    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public UserDTO create(UserDTO user) {
        return null;
    }

    @Override
    public UserDTO update(UserDTO user) {
        return null;
    }

    @Override
    public UserDTO fetch() {
        return null;
    }

    @Override
    public List<UserDTO> fetchAll() {
        return null;
    }

    @Override
    public void close() {

    }
}
