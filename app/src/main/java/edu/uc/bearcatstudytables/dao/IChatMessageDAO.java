package edu.uc.bearcatstudytables.dao;

import edu.uc.bearcatstudytables.dto.ChatMessageDTO;

public interface IChatMessageDAO {

    /**
     * Create a new chat message
     *
     * @param chatMessage Chat Message
     * @param callback    Callback
     */
    void create(ChatMessageDTO chatMessage, DataAccess.TaskCallback callback);
}
