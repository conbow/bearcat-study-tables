package edu.uc.bearcatstudytables.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.dao.IDataAccess;
import edu.uc.bearcatstudytables.databinding.ActivityForgotPasswordBinding;
import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.ui.util.ValidationUtil;
import edu.uc.bearcatstudytables.ui.viewmodel.AuthViewModel;

public class ForgotPasswordActivity extends BaseActivity {

    private static final String TAG = "ForgotPasswordActivity";

    public static final int REQUEST_CODE = 2;

    private ActivityForgotPasswordBinding mBinding;
    private AuthViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.action_forgot_password);

        // Setup ViewModel for retaining data on configuration changes
        mViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        // Setup data binding and set ViewModel
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        mBinding.setViewModel(mViewModel);
    }

    /**
     * Forgot password button click handler
     * Validates input and sends data to service
     *
     * @param view View
     */
    public void onPasswordResetButtonClick(final View view) {
        final UserDTO inputUser = mViewModel.getUser();

        // Input validation
        View focusView = null;
        // Email
        if (inputUser.getEmail().isEmpty()) {
            mBinding.email.setError(getString(R.string.error_field_required));
            focusView = mBinding.email;
        } else if (!ValidationUtil.isValidEmail(inputUser.getEmail())) {
            mBinding.email.setError(getString(R.string.error_invalid_email));
            focusView = mBinding.email;
        }

        // Check input validation and attempt password reset
        if (focusView != null) {
            focusView.requestFocus();
        } else {

            mUserService.resetPassword(inputUser, new IDataAccess.TaskCallback() {
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
                    setResult(RESULT_OK);
                    finish();
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
