package edu.uc.bearcatstudytables.ui.viewmodel.common;

/**
 * Created by connorbowman on 10/11/17.
 */

import android.arch.lifecycle.ViewModel;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import edu.uc.bearcatstudytables.BR;

/**
 * Combine Android Data Binding Observables with ViewModel library
 * This is temporary until the Architecture library is updated to work with Android Data Binding
 * See: https://stackoverflow.com/questions/44442251/viewmodel-and-data-binding
 */
public class BaseViewModel extends ViewModel implements Observable {

    private final PropertyChangeRegistry mPropertyChangeRegistry = new PropertyChangeRegistry();

    public void notifyChange() {
        mPropertyChangeRegistry.notifyChange(this, BR._all);
    }

    public void notifyPropertyChanged(int fieldId) {
        mPropertyChangeRegistry.notifyChange(this, fieldId);
    }

    /**
     * Adds a callback to listen for changes to the Observable.
     *
     * @param callback The callback to start listening.
     */
    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mPropertyChangeRegistry.add(callback);
    }

    /**
     * Removes a callback from those listening for changes.
     *
     * @param callback The callback that should stop listening.
     */
    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mPropertyChangeRegistry.remove(callback);
    }
}
