package com.example.yolov5application;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

public class Native {
    static {
        System.loadLibrary("native");
    }

    public static native void init(AssetManager manager);
    public static native Box[] detect(Bitmap bitmap, double threshold, double nms_threshold);

}
