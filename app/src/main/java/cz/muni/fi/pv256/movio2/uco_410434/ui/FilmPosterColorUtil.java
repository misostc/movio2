package cz.muni.fi.pv256.movio2.uco_410434.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.view.View;

public class FilmPosterColorUtil {
    public static void setBackgroundColor(Context context, Drawable drawable, final View view) {
        Bitmap myBitmap = ((BitmapDrawable) drawable).getBitmap();
        if (myBitmap != null && !myBitmap.isRecycled()) {
            Palette.PaletteAsyncListener listener = new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    int transparent = (palette.getDarkMutedColor(0x0) & (0x00ffffff)) | 0xEE000000;
                    view.setBackgroundColor(transparent);
                }
            };
            Palette.from(myBitmap).generate(listener);
        }
    }

}
