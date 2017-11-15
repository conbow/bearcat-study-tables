package edu.uc.bearcatstudytables.dao;

import edu.uc.bearcatstudytables.dto.ChatDTO;

public interface IChatDAO {

    /**
     * Create a new chat (course or group)
     *
     * @param chat     Chat
     * @param callback Callback
     */
    void create(ChatDTO chat, DataAccess.TaskCallback callback);

    /**
     * Update a chat (course or group)
     *
     * @param chat     Chat
     * @param callback Callback
     */
    void update(ChatDTO chat, DataAccess.TaskCallback callback);
}
