package edu.uc.bearcatstudytables.dao;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import edu.uc.bearcatstudytables.dto.ChatMessageDTO;

public class ChatMessageDAO implements IChatMessageDAO {

    public static DatabaseReference getReference() {
        return ChatDAO.getReference().child("message");
    }

    public static Query getQueryForChatId(String chatId) {
        return getReference().orderByChild("chatId").equalTo(chatId);
    }

    public static StorageReference getReferenceForChatMessageFile(String chatId, String key) {
        return FirebaseStorage.getInstance().getReference()
                .child(ChatDAO.getReference().getKey())
                .child(getReference().getKey())
                .child(chatId)
                .child(key);
    }

    private void createMessage(DatabaseReference databaseReference, ChatMessageDTO chatMessage,
                               final DataAccess.TaskCallback callback) {
        databaseReference.setValue(chatMessage)
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

    private void uploadFileAndCreateMessage(final DatabaseReference databaseReference,
                                            final ChatMessageDTO chatMessage,
                                            final DataAccess.TaskCallback callback) {
        UploadTask uploadTask = getReferenceForChatMessageFile(chatMessage.getChatId(),
                databaseReference.getKey()).putFile(chatMessage.getLocalFileUri());
        uploadTask.addOnCompleteListener(
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().getDownloadUrl() != null) {
                            chatMessage.setFileUrl(task.getResult().getDownloadUrl().toString());
                            createMessage(databaseReference, chatMessage, callback);
                        } else {
                            callback.onComplete();
                            callback.onFailure(task.getException());
                        }
                    }
                });
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

        // Set chat message date
        chatMessage.setDate(new Date());

        // Create chat message reference
        DatabaseReference ref = getReference().push();

        // If we have a file then upload it and create the message, otherwise just create message
        if (chatMessage.getLocalFileUri() != null) {
            chatMessage.setMessage(null);
            uploadFileAndCreateMessage(ref, chatMessage, callback);
        } else {
            createMessage(ref, chatMessage, callback);
        }
    }
}
