package edu.uc.bearcatstudytables.service;

import edu.uc.bearcatstudytables.dao.IDataAccess;
import edu.uc.bearcatstudytables.dto.UserDTO;

/**
 * Created by connorbowman on 10/3/17.
 */

public interface IUserService extends IDataAccess {

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
    void login(UserDTO user, TaskCallback callback);

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
    void signUp(UserDTO user, TaskCallback callback);

    /**
     * Reset a user password
     *
     * @param user     User
     * @param callback Callback
     */
    void resetPassword(UserDTO user, TaskCallback callback);

    /**
     * Update current users profile
     *
     * @param user     User
     * @param callback Callback
     */
    void updateProfile(UserDTO user, TaskCallback callback);

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
