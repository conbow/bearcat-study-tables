package edu.uc.bearcatstudytables.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.service.ChatService;
import edu.uc.bearcatstudytables.service.IChatService;
import edu.uc.bearcatstudytables.service.IUserService;
import edu.uc.bearcatstudytables.service.UserService;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements IUserService.AuthCallback {

    protected IUserService mUserService;
    protected IChatService mChatService;
    protected ObservableField<UserDTO> mCurrentUser = new ObservableField<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set service instances
        mUserService = new UserService(this);
        mChatService = new ChatService();

        // Set observable for current user
        mCurrentUser.set(mUserService.getCurrentUser());

        // Set toolbar back button arrow if not a root activity
        // OR if not LoginActivity (workaround for older APIs that have issues with isTaskRoot)
        ActionBar actionBar = getSupportActionBar();
        if ((!isTaskRoot() && actionBar != null) && !(BaseActivity.this instanceof LoginActivity)) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Fix for vectors on older APIs
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        mUserService.start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle actionbar/navbar back button press
        onBackPressed();
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        mUserService.stop();
    }

    /**
     * Called when authentication or user data is changed (ex: user logs out or changes email)
     *
     * @param user New/changed user model
     */
    @Override
    public void onAuthChanged(UserDTO user) {
        // Make sure user is on a screen where they should be logged in or logged out,
        // otherwise redirect them accordingly
        boolean isALoginActivity = (BaseActivity.this instanceof LoginActivity)
                || (BaseActivity.this instanceof SignUpActivity)
                || (BaseActivity.this instanceof ForgotPasswordActivity);
        if ((isALoginActivity && user != null) ||
                (!isALoginActivity && user == null)) {
            Intent intent = new Intent(BaseActivity.this, MainActivity.class);
            startActivity(intent);
            ActivityCompat.finishAffinity(BaseActivity.this);
        }

        // Set current user observable (UserDTO will handle notifying observers)
        mCurrentUser.set(user);
    }
}
