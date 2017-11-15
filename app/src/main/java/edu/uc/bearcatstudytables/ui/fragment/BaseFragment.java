package edu.uc.bearcatstudytables.ui.fragment;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.service.IUserService;
import edu.uc.bearcatstudytables.service.UserService;

public class BaseFragment extends Fragment {

    protected IUserService mUserService;
    protected ObservableField<UserDTO> mCurrentUser = new ObservableField<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set service instances
        mUserService = new UserService();

        // Set observable for current user
        mCurrentUser.set(mUserService.getCurrentUser());
    }
}
