package edu.uc.bearcatstudytables.ui.viewmodel.common;

import android.databinding.ObservableBoolean;

public class TaskViewModel extends BaseViewModel {

    private ObservableBoolean isLoading = new ObservableBoolean(false);

    public ObservableBoolean getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading.set(isLoading);
    }
}
