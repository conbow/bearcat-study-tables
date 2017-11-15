package edu.uc.bearcatstudytables.ui.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableField;

import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.ui.viewmodel.common.TaskViewModel;

public class AuthViewModel extends TaskViewModel {

    private ObservableField<UserDTO> user = new ObservableField<>();

    @Bindable
    public ObservableField<UserDTO> currentUser = new ObservableField<>();

    public AuthViewModel() {
        setUser(new UserDTO());
    }

    @Bindable
    public UserDTO getUser() {
        return user.get();
    }

    public void setUser(UserDTO user) {
        this.user.set(user);
    }
}
