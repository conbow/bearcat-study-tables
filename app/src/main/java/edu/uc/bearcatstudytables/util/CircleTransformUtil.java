package edu.uc.bearcatstudytables.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

/**
 * Created by connorbowman on 10/5/17.
 */

/**
 * Picasso utility function to crop Bitmap image into a circle
 * Used to make profile photo into a circle
 * Via https://stackoverflow.com/questions/26112150/android-create-circular-image-with-picasso
 */
public class CircleTransformUtil implements Transformation {

    /**
     * Transform the source bitmap into a new bitmap. If you create a new bitmap instance, you must
     * call {@link Bitmap#recycle()} on {@code source}. You may return the original
     * if no transformation is required.
     *
     * @param source Bitmap image
     */
    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    /**
     * Returns a unique key for the transformation, used for caching purposes. If the transformation
     * has parameters (e.g. size, scale factor, etc) then these should be part of the key.
     */
    @Override
    public String key() {
        return "circle";
    }
}
