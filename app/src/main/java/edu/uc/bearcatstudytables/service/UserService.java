package edu.uc.bearcatstudytables.service;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import edu.uc.bearcatstudytables.dao.DataAccess;
import edu.uc.bearcatstudytables.dao.IUserDAO;
import edu.uc.bearcatstudytables.dao.UserDAO;
import edu.uc.bearcatstudytables.dto.UserDTO;

public class UserService implements IUserService {

    private UserDTO mCurrentUser;
    private IUserDAO mUserDAO;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public UserService() {
        // Sometimes we don't need to set AuthCallback, such as in Fragments where the parent
        // activity will already have an auth listener
        this(null);
    }

    public UserService(final AuthCallback authCallback) {
        // Create UserDAO instance
        mUserDAO = new UserDAO();

        // Get current Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // Set current user
        setCurrentUser(UserDAO.bindToUser(mAuth.getCurrentUser()));

        // Check for AuthCallback as we don't always care about changes, in fragments for example
        if (authCallback != null) {
            // Add listener to monitor changes to Firebase Auth (ex: user logs out or changes email)
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    UserDTO user = UserDAO.bindToUser(firebaseAuth.getCurrentUser());
                    // Check for user name. Fix for problem where user is created and logged in
                    // before name is actually saved
                    if (user != null && user.getName() != null && !user.getName().isEmpty()) {
                        setCurrentUser(user);
                        authCallback.onAuthChanged(user);
                    } else {
                        setCurrentUser(null);
                        authCallback.onAuthChanged(null);
                    }
                }
            };
        }
    }

    /**
     * Set current user
     *
     * @param user User
     */
    private void setCurrentUser(UserDTO user) {
        this.mCurrentUser = user;
    }

    /**
     * Start Service
     * Creates and starts auth listeners
     * Should be called in Activities onStart() method
     */
    @Override
    public void start() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Login a user
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void login(UserDTO user, final DataAccess.TaskCallback callback) {
        callback.onStart();
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
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
     * Logout current user
     */
    @Override
    public void logout() {
        mAuth.signOut();
    }

    /**
     * Check if a user is authenticated
     *
     * @return boolean auth status
     */
    @Override
    public boolean isLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    /**
     * Returns current user
     *
     * @return Current user
     */
    @Override
    public UserDTO getCurrentUser() {
        return mCurrentUser;
    }

    /**
     * Create a user account
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void signUp(UserDTO user, DataAccess.TaskCallback callback) {
        mUserDAO.create(user, callback);
    }

    /**
     * Fetch all users
     *
     * @param callback Callback
     */
    @Override
    public void fetchAll(DataAccess.TaskDataCallback<List<UserDTO>> callback) {
        mUserDAO.fetchAll(callback);
    }

    /**
     * Reset a user password
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void resetPassword(UserDTO user, final DataAccess.TaskCallback callback) {
        callback.onStart();
        mAuth.sendPasswordResetEmail(user.getEmail())
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
     * Update current users profile
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void updateProfile(UserDTO user, DataAccess.TaskCallback callback) {
        mUserDAO.update(user, callback);
        /*
        callback.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mUserDAO.update(user, callback);
            }
        }, 10000);*/
    }

    /**
     * Reload Service
     * Reloads auth state
     */
    @Override
    public void reload() {
        mAuthListener.onAuthStateChanged(mAuth);
    }

    /**
     * Stop Service
     * Clears and removes auth listener
     * Should be called in Activities onStop() method
     */
    @Override
    public void stop() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
