package edu.uc.bearcatstudytables.dao;

import java.util.List;

import edu.uc.bearcatstudytables.dto.UserDTO;

/**
 * Created by connorbowman on 10/3/17.
 */

public class UserDAOStub implements IUserDAO {

    /**
     * Create a new user
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void create(UserDTO user, TaskCallback callback) {

    }

    /**
     * Fetch all users
     *
     * @param callback Callback
     */
    @Override
    public void fetch(TaskDataCallback<List<UserDTO>> callback) {

    }

    /**
     * Fetch single user with ID
     *
     * @param userId   User ID
     * @param callback Callback
     */
    @Override
    public void fetch(String userId, UserDTO callback) {

    }

    /**
     * Fetch all users that match
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void fetch(UserDTO user, TaskDataCallback<List<UserDTO>> callback) {

    }

    /**
     * Update a user
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void update(UserDTO user, TaskCallback callback) {

    }
}
