package edu.uc.bearcatstudytables.ui.viewmodel.common;

/**
 * Created by connorbowman on 10/13/17.
 */

public class TaskDataViewModel<T> extends TaskViewModel {

    private T mData;

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        this.mData = data;
    }
}
