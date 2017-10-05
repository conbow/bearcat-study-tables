package edu.uc.bearcatstudytables.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.databinding.ActivityLoginBinding;
import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.util.ValidationUtil;
import edu.uc.bearcatstudytables.viewmodel.SingleTaskViewModel;

/**
 * Created by connorbowman on 10/4/17.
 */

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private ActivityLoginBinding mBinding;
    private SingleTaskViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        mUser = new UserDTO();
        mBinding.setUser(mUser);

        mViewModel = ViewModelProviders.of(this).get(SingleTaskViewModel.class);
        mBinding.setViewModel(mViewModel);
    }

    public void onLoginButtonClick(final View view) {
        String email = mUser.getEmail();
        String password = mUser.getPassword();

        // Input validation
        View focusView = null;
        // Password
        if (password.isEmpty()) {
            mBinding.password.setError(getString(R.string.error_field_required));
            focusView = mBinding.password;
        } else if (!ValidationUtil.isValidPassword(password)) {
            mBinding.password.setError(getString(R.string.error_invalid_password));
            focusView = mBinding.password;
        }
        // Email
        if (email.isEmpty()) {
            mBinding.email.setError(getString(R.string.error_field_required));
            focusView = mBinding.email;
        } else if (!ValidationUtil.isValidEmail(email)) {
            mBinding.email.setError(getString(R.string.error_invalid_email));
            focusView = mBinding.email;
        }

        // Check input validation and attempt login
        if (focusView != null) {
            focusView.requestFocus();
        } else {

            mViewModel.isLoading.set(true);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, CoursesActivity.class));
                                finish();
                            } else {
                                mViewModel.isLoading.set(false);
                                Snackbar.make(view,
                                        getString(R.string.error_incorrect_login), Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
        }
    }

    public void onSignUpButtonClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, SignUpActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SignUpActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            Snackbar.make(findViewById(android.R.id.content),
                    getString(R.string.success_sign_up), Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}
