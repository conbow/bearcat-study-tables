package edu.uc.bearcatstudytables.dao;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.uc.bearcatstudytables.dto.UserDTO;

/**
 * Created by connorbowman on 10/19/17.
 */

public class UserDAO implements IUserDAO {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    public UserDAO() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    /**
     * Helper function to convert FirebaseUser to a UserDTO
     *
     * @param firebaseUser FirebaseUser
     * @return User
     */
    public static UserDTO bindToUser(FirebaseUser firebaseUser) {
        UserDTO user = null;
        if (firebaseUser != null) {
            user = new UserDTO();
            user.setId(firebaseUser.getUid());
            user.setEmail(firebaseUser.getEmail());
            user.setName(firebaseUser.getDisplayName());
            user.setPhotoUrl(firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl()
                    .toString() : null);
            // User type is instructor if email ends with @uc.edu, otherwise student
            if (user.getEmail().endsWith("@uc.edu")) {
                user.setType(UserDTO.types.INSTRUCTOR.name());
            } else {
                user.setType(UserDTO.types.STUDENT.name());
            }
        }
        return user;
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
     * Task to copy user to database
     * Used because FirebaseAuth has no way to fetch a list of users...
     * So we maintain id->email in Firebase database
     *
     * @param user User
     */
    private Task<Void> copyUserToDatabaseTask(UserDTO user) {
        return getReferenceForId(user.getId()).setValue(user);
    }

    /**
     * Create a new user
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void create(final UserDTO user, final TaskCallback callback) {
        callback.onStart();
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        if (task.isSuccessful() && firebaseUser != null) {
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
                                        if (taskBatch.isSuccessful()) {
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
    public void fetch(TaskDataCallback<List<UserDTO>> callback) {

    }

    /**
     * Fetch single user with ID
     *
     * @param userId   User ID
     * @param callback Callback
     */
    @Override
    public void fetch(String userId, UserDTO callback) {

    }

    /**
     * Fetch all users that match
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void fetch(UserDTO user, TaskDataCallback<List<UserDTO>> callback) {

    }

    /**
     * Update a user
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void update(final UserDTO user, final TaskCallback callback) {
        callback.onStart();

        // TODO Clean this up, it was callback hell, now probably worse
        // The challenge is that there is no way to update every profile field at once
        // so the goal was to get everything into one callback for simplicity in the UI

        final TaskBatch taskBatch = new TaskBatch();

        // Get current Firebase user
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        DatabaseReference databaseReference = mDatabase.getReference();
        if (firebaseUser == null) {
            return;
        }

        // Update user name or photo uri, if it changed
        // Create user profile change request (can only update display name and photo uri)
        boolean isNewName = user.getName() != null && !user.getName().equals(firebaseUser
                .getDisplayName());
        boolean isNewPhotoUrl = user.getPhotoUrl() != null &&
                (firebaseUser.getPhotoUrl() == null || !user.getPhotoUrl().equals(firebaseUser
                        .getPhotoUrl().toString()));
        if (isNewName || isNewPhotoUrl) {
            UserProfileChangeRequest.Builder profileChangeBuilder = new UserProfileChangeRequest
                    .Builder();
            if (isNewName) {
                profileChangeBuilder.setDisplayName(user.getName());
            }
            if (isNewPhotoUrl) {
                profileChangeBuilder.setPhotoUri(Uri.parse(user.getPhotoUrl()));
            }
            taskBatch.addTask(firebaseUser.updateProfile(profileChangeBuilder.build()));
        }

        // Update email if it changed
        if (user.getEmail() != null && !user.getEmail().equals(firebaseUser.getEmail())) {
            taskBatch.addTask(firebaseUser.updateEmail(user.getEmail()));
        }

        // Update password if it changed (if it's set, it will be changed)
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            taskBatch.addTask(firebaseUser.updatePassword(user.getPassword()));
        }

        // Batch callback if we ran anything, otherwise just call onComplete
        if (taskBatch.getTaskCount() > 0) {
            final int taskCount = taskBatch.getTaskCount();
            taskBatch.addObserver(new Observer() {
                @Override
                public void update(Observable observable, Object o) {
                    // If anything was updated and it's successful we'll copy to the database as well
                    if (taskBatch.getTaskCount() == taskCount && taskBatch.isSuccessful()) {
                        taskBatch.addTask(copyUserToDatabaseTask(user));
                    } else if (taskBatch.isAllComplete()) {
                        callback.onComplete();
                        if (taskBatch.isSuccessful()) {
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

        List<Task> getCompletedTasks() {
            return completedTasks;
        }

        void addTask(Task<Void> task) {
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

        boolean isSuccessful() {
            for (Task task : completedTasks) {
                if (!task.isSuccessful()) {
                    return false;
                }
            }
            return true;
        }

        boolean isAllComplete() {
            return getCompletedTasks().size() == getTaskCount();
        }

        Exception getException() {
            for (Task task : completedTasks) {
                if (!task.isSuccessful()) {
                    try {
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
