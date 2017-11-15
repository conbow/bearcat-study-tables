package edu.uc.bearcatstudytables.dao;

import java.util.List;

import edu.uc.bearcatstudytables.dto.UserDTO;

public class UserDAOStub implements IUserDAO {

    /**
     * Create a new user
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void create(UserDTO user, DataAccess.TaskCallback callback) {

    }

    /**
     * Fetch all users
     *
     * @param callback Callback
     */
    @Override
    public void fetchAll(DataAccess.TaskDataCallback<List<UserDTO>> callback) {

    }

    /**
     * Fetch single user with ID
     *
     * @param userId   User ID
     * @param callback Callback
     */
    @Override
    public void fetchById(String userId, DataAccess.TaskDataCallback<UserDTO> callback) {

    }

    /**
     * Fetch all users that match
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void fetch(UserDTO user, DataAccess.TaskDataCallback<List<UserDTO>> callback) {

    }

    /**
     * Update a user
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void update(UserDTO user, DataAccess.TaskCallback callback) {

    }
}
