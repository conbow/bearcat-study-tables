package edu.uc.bearcatstudytables.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

/**
 * Created by connorbowman on 10/4/17.
 */

public class SingleTaskViewModel extends ViewModel {

    public ObservableBoolean isLoading = new ObservableBoolean(false);
}
