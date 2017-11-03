package edu.uc.bearcatstudytables.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.dao.IDataAccess;
import edu.uc.bearcatstudytables.databinding.ActivitySignUpBinding;
import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.ui.util.ValidationUtil;
import edu.uc.bearcatstudytables.ui.viewmodel.AuthViewModel;

public class SignUpActivity extends BaseActivity {

    private static final String TAG = "SignUpActivity";

    private ActivitySignUpBinding mBinding;
    private AuthViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.action_sign_up);

        // Setup ViewModel for retaining data on configuration changes
        mViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        // Setup data binding and set ViewModel
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        mBinding.setViewModel(mViewModel);
    }

    /**
     * Signup button click handler
     * Validates input and sends data to service
     *
     * @param view View
     */
    public void onSignUpButtonClick(final View view) {
        final UserDTO inputUser = mViewModel.getUser();

        // Input validation
        View focusView = null;
        // Password Repeat
        String passwordRepeat = mBinding.passwordRepeat.getText().toString();
        if (passwordRepeat.isEmpty()) {
            mBinding.passwordRepeat.setError(getString(R.string.error_field_required));
            focusView = mBinding.passwordRepeat;
        } else if (!ValidationUtil.isValidPassword(passwordRepeat)) {
            mBinding.passwordRepeat.setError(getString(R.string.error_invalid_password));
            focusView = mBinding.passwordRepeat;
        } else if (!passwordRepeat.equals(inputUser.getPassword())) {
            mBinding.password.setError(getString(R.string.error_passwords_must_match));
            mBinding.passwordRepeat.setError(getString(R.string.error_passwords_must_match));
            focusView = mBinding.password;
        }
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
        // Name
        if (inputUser.getName().isEmpty()) {
            mBinding.name.setError(getString(R.string.error_field_required));
            focusView = mBinding.name;
        }

        // Check input validation and attempt sign up
        if (focusView != null) {
            focusView.requestFocus();
        } else {

            mUserService.signUp(inputUser, new IDataAccess.TaskCallback() {
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
                    // We don't need to do anything, signup will automatically log the user in
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
}
