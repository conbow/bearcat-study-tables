package edu.uc.bearcatstudytables.dao;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import edu.uc.bearcatstudytables.dto.ChatDTO;

/**
 * Created by connorbowman on 10/19/17.
 */

public class ChatDAO implements IChatDAO {

    public static DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReference().child("chat").child("list");
    }

    public static DatabaseReference getReferenceForId(String chatId) {
        return getReference().child(chatId);
    }

    public static Query getQueryForType(String type) {
        return getReference().orderByChild("type").equalTo(type);
    }

    /**
     * Create a new chat (course or group)
     *
     * @param chat     Chat
     * @param callback Callback
     */
    @Override
    public void create(ChatDTO chat, final TaskCallback callback) {
        callback.onStart();
        getReference().push().setValue(chat)
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

    /**
     * Update a chat (course or group)
     *
     * @param chat     Chat
     * @param callback Callback
     */
    @Override
    public void update(ChatDTO chat, TaskCallback callback) {

    }
}
