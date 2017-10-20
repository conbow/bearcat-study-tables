package edu.uc.bearcatstudytables.service;

import edu.uc.bearcatstudytables.dao.IDataAccess;
import edu.uc.bearcatstudytables.dto.ChatDTO;
import edu.uc.bearcatstudytables.dto.ChatMessageDTO;

/**
 * Created by connorbowman on 10/3/17.
 */

public interface IChatService extends IDataAccess {

    /**
     * Create a new chat (course or group)
     *
     * @param chat     Chat
     * @param callback Callback
     */
    void create(ChatDTO chat, TaskCallback callback);

    /**
     * Send a new chat message
     *
     * @param chatId      Chat ID
     * @param chatMessage Chat Message
     * @param callback    Callback
     */
    void sendMessage(String chatId, ChatMessageDTO chatMessage, TaskCallback callback);
}
