package edu.uc.bearcatstudytables.ui.util;

import android.text.TextUtils;

public class ValidationUtil {

    public static final int MIN_PASSWORD_LENGTH = 8;

    public static boolean isValidEmail(CharSequence email) {
        // Via https://stackoverflow.com/a/7882950
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }

    public static boolean isValidUniversityEmail(String email) {
        return email.endsWith("uc.edu") || email.endsWith("mail.uc.edu")
                || email.endsWith("ucmail.uc.edu");
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= MIN_PASSWORD_LENGTH;
    }
}
