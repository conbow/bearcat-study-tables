package edu.uc.bearcatstudytables.dao;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.uc.bearcatstudytables.dto.ChatMessageDTO;
import edu.uc.bearcatstudytables.dto.UserDTO;

public class UserDAO implements IUserDAO {

    private FirebaseAuth mAuth;

    public UserDAO() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Helper function to convert FirebaseUser to a UserDTO
     *
     * @param firebaseUser FirebaseUser
     * @return User
     */
    public static UserDTO bindToUser(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            UserDTO user = new UserDTO();
            user.setId(firebaseUser.getUid());
            user.setEmail(firebaseUser.getEmail());
            user.setName(firebaseUser.getDisplayName());
            user.setPhotoUrl(firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl()
                    .toString() : null);
            // User type is instructor if email is @uc.edu or @ucmail.uc.edu, otherwise student
            if (user.getEmail().endsWith("@uc.edu") || user.getEmail().endsWith("@ucmail.uc.edu")) {
                user.setType(UserDTO.Type.INSTRUCTOR);
            } else {
                user.setType(UserDTO.Type.STUDENT);
            }
            return user;
        }
        return null;
    }

    /**
     * Return DatabaseReference to users
     *
     * @return DatabaseReference
     */
    public static DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReference().child("user");
    }

    /**
     * Return DatabaseReference to User with ID
     *
     * @param id User ID
     * @return DatabaseReference
     */
    public static DatabaseReference getReferenceForId(String id) {
        return getReference().child(id);
    }

    /**
     * Return StorageReference for a users profile photo
     *
     * @param id User ID
     * @return StorageReference
     */
    public static StorageReference getPhotoReference(String id) {
        return FirebaseStorage.getInstance().getReference()
                .child("user")
                .child(id)
                .child("profile.jpg");
    }

    /**
     * Task to copy user to database
     * Used because FirebaseAuth has no way to fetch a list of users...
     * So we maintain id->email in Firebase database
     *
     * @param user User
     * @return Task<Void>
     */
    private Task<Void> copyUserToDatabaseTask(UserDTO user) {
        updateUserChatMessages(user);
        return getReferenceForId(user.getId()).setValue(user);
    }

    /**
     * Update users info for each of their existing chat messages
     *
     * @param user User
     */
    private void updateUserChatMessages(final UserDTO user) {
        ChatMessageDAO.getReference().orderByChild("from/id").equalTo(user.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot chatMessageDataSnapshot : dataSnapshot.getChildren()) {
                            ChatMessageDTO chatMessage = chatMessageDataSnapshot
                                    .getValue(ChatMessageDTO.class);
                            if (chatMessage != null) {
                                chatMessageDataSnapshot.getRef().child("from").setValue(user);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Create a new user
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void create(final UserDTO user, final DataAccess.TaskCallback callback) {
        callback.onStart();
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser firebaseUser = task.getResult().getUser();
                            final TaskBatch taskBatch = new TaskBatch();
                            user.setId(firebaseUser.getUid());

                            // Update user auth profile name and photo url
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                                    .Builder().setDisplayName(user.getName())
                                    .setPhotoUri(Uri.parse(user.getPhotoUrl())).build();
                            taskBatch.addTask(firebaseUser.updateProfile(profileUpdates));

                            // We'll copy to the database as well
                            taskBatch.addTask(copyUserToDatabaseTask(user));

                            // Batch callback
                            taskBatch.addObserver(new Observer() {
                                @Override
                                public void update(Observable observable, Object o) {
                                    if (taskBatch.isAllComplete()) {
                                        callback.onComplete();
                                        if (taskBatch.isAllSuccessful()) {
                                            callback.onSuccess();
                                        } else {
                                            callback.onFailure(taskBatch.getException());
                                        }
                                    }
                                }
                            });
                        } else {
                            callback.onComplete();
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    /**
     * Fetch all users
     *
     * @param callback Callback
     */
    @Override
    public void fetchAll(final DataAccess.TaskDataCallback<List<UserDTO>> callback) {
        callback.onStart();
        getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<UserDTO> users = new ArrayList<>();
                for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                    UserDTO user = dataSnapshotChild.getValue(UserDTO.class);
                    if (user != null) {
                        users.add(user);
                    }
                }
                callback.onComplete();
                callback.onSuccess(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onComplete();
                callback.onFailure(databaseError.toException());
            }
        });
    }

    /**
     * Fetch single user with ID
     *
     * @param userId   User ID
     * @param callback Callback
     */
    @Override
    public void fetchById(String userId, DataAccess.TaskDataCallback<UserDTO> callback) {

    }

    /**
     * Fetch all users that match
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void fetch(UserDTO user, DataAccess.TaskDataCallback<List<UserDTO>> callback) {

    }

    private OnCompleteListener<Task> onComplete(final DataAccess.TaskCallback callback) {
        return new OnCompleteListener<Task>() {
            @Override
            public void onComplete(@NonNull Task<Task> task) {
                callback.onComplete();
                if (task.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure(task.getException());
                }
            }
        };
    }

    /**
     * Update a user
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void update(final UserDTO user, final DataAccess.TaskCallback callback) {

        // TODO Clean this up, it was callback hell, now probably worse
        // The challenge is that there is no way to update every profile field at once
        // so the goal was to get everything into one callback for simplicity in the UI

        callback.onStart();

        // Get current Firebase user
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            return;
        }

        final TaskBatch taskBatch = new TaskBatch();

        // Upload photo, if it exists
        if (user.getPhoto() != null) {
            UploadTask uploadTask = getPhotoReference(user.getId()).putBytes(user.getPhoto());
            uploadTask.addOnCompleteListener(
                    new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful() && task.getResult().getDownloadUrl() != null) {
                                user.setPhotoUrl(task.getResult().getDownloadUrl().toString());
                            }
                            taskBatch.addTask(firebaseUser
                                    .updateProfile(new UserProfileChangeRequest
                                            .Builder().setPhotoUri(Uri.parse(user.getPhotoUrl()))
                                            .build()), false);
                        }
                    });
            taskBatch.incTaskCount();
            taskBatch.addTask(uploadTask);
        }

        // Update user profile name if it changed
        if (user.getName() != null && !user.getName().equals(firebaseUser.getDisplayName())) {
            taskBatch.addTask(firebaseUser.updateProfile(
                    new UserProfileChangeRequest.Builder().setDisplayName(user.getName()).build()));
        }

        // Update email if it changed
        if (user.getEmail() != null && !user.getEmail().equals(firebaseUser.getEmail())) {
            taskBatch.addTask(firebaseUser.updateEmail(user.getEmail()));
        }

        // If anything was changed we want to copy the user to the database at the end
        if (taskBatch.getTaskCount() > 0) {
            final int taskCount = taskBatch.getTaskCount();
            taskBatch.addObserver(new Observer() {
                @Override
                public void update(Observable observable, Object o) {
                    if (taskBatch.getCompletedTaskCount() == taskCount) {
                        taskBatch.addTask(copyUserToDatabaseTask(user), false);
                    }
                }
            });
            taskBatch.incTaskCount();
        }

        // Update password if it changed (if it's set, it will be changed)
        // Changed at the end otherwise it may cause session problems for other concurrent requests
        if (!user.getPassword().isEmpty()) {
            final int taskCount = taskBatch.getTaskCount();
            taskBatch.addObserver(new Observer() {
                @Override
                public void update(Observable observable, Object o) {
                    if (taskBatch.getCompletedTaskCount() == taskCount) {
                        taskBatch.addTask(firebaseUser.updatePassword(user.getPassword()), false);
                    }
                }
            });
            taskBatch.incTaskCount();
        }

        // Batch callback if we ran anything, otherwise just call onComplete
        final int tasksCount = taskBatch.getTaskCount();
        if (tasksCount > 0) {
            taskBatch.addObserver(new Observer() {
                @Override
                public void update(Observable observable, Object o) {
                    // When everything is complete, callback
                    if (taskBatch.isAllComplete()) {
                        callback.onComplete();
                        if (taskBatch.isAllSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(taskBatch.getException());
                        }
                    }
                }
            });
        } else {
            callback.onComplete();
        }
    }

    /**
     * Batches many tasks together and returns one callback
     * This was almost certainly misguided
     */
    private class TaskBatch extends Observable {

        private int taskCount = 0;
        private List<Task> completedTasks = new ArrayList<>();

        int getTaskCount() {
            return taskCount;
        }

        void incTaskCount() {
            taskCount++;
            setChanged();
            notifyObservers();
        }

        int getCompletedTaskCount() {
            return completedTasks.size();
        }

        List<Task> getCompletedTasks() {
            return completedTasks;
        }

        void addTask(Task<Void> task) {
            addTask(task, true);
        }

        void addTask(Task<Void> task, boolean incrementTaskCount) {
            if (incrementTaskCount)
                taskCount++;
            task.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    completedTasks.add(task);
                    setChanged();
                    notifyObservers();
                }
            });
        }

        void addTask(UploadTask uploadTask) {
            taskCount++;
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    completedTasks.add(task);
                    setChanged();
                    notifyObservers();
                }
            });
        }

        boolean isAllSuccessful() {
            for (Task task : completedTasks) {
                if (!task.isSuccessful()) {
                    return false;
                }
            }
            return true;
        }

        boolean isAllComplete() {
            return getCompletedTaskCount() == getTaskCount();
        }

        Exception getException() {
            for (Task task : completedTasks) {
                if (!task.isSuccessful()) {
                    try {
                        if (task.getException() != null)
                            throw task.getException();
                    } catch (Exception e) {
                        return e;
                    }
                }
            }
            return null;
        }
    }
}
