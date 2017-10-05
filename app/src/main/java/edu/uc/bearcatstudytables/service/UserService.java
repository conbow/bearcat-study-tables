package edu.uc.bearcatstudytables.service;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import edu.uc.bearcatstudytables.dto.UserDTO;

/**
 * Created by connorbowman on 10/3/17.
 */

public class UserService implements IUserService {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public UserService() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void login(UserDTO user) {
        String email = user.getEmail();
        String password = user.getPassword();

        mAuth = FirebaseAuth.getInstance();


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
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
