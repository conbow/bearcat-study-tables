package edu.uc.bearcatstudytables.viewmodel.common;

import android.databinding.ObservableBoolean;

import edu.uc.bearcatstudytables.viewmodel.common.BaseViewModel;

/**
 * Created by connorbowman on 10/13/17.
 */

public class TaskViewModel extends BaseViewModel {

    private ObservableBoolean isLoading = new ObservableBoolean(false);

    public ObservableBoolean getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading.set(isLoading);
    }
}
