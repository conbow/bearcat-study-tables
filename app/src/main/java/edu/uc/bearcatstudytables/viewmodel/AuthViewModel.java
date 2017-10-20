package edu.uc.bearcatstudytables.viewmodel;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableField;

import com.android.databinding.library.baseAdapters.BR;

import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.viewmodel.common.TaskViewModel;

/**
 * Created by connorbowman on 10/5/17.
 */

public class AuthViewModel extends TaskViewModel implements Observable {

    private ObservableField<UserDTO> userObservable;

    public AuthViewModel() {
        this(null);
    }

    public AuthViewModel(ObservableField userObservable) {
        // You can optionally bind the user observable to an already existing observable
        if (userObservable != null) {
            this.userObservable = userObservable;
        } else {
            this.userObservable = new ObservableField<>();
        }

        // Set empty User DTO if null
        if (this.userObservable.get() == null) {
            //this.userObservable.set(new UserDTO());
        }
    }

    @Bindable
    public UserDTO getUser() {
        if (userObservable.get() == null) userObservable.set(new UserDTO());
        return userObservable.get();
    }

    public void setUser(UserDTO user) {
        userObservable.set(user);
        notifyPropertyChanged(BR.user);
    }

    public ObservableField<UserDTO> getUserObservable() {
        notifyPropertyChanged(BR.user);
        return userObservable;
    }

    public void setUserObservable(ObservableField<UserDTO> userObservable) {
        notifyPropertyChanged(BR.user);
        this.userObservable = userObservable;
    }
}
