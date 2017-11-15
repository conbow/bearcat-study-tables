package edu.uc.bearcatstudytables.service;

import edu.uc.bearcatstudytables.dao.DataAccess;
import edu.uc.bearcatstudytables.dto.ChatDTO;
import edu.uc.bearcatstudytables.dto.ChatMessageDTO;

public interface IChatService {

    /**
     * Create a new chat (course or group)
     *
     * @param chat     Chat
     * @param callback Callback
     */
    void create(ChatDTO chat, DataAccess.TaskCallback callback);

    /**
     * Send a new chat message
     *
     * @param chatMessage Chat Message
     * @param callback    Callback
     */
    void sendMessage(ChatMessageDTO chatMessage, DataAccess.TaskCallback callback);
}
