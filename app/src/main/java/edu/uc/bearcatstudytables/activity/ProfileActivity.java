package edu.uc.bearcatstudytables.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.View;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.dao.IDataAccess;
import edu.uc.bearcatstudytables.databinding.ActivityProfileBinding;
import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.util.ValidationUtil;
import edu.uc.bearcatstudytables.viewmodel.AuthViewModel;

public class ProfileActivity extends BaseActivity {

    private static final String TAG = "ProfileActivity";

    public static final int REQUEST_CODE = 3;
    public static final int REQUEST_IMAGE_CAPTURE = 5;

    private ActivityProfileBinding mBinding;
    private AuthViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.action_profile);

        // Setup ViewModel for retaining data on configuration changes
        mViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        mViewModel.setUser(mUserService.getCurrentUser());

        // Setup data binding and set ViewModel
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        mBinding.setViewModel(mViewModel);

        /*
        mViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        // Bind existing user info to view model, include password in case it changes
        mUser.setPassword(mViewModel.getUser().getPassword());
        mViewModel.setUser(mUser);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        mBinding.setViewModel(mViewModel);*/
    }

    public void onUpdateProfileButtonClick(final View view) {
        final UserDTO inputUser = mViewModel.getUser();

        // Input validation
        View focusView = null;
        // Password
        if (!inputUser.getPassword().isEmpty() && !ValidationUtil.isValidPassword(inputUser
                .getPassword())) {
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

        // Check input validation and attempt profile update
        if (focusView != null) {
            focusView.requestFocus();
        } else {

            mUserService.updateProfile(mViewModel.getUser(), new IDataAccess.TaskCallback() {
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
                    // Show success message
                    Snackbar.make(view, R.string.success_profile_update, Snackbar.LENGTH_LONG)
                            .show();
                }

                @Override
                public void onFailure(Exception e) {
                    // Show error message
                    Snackbar.make(view, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    public void onProfilePhotoChangeButtonClick(View view) {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mBinding.profilePhoto.setImageBitmap(imageBitmap);
        }
    }
}
