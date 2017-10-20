package edu.uc.bearcatstudytables.dao;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import edu.uc.bearcatstudytables.dto.ChatMessageDTO;

/**
 * Created by connorbowman on 10/19/17.
 */

public class ChatMessageDAO implements IChatMessageDAO {

    public static DatabaseReference getReference() {
        return ChatDAO.getReference().getParent().child("message");
    }

    public static DatabaseReference getReference(String chatId) {
        return getReference().child(chatId);
    }

    /**
     * Create a new chat message
     *
     * @param chatMessage Chat Message
     * @param callback    Callback
     */
    @Override
    public void create(String chatId, ChatMessageDTO chatMessage, final TaskCallback callback) {
        callback.onStart();
        getReference(chatMessage.getCourseId()).push().setValue(chatMessage)
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
