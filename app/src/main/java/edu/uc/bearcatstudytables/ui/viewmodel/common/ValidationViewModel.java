package edu.uc.bearcatstudytables.ui.viewmodel.common;

import android.view.View;
import android.widget.EditText;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.ui.util.ValidationUtil;

public class ValidationViewModel {

    private HashMap<View, String> form = new LinkedHashMap<>();
    private View focusView;

    /**
     * Add a validation to the form
     *
     * @param view       View
     * @param validation Validation string
     */
    public void addValidation(View view, String validation) {
        form.put(view, validation);
    }

    /**
     * Display error to view in the UI
     *
     * @param view  View
     * @param error Error string resource (ex: R.string.app_name)
     */
    private void setError(View view, int error) {
        // If the view is an EditText we do setError, can eventually add other types
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            editText.setError(editText.getContext().getString(error));
        }

        // If focusView is already set we want to keep that, since we want the first error
        // that occurs to have focus
        if (focusView == null) {
            focusView = view;
        }
    }

    /**
     * Check specific view for errors
     *
     * @param view             View
     * @param validationString String with validations to run, split by pipe (ex: "required|password")
     */
    private void checkForError(View view, String validationString) {
        String[] validations = validationString.split("\\|");
        String inputText = "";

        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            inputText = editText.getText().toString();
        }

        // If the field isn't required, and it's empty, we can ignore it
        if (!Arrays.asList(validations).contains("required") && inputText.isEmpty())
            return;

        for (String validation : validations) {

            // Required
            if (validation.equals("required") && inputText.isEmpty()) {
                setError(view, R.string.error_field_required);
                break;
            }

            // Email
            if (validation.equals("email")) {
                if (!ValidationUtil.isValidEmail(inputText)) {
                    setError(view, R.string.error_invalid_email);
                    break;
                } else if (!ValidationUtil.isValidUniversityEmail(inputText)) {
                    setError(view, R.string.error_university_email_required);
                    break;
                }
            }

            // Password
            if (validation.equals("password") &&
                    !ValidationUtil.isValidPassword(inputText)) {
                setError(view, R.string.error_invalid_password);
                break;
            }

            // Matches
            if (validation.startsWith("matches:")) {
                String matchesTag = validation.substring("matches:".length());
                String matchesInputText = "";
                View matchesView = view.getRootView().findViewWithTag(matchesTag);
                if (matchesView != null && matchesView instanceof EditText) {
                    EditText matchesEditText = (EditText) matchesView;
                    if (!inputText.equals(matchesEditText.getText().toString())) {
                        setError(view, R.string.error_fields_must_match);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Check all views in form for errors
     */
    private void checkForErrors() {
        for (View view : form.keySet()) {
            checkForError(view, form.get(view));
        }
    }

    /**
     * Check if form has errors
     *
     * @return boolean
     */
    private boolean hasErrors() {
        return focusView != null;
    }

    /**
     * Check if form is valid and return true, otherwise show errors and return false
     *
     * @return boolean
     */
    public boolean isValid() {
        checkForErrors();

        boolean hasErrors = hasErrors();

        // Request focus if it exists, this means moving the current focus
        if (focusView != null)
            focusView.requestFocus();
        focusView = null;

        return !hasErrors;
    }
}
