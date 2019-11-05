package com.wan.natives;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = getWindow().getDecorView();
        v.setSystemUiVisibility(View.GONE);
        final ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(imageView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AssetManager assetManager = getAssets();
                    InputStream inputStream = readFileFromAssets(assetManager, "2m.jpg");
                    Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream);
                    inputStream = readFileFromAssets(assetManager, "500k.jpg");
                    Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream);
                    inputStream = readFileFromAssets(assetManager, "50k.jpg");
                    Bitmap bitmap3 = BitmapFactory.decodeStream(inputStream);
                    for (int i = 0; i < 500; i++) {
                        BitmapCache.add("2m" + i, bitmap1);
                        BitmapCache.add("500k" + i, bitmap2);
                        BitmapCache.add("50k" + i, bitmap3);
                    }


//                    for (String path : BitmapCache.getPaths()) {
//                        BitmapCache.remove(path);
//                    }

                    final Bitmap bitmap = BitmapCache.get("2m1");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                    BitmapCache.clear();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static InputStream readFileFromAssets(AssetManager manager, String name) {
        InputStream inputStream = null;
        try {
            inputStream = manager.open(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
