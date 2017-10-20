package edu.uc.bearcatstudytables.dao;

import edu.uc.bearcatstudytables.dto.ChatDTO;

/**
 * Created by connorbowman on 10/3/17.
 */

public interface IChatDAO extends IDataAccess {

    /**
     * Create a new chat (course or group)
     *
     * @param chat     Chat
     * @param callback Callback
     */
    void create(ChatDTO chat, TaskCallback callback);

    /**
     * Update a chat (course or group)
     *
     * @param chat     Chat
     * @param callback Callback
     */
    void update(ChatDTO chat, TaskCallback callback);
}
