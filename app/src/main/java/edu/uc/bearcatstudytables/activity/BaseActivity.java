package edu.uc.bearcatstudytables.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.uc.bearcatstudytables.dto.UserDTO;

/**
 * Created by connorbowman on 10/4/17.
 */

public class BaseActivity extends AppCompatActivity {

    protected UserDTO mUser;
    protected FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            mUser = new UserDTO();
            mUser.setId(firebaseUser.getUid());
            mUser.setEmail(firebaseUser.getEmail());
            mUser.setName(firebaseUser.getDisplayName());
        }

        // Set toolbar back button arrow if not root activity
        ActionBar actionBar = getSupportActionBar();
        if (!isTaskRoot() && actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
