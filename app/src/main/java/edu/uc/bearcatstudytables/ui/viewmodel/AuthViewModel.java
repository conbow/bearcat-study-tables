package edu.uc.bearcatstudytables.ui.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableField;

import edu.uc.bearcatstudytables.BR;
import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.ui.viewmodel.common.TaskViewModel;

/**
 * Created by connorbowman on 10/5/17.
 */

public class AuthViewModel extends TaskViewModel {

    @Bindable
    public ObservableField<UserDTO> currentUser = new ObservableField<>();
    private ObservableField<UserDTO> user = new ObservableField<>();


    @Bindable
    public UserDTO getUser() {
        if (user.get() == null) setUser(new UserDTO());
        return user.get();
    }

    public void setUser(UserDTO user) {
        this.user.set(user);
        notifyPropertyChanged(BR.user);
    }

    public ObservableField<UserDTO> getUserObservable() {
        return user;
    }

    public void setUserObservable(ObservableField<UserDTO> user) {
        this.user = user;
        notifyPropertyChanged(BR.user);
    }

    /*

    @Bindable
    public UserDTO getCurrentUser() {
        return currentUser.get();
    }

    public void setCurrentUser(UserDTO user) {
        this.currentUser.set(user);
        notifyPropertyChanged(BR.currentUser);
    }

    public ObservableField<UserDTO> getCurrentUserObservable() {
        return currentUser;
    }

    public void setCurrentUserObservable(ObservableField<UserDTO> currentUser) {
        this.currentUser = currentUser;
        notifyPropertyChanged(BR.currentUser);
    }*/
}
