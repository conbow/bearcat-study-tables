package edu.uc.bearcatstudytables.util;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * Created by connorbowman on 10/4/17.
 */

public class BindingUtil {

    @BindingAdapter("isVisible")
    public static void isVisible(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /*
    @BindingAdapter("bind:validation")
    public static void setValidation(final TextInputLayout view, final String validationString) {
        view.setErrorEnabled(true);
        view.setError("test");
        view.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View aview, boolean hasFocus) {
                if (!hasFocus) {
                    String[] validations = validationString.split("|");
                    String text = view.getEditText().getText().toString();
                    for (String validation : validations) {
                        if (validation.equals("required")) {
                            view.getEditText().setError(view.getContext().getString(R.string.error_field_required));
                        }
                    }
                }
            }
        });
    }

    @BindingAdapter("validate")
    public static void validate(final TextInputLayout textInputLayout, String validationTypes) {
        final String[] validators = validationTypes.split("\\|");
        final TextInputEditText editText = (TextInputEditText) textInputLayout.getEditText();
        if (editText != null) {
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    textInputLayout.setError(null);
                    String inputText = editText.getText().toString();
                    if (!hasFocus) {
                        for (String validation : validators) {
                            if (validation.equals("required") && inputText.isEmpty()) {
                                textInputLayout.setError(view.getContext().getString(R.string.error_field_required));
                            } else if (validation.equals("email") && !ValidationUtil.isValidEmail(inputText)) {
                                textInputLayout.setError(view.getContext().getString(R.string.error_invalid_email));
                            } else if (validation.equals("password") && !ValidationUtil.isValidPassword(inputText)) {
                                textInputLayout.setError(view.getContext().getString(R.string.error_invalid_password));
                            }
                        }
                    }
                }
            });
        }
    }*/
}
