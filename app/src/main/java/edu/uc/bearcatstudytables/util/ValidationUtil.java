package edu.uc.bearcatstudytables.util;

import android.text.TextUtils;

/**
 * Created by connorbowman on 10/4/17.
 */

public class ValidationUtil {

    // Via https://stackoverflow.com/a/7882950
    public static boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= 8;
    }
}
