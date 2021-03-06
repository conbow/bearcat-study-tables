package edu.uc.bearcatstudytables.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.dao.DataAccess;
import edu.uc.bearcatstudytables.databinding.ActivityForgotPasswordBinding;
import edu.uc.bearcatstudytables.ui.viewmodel.AuthViewModel;

public class ForgotPasswordActivity extends BaseActivity {

    public static final int REQUEST_CODE = 2;

    private AuthViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.forgot_password);

        // Setup ViewModel for retaining data on configuration changes
        mViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        // Setup data binding and set ViewModel
        ActivityForgotPasswordBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_forgot_password);
        binding.setViewModel(mViewModel);
    }

    /**
     * Forgot password button click handler
     * Validates input and sends data to service
     *
     * @param view View
     */
    public void onPasswordResetButtonClick(final View view) {
        if (mViewModel.getValidation().isValid()) {
            mUserService.resetPassword(mViewModel.getUser(), new DataAccess.TaskCallback() {
                @Override
                public void onStart() {
                    mViewModel.setIsLoading(true);
                }

                @Override
                public void onComplete() {
                    // We don't hide progress unless the task fails, because we don't want the user
                    // to be able to click again in the split second when switching activities
                }

                @Override
                public void onSuccess() {
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    mViewModel.setIsLoading(false);

                    // Show error message
                    Snackbar.make(view, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }
}
