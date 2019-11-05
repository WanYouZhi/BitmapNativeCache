package com.wan.natives;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * author: 万友志
 * created on: 2019/10/28 13:51
 * description:
 */
public final class BitmapUtil {
    private static HashMap<String, byte[]> hashMap = new HashMap<>();
    private static ArrayList<byte[]> bytesd = new ArrayList<>();

    private BitmapUtil() {
        throw new AssertionError();
    }

    public static BitmapFactory.Options getScaleOptions(
            String picturePath, int viewWidth, int viewHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);
        int inSampleSize = 1;
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;
        if (viewHeight <= 0) {
            viewHeight = getScreenHeight();
        }
        if (viewWidth <= 0) {
            viewWidth = getScreenWidth();
        }
        //假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
        if (bitmapWidth > viewWidth || bitmapHeight > viewHeight) {
            int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);
            int heightScale = Math.round((float) bitmapHeight / (float) viewWidth);

            //为了保证图片不缩放变形，我们取宽高比例最小的那个
            inSampleSize = widthScale < heightScale ? widthScale : heightScale;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return options;
    }

    public static Bitmap decodeBitmapFromPath(String path, int reqWidth, int reqHeight) {
        // 首先不加载图片,仅获取图片尺寸
        final BitmapFactory.Options options = getScaleOptions(path, reqWidth, reqHeight);
        // 利用计算的比例值获取大小变小的图片对象
        return BitmapFactory.decodeFile(path, options);
    }

    public static int getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    private static DisplayMetrics metrics;

    public static DisplayMetrics getDisplayMetrics() {
        //即使第一次有两个同时new 也没多大问题
        if (metrics == null) {
            metrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) BaseApplication.getApplication().getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getRealMetrics(metrics);
        }
        return metrics;
    }
}
