package edu.uc.bearcatstudytables.dao;

import edu.uc.bearcatstudytables.dto.ChatMessageDTO;

/**
 * Created by connorbowman on 10/19/17.
 */

public interface IChatMessageDAO extends IDataAccess {

    /**
     * Create a new chat message
     *
     * @param chatId      Chat ID
     * @param chatMessage Chat Messagge
     * @param callback    Callback
     */
    void create(String chatId, ChatMessageDTO chatMessage, TaskCallback callback);
}
