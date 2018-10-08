package com.lcl.overdrawdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private int cardResId[] = {R.mipmap.test3, R.mipmap.test3,
            R.mipmap.test3, R.mipmap.test3};

    private MultiCardsView multicardsView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        multicardsView = (MultiCardsView)findViewById(R.id.cardView);

        multicardsView.setEnableOverdrawOpt(true);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int cardWidth = width /2;
        int cardHeight = height /2;
        int yOffset = 40;
        int xOffset = 40;
        for (int i = 0; i < cardResId.length; i++) {
            SingleCard cd = new SingleCard(new RectF(xOffset, yOffset, xOffset + cardWidth, yOffset + cardHeight));
            Bitmap bitmap = loadImageResource(cardResId[i], cardWidth, cardHeight);
            cd.setBitmap(bitmap);
            multicardsView.addCards(cd);
            xOffset += cardWidth / 3;
        }
        Button overdraw = (Button) findViewById(R.id.btnOverDraw);
        overdraw.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            multicardsView.setEnableOverdrawOpt(false);
                                        }
                                    }
        );
        Button perfectdraw = (Button) findViewById(R.id.btnPerfectDraw);
        perfectdraw.setOnClickListener(new View.OnClickListener() {

                                           @Override
                                           public void onClick(View v) {
                                               multicardsView.setEnableOverdrawOpt(true);
                                           }
                                       }
        );
    }

    public Bitmap loadImageResource(int imageResId, int cardWidth, int cardHeight) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inDensity = DisplayMetrics.DENSITY_DEFAULT;
            options.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
            options.inScreenDensity = DisplayMetrics.DENSITY_DEFAULT;
            BitmapFactory.decodeResource(getResources(), imageResId, options);

            // ����decode
            options.inJustDecodeBounds = false;
            options.inSampleSize = findBestSampleSize(options.outWidth, options.outHeight, cardWidth, cardHeight);
            bitmap = BitmapFactory.decodeResource(getResources(), imageResId, options);
        } catch (OutOfMemoryError exception) {
            Log.d("img", "loadImageResource : out of memory resid: " + imageResId);
        }
        return bitmap;
    }

    /**
     * Returns the largest power-of-two divisor for use in downscaling a bitmap
     * that will not result in the scaling past the desired dimensions.
     *
     * @param actualWidth   Actual width of the bitmap
     * @param actualHeight  Actual height of the bitmap
     * @param desiredWidth  Desired width of the bitmap
     * @param desiredHeight Desired height of the bitmap
     */
    public static int findBestSampleSize(
            int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }
}
