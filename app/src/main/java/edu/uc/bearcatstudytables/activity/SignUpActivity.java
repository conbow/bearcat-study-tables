package edu.uc.bearcatstudytables.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.databinding.ActivitySignUpBinding;
import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.util.ValidationUtil;
import edu.uc.bearcatstudytables.viewmodel.SingleTaskViewModel;

public class SignUpActivity extends BaseActivity {

    public static final int REQUEST_CODE = 1;
    private static final String TAG = "SignUpActivity";

    private ActivitySignUpBinding mBinding;
    private SingleTaskViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.action_sign_up));
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        mUser = new UserDTO();
        mBinding.setUser(mUser);

        mViewModel = ViewModelProviders.of(this).get(SingleTaskViewModel.class);
        mBinding.setViewModel(mViewModel);
    }

    public void onSignUpButtonClick(final View view) {
        final String name =  mUser.getName();
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
        // Name
        if (name.isEmpty()) {
            mBinding.name.setError(getString(R.string.error_field_required));
            focusView = mBinding.name;
        }

        // Check input validation and attempt sign up
        if (focusView != null) {
            focusView.requestFocus();
        } else {

            mViewModel.isLoading.set(true);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name).build();
                                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                mViewModel.isLoading.set(false);
                                Snackbar.make(view,
                                        getString(R.string.error_sign_up), Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
        }
    }
}
