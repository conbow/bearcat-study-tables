package edu.uc.bearcatstudytables.dao;

/**
 * Created by connorbowman on 10/19/17.
 */

public interface IDataAccess {

    /**
     * Callback for a simple task with no data
     */
    interface TaskCallback {

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
    interface TaskDataCallback<T> {

        void onStart();

        void onComplete();

        void onSuccess(T data);

        void onFailure(Exception e);
    }
}
