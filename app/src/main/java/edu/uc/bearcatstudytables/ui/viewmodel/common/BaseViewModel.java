package edu.uc.bearcatstudytables.ui.viewmodel.common;

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

    private ValidationViewModel mValidation;

    public BaseViewModel() {
        mValidation = new ValidationViewModel();
    }

    public ValidationViewModel getValidation() {
        return mValidation;
    }

    public void setValidation(ValidationViewModel validation) {
        this.mValidation = validation;
    }

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
