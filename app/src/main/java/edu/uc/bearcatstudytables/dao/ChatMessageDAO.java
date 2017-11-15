package edu.uc.bearcatstudytables.dao;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import edu.uc.bearcatstudytables.dto.ChatMessageDTO;

public class ChatMessageDAO implements IChatMessageDAO {

    public static DatabaseReference getReference() {
        return ChatDAO.getReference().child("message");
    }

    public static DatabaseReference getReference(String chatId) {
        return getReference().orderByChild("chatId").equalTo(chatId).getRef();
    }

    /**
     * Create a new chat message
     *
     * @param chatMessage Chat Message
     * @param callback    Callback
     */
    @Override
    public void create(ChatMessageDTO chatMessage, final DataAccess.TaskCallback callback) {
        callback.onStart();
        getReference(chatMessage.getChatId()).push().setValue(chatMessage)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onComplete();
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }
}
