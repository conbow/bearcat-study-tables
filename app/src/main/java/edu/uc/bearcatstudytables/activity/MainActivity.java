package edu.uc.bearcatstudytables.activity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Upon app opening we direct the user to either the login screen or the course list screen,
        // depending on if the user is authenticated already or not
        Class intentClass;
        if (mCurrentUser != null) {
            intentClass = UserActivity.class;
        } else {
            intentClass = LoginActivity.class;
        }
        startActivity(new Intent(this, intentClass));
        finish();
    }
}
