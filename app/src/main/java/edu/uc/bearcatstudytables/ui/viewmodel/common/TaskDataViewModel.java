package edu.uc.bearcatstudytables.ui.viewmodel.common;

public class TaskDataViewModel<T> extends TaskViewModel {

    private T mData;

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        this.mData = data;
    }
}
