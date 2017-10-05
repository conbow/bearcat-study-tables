package edu.uc.bearcatstudytables.util;

import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;

/**
 * Created by connorbowman on 10/4/17.
 */

public class BindingUtil {

    @BindingAdapter("app:errorText")
    public static void setErrorText(TextInputLayout view, String errorMessage) {
        view.setError(errorMessage);
    }
}
