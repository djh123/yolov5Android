package com.example.yolov5application;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Native.init(getAssets());
////
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hekaiming);
//
        Matrix matrix1 = new Matrix();

        float scaleWidth = ((float) 640) / bitmap.getWidth();
        float scaleHeight = ((float) 640) / bitmap.getHeight();
        // 取得想要缩放的matrix参数
        matrix1.postScale(scaleWidth, scaleHeight);

        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix1, false);


//
        Box[] result = Native.detect(bitmap2,0.3,0.7);
//
        Bitmap mutableBitmap = drawBoxRects(bitmap2, result);//

        ImageView view = (ImageView)findViewById(R.id.im);
        view.setImageBitmap(bitmap);

        ImageView viewout = (ImageView)findViewById(R.id.imout);
        viewout.setImageBitmap(mutableBitmap);

    }

    protected Bitmap drawBoxRects(Bitmap mutableBitmap, Box[] results) {
        if (results == null || results.length <= 0) {
            return mutableBitmap;
        }
        Canvas canvas = new Canvas(mutableBitmap);
        final Paint boxPaint = new Paint();
        boxPaint.setAlpha(200);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(4 * mutableBitmap.getWidth() / 800.0f);
        boxPaint.setTextSize(30 * mutableBitmap.getWidth() / 800.0f);
        for (Box box : results) {

            boxPaint.setColor(box.getColor());
            boxPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(box.getLabel() + String.format(Locale.CHINESE, " %.3f", box.getScore()), box.x0 + 3, box.y0 + 40 * mutableBitmap.getWidth() / 1000.0f, boxPaint);
            boxPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(box.getRect(), boxPaint);
        }
        return mutableBitmap;
    }
}
