package edu.uc.bearcatstudytables.ui.util;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

/**
 * Created by connorbowman on 10/4/17.
 */

public class BindingUtil {

    @BindingAdapter("isVisible")
    public static void isVisible(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter(value = {"imageUrl", "imageData", "imagePlaceholder", "imageRenderCircle"},
            requireAll = false)
    public static void bindImage(ImageView imageView, String imageUrl, byte[] imageData,
                                 Drawable imagePlaceholder, boolean imageRenderCircle) {
        RequestBuilder glide = Glide.with(imageView.getContext())
                .asBitmap()
                .load(imageData != null ? imageData : imageUrl)
                .apply(RequestOptions
                        .centerCropTransform()
                        .diskCacheStrategy(DiskCacheStrategy.NONE));
        if (imageRenderCircle) {
            glide.apply(RequestOptions.circleCropTransform());
        }
        if (imageData != null) {
            glide.apply(RequestOptions.signatureOf(new ObjectKey(imageData)));
        }
        glide.apply(RequestOptions.placeholderOf(imagePlaceholder))
                .apply(RequestOptions.errorOf(imagePlaceholder))
                .into(imageView);
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
