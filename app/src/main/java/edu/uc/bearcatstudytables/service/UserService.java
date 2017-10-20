package edu.uc.bearcatstudytables.service;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.uc.bearcatstudytables.dao.IUserDAO;
import edu.uc.bearcatstudytables.dao.UserDAO;
import edu.uc.bearcatstudytables.dto.UserDTO;

/**
 * Created by connorbowman on 10/3/17.
 */

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
                    setCurrentUser(user);
                    authCallback.onAuthChanged(user);
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
    public void login(UserDTO user, final TaskCallback callback) {
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
    public void signUp(UserDTO user, TaskCallback callback) {
        mUserDAO.create(user, callback);
    }

    /**
     * Reset a user password
     *
     * @param user     User
     * @param callback Callback
     */
    @Override
    public void resetPassword(UserDTO user, final TaskCallback callback) {
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
    public void updateProfile(UserDTO user, final TaskCallback callback) {
        mUserDAO.update(user, callback);
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
