package edu.uc.bearcatstudytables.dao;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import edu.uc.bearcatstudytables.dto.ChatDTO;
import edu.uc.bearcatstudytables.dto.UserDTO;

import static android.R.attr.type;

/**
 * Created by connorbowman on 10/19/17.
 */

public class ChatDAO implements IChatDAO {

    public static DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReference().child("chat");
    }

    public static DatabaseReference getReferenceForType(ChatDTO.types type) {
        return FirebaseDatabase.getInstance().getReference().child("chat").child(type.name()
                .toLowerCase());
    }

    public static DatabaseReference getReferenceForId(ChatDTO.types type, String chatId) {
        return getReference().child(chatId);
    }

    /*
    public static Query getQueryForType(UserDTO.types type) {
        return getReference(type).orderByChild("type").equalTo(type);
    }*/

    /**
     * Create a new chat (course or group)
     *
     * @param chat     Chat
     * @param callback Callback
     */
    @Override
    public void create(ChatDTO chat, final TaskCallback callback) {
        callback.onStart();
        getReferenceForType(ChatDTO.types.valueOf(chat.getType())).push().setValue(chat)
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
