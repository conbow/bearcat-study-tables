package edu.uc.bearcatstudytables.dao;

public class DataAccess {

    /**
     * Callback for a simple task with no data
     */
    public interface TaskCallback {

        void onStart();

        void onComplete();

        void onSuccess();

        void onFailure(Exception e);
    }

    /**
     * Callback for a more complex task that will return data
     *
     * @param <T> Data type to be returned
     */
    public interface TaskDataCallback<T> extends TaskCallback {

        void onSuccess(T data);
    }
}
