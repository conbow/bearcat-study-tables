package edu.uc.bearcatstudytables.service;

import java.util.List;

import edu.uc.bearcatstudytables.dao.DataAccess;
import edu.uc.bearcatstudytables.dto.UserDTO;

public interface IUserService {

    /**
     * Start Service
     * Creates and starts auth listeners
     * Should be called in Activities onStart() method
     */
    void start();

    /**
     * Login a user
     *
     * @param user     User
     * @param callback Callback
     */
    void login(UserDTO user, DataAccess.TaskCallback callback);

    /**
     * Logout current user
     */
    void logout();

    /**
     * Check if a user is authenticated
     *
     * @return boolean auth status
     */
    boolean isLoggedIn();

    /**
     * Returns current user
     *
     * @return Current user
     */
    UserDTO getCurrentUser();

    /**
     * Create a user account
     *
     * @param user     User
     * @param callback Callback
     */
    void signUp(UserDTO user, DataAccess.TaskCallback callback);

    /**
     * Fetch all users
     *
     * @param callback Callback
     */
    void fetchAll(DataAccess.TaskDataCallback<List<UserDTO>> callback);

    /**
     * Reset a user password
     *
     * @param user     User
     * @param callback Callback
     */
    void resetPassword(UserDTO user, DataAccess.TaskCallback callback);

    /**
     * Update current users profile
     *
     * @param user     User
     * @param callback Callback
     */
    void updateProfile(UserDTO user, DataAccess.TaskCallback callback);

    /**
     * Stop Service
     * Clears and removes auth listener
     * Should be called in Activities onStop() method
     */
    void stop();

    /**
     * Reload Service
     * Reloads auth state
     */
    void reload();

    interface AuthCallback {

        /**
         * Called when authentication or user data is changed (ex: user logs out or changes email)
         *
         * @param user New/changed user model
         */
        void onAuthChanged(UserDTO user);
    }
}
