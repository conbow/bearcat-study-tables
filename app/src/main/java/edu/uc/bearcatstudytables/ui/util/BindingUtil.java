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

import edu.uc.bearcatstudytables.ui.viewmodel.common.ValidationViewModel;

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
        if (imageRenderCircle)
            glide.apply(RequestOptions.circleCropTransform());
        if (imageData != null)
            glide.apply(RequestOptions.signatureOf(new ObjectKey(imageData)));
        glide.apply(RequestOptions.placeholderOf(imagePlaceholder))
                .apply(RequestOptions.errorOf(imagePlaceholder))
                .into(imageView);
    }

    @BindingAdapter(value = {"validate", "validator"})
    public static void bindValidate(View view, String validation,
                                    ValidationViewModel validationViewModel
    ) {
        validationViewModel.addValidation(view, validation);
    }
}
