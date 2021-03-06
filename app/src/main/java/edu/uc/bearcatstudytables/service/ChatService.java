package edu.uc.bearcatstudytables.service;

import edu.uc.bearcatstudytables.dao.ChatDAO;
import edu.uc.bearcatstudytables.dao.ChatMessageDAO;
import edu.uc.bearcatstudytables.dao.DataAccess;
import edu.uc.bearcatstudytables.dao.IChatDAO;
import edu.uc.bearcatstudytables.dao.IChatMessageDAO;
import edu.uc.bearcatstudytables.dto.ChatDTO;
import edu.uc.bearcatstudytables.dto.ChatMessageDTO;

public class ChatService implements IChatService {

    private IChatDAO mChatDAO;
    private IChatMessageDAO mChatMessageDAO;

    public ChatService() {
        // Create ChatDAO and ChatMessageDAO instances
        mChatDAO = new ChatDAO();
        mChatMessageDAO = new ChatMessageDAO();
    }

    /**
     * Create a new chat (course or group)
     *
     * @param chat     Chat
     * @param callback Callback
     */
    @Override
    public void create(ChatDTO chat, DataAccess.TaskCallback callback) {
        mChatDAO.create(chat, callback);
    }

    /**
     * Send a new chat message
     *
     * @param chatMessage Chat Message
     * @param callback    Callback
     */
    @Override
    public void sendMessage(ChatMessageDTO chatMessage, DataAccess.TaskCallback callback) {
        mChatMessageDAO.create(chatMessage, callback);
    }
}
