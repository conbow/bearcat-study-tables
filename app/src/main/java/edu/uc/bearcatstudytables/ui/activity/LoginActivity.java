package edu.uc.bearcatstudytables.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.dao.IDataAccess;
import edu.uc.bearcatstudytables.databinding.ActivityLoginBinding;
import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.ui.util.ValidationUtil;
import edu.uc.bearcatstudytables.ui.viewmodel.AuthViewModel;

/**
 * Created by connorbowman on 10/4/17.
 */

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private ActivityLoginBinding mBinding;
    private AuthViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup ViewModel for retaining data on configuration changes
        mViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        // Setup data binding and set ViewModel
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mBinding.setViewModel(mViewModel);
    }

    /**
     * Login button click handler
     * Validates input and sends data to service
     *
     * @param view View
     */
    public void onLoginButtonClick(final View view) {
        final UserDTO inputUser = mViewModel.getUser();

        // Input validation
        View focusView = null;
        // Password
        if (inputUser.getPassword().isEmpty()) {
            mBinding.password.setError(getString(R.string.error_field_required));
            focusView = mBinding.password;
        } else if (!ValidationUtil.isValidPassword(inputUser.getPassword())) {
            mBinding.password.setError(getString(R.string.error_invalid_password));
            focusView = mBinding.password;
        }
        // Email
        if (inputUser.getEmail().isEmpty()) {
            mBinding.email.setError(getString(R.string.error_field_required));
            focusView = mBinding.email;
        } else if (!ValidationUtil.isValidEmail(inputUser.getEmail())) {
            mBinding.email.setError(getString(R.string.error_invalid_email));
            focusView = mBinding.email;
        }

        // Check input validation and attempt login
        if (focusView != null) {
            focusView.requestFocus();
        } else {

            mUserService.login(inputUser, new IDataAccess.TaskCallback() {
                @Override
                public void onStart() {
                    mViewModel.setIsLoading(true);
                }

                @Override
                public void onComplete() {
                    mViewModel.setIsLoading(false);
                }

                @Override
                public void onSuccess() {
                    // We don't need to do anything, login will automatically log the user in
                    // and then our auth listener will redirect the user to new activity
                }

                @Override
                public void onFailure(Exception e) {
                    // Show error message
                    Snackbar.make(view, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    public void onSignUpButtonClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void onForgotPasswordClick() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivityForResult(intent, ForgotPasswordActivity.REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_forgot_password) {
            onForgotPasswordClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ForgotPasswordActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            Snackbar.make(findViewById(R.id.main_container), R.string.success_reset_password,
                    Snackbar.LENGTH_LONG).show();
        }
    }
}
