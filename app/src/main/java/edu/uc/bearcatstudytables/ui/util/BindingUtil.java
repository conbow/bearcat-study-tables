package edu.uc.bearcatstudytables.ui.util;

import android.annotation.SuppressLint;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;

import edu.uc.bearcatstudytables.ui.viewmodel.common.ValidationViewModel;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class BindingUtil {

    @BindingAdapter("isVisible")
    public static void isVisible(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter(value = {"imageUrl", "imageData", "imagePlaceholder", "imageRenderCircle",
            "imageNoCache"}, requireAll = false)
    public static void bindImage(ImageView imageView, String imageUrl, byte[] imageData,
                                 Drawable imagePlaceholder, boolean imageRenderCircle,
                                 boolean imageNoCache) {
        @SuppressLint("Range")
        RequestBuilder glide = Glide.with(imageView.getContext())
                .load(imageData != null ? imageData : imageUrl)
                .transition(withCrossFade())
                .apply(RequestOptions.overrideOf(Target.SIZE_ORIGINAL));

        if (imageRenderCircle)
            glide.apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.circleCropTransform());
        if (imageNoCache)
            glide.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
        if (imageData != null)
            glide.apply(RequestOptions.signatureOf(new ObjectKey(imageData)));
        if (imagePlaceholder != null)
            glide.apply(RequestOptions.placeholderOf(imagePlaceholder))
                    .apply(RequestOptions.errorOf(imagePlaceholder));

        glide.into(imageView);
    }

    @BindingAdapter(value = {"validate", "validator"})
    public static void bindValidate(View view, String validation,
                                    ValidationViewModel validationViewModel
    ) {
        validationViewModel.addValidation(view, validation);
    }
}
