package com.peter.carloading.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.peter.carloading.R;

/**
 * Created by peter on 2019/4/4.
 * explain:
 */

public class CarLoadingView extends View {

    private static final String TAG = "CarLoadingView";

    private int viewWidth;
    private int viewHeight;

    private float zoom_ratio;

    private int body_zoom_w;
    private int body_zoom_h;
    private int wheel_zoom_w_h;

    private int body_x;
    private int body_y;
    private int wheel_front_x;
    private int wheel_front_y;
    private int wheel_behind_x;
    private int wheel_behind_y;

    private BitmapFactory.Options car_body_options;
    private BitmapFactory.Options car_wheel_front_options;
    private BitmapFactory.Options car_wheel_behind_options;

    private Bitmap car_body_bitmap;
    private Bitmap car_wheel_front_bitmap;
    private Bitmap car_wheel_behind_bitmap;
    private Bitmap car_wheel_front_degrees_bitmap;
    private Bitmap car_wheel_behind_degrees_bitmap;

    private Paint car_body_paint;
    private Paint car_wheel_front_paint;
    private Paint car_wheel_behind_paint;

    public CarLoadingView(Context context) {
        super(context);

        initData();
        initView();
    }

    public CarLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initData();
        initView();
    }

    public CarLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initData();
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CarLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initData();
        initView();
    }

    private void initData() {
        car_body_options = new BitmapFactory.Options();
        car_wheel_front_options = new BitmapFactory.Options();
        car_wheel_behind_options = new BitmapFactory.Options();

        car_body_bitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.mipmap.img_car_body), null, car_body_options);
        car_wheel_front_bitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.mipmap.img_car_wheel), null, car_wheel_front_options);
        car_wheel_behind_bitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.mipmap.img_car_wheel), null, car_wheel_behind_options);

        car_wheel_front_degrees_bitmap = car_wheel_front_bitmap;
        car_wheel_behind_degrees_bitmap = car_wheel_behind_bitmap;

        car_body_paint = new Paint();
        car_body_paint.setAntiAlias(true);
        car_wheel_front_paint = new Paint();
        car_wheel_front_paint.setAntiAlias(true);
        car_wheel_behind_paint = new Paint();
        car_wheel_behind_paint.setAntiAlias(true);
    }

    private void initView() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.i(TAG, "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();

        // 计算缩放后的 车身宽高 & 轮子的宽高

        // 计算图片缩放比
        zoom_ratio = viewWidth / 2 / ((float) car_body_options.outWidth);

        // 计算图片要显示宽高
        body_zoom_w = viewWidth / 2;
        body_zoom_h = (int) (zoom_ratio * car_body_options.outHeight);
        wheel_zoom_w_h = (int) (zoom_ratio * car_wheel_front_options.outWidth);

        // 计算车子部件的左上角坐标
        body_x = (viewWidth - body_zoom_w) / 2;
        body_y = (viewHeight - body_zoom_h) / 2;
        wheel_front_x = body_x + (int) (body_zoom_w * 0.726);
        wheel_front_y = body_y + (int) (body_zoom_h * 0.56);
        wheel_behind_x = body_x + (int) (body_zoom_w * 0.112);
        wheel_behind_y = body_y + (int) (body_zoom_h * 0.56);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.i(TAG, "onDraw");
        if (myThread == null) {
            myThread = new MyThread();
            myThread.start();
        }

        // 绘制车身
        drawImage(canvas, car_body_bitmap, body_x, body_y, body_zoom_w, body_zoom_h);

        // 绘制前轮
        drawImageWheel(canvas, car_wheel_front_degrees_bitmap, wheel_front_x, wheel_front_y, wheel_zoom_w_h, wheel_zoom_w_h);

        // 绘制后轮
        drawImageWheel(canvas, car_wheel_behind_degrees_bitmap, wheel_behind_x, wheel_behind_y, wheel_zoom_w_h, wheel_zoom_w_h);
    }

    private RectF imageDst = new RectF();// 屏幕 >>目标矩形

    /**
     * 绘制图片
     *
     * @param canvas
     * @param bitmap
     * @param x      x屏幕上的x坐标
     * @param y      y屏幕上的y坐标
     * @param w      w要绘制的图片的宽度
     * @param h      h要绘制的图片的高度
     */
    public void drawImage(Canvas canvas, Bitmap bitmap, int x, int y, int w, int h) {
        imageDst.left = x;
        imageDst.top = y;
        imageDst.right = x + w;
        imageDst.bottom = y + h;

        canvas.drawBitmap(bitmap, null, imageDst, null);
    }

    /**
     * 绘制图片
     *offsetMax
     * @param canvas
     * @param bitmap
     * @param x      x屏幕上的x坐标
     * @param y      y屏幕上的y坐标
     * @param w      w要绘制的图片的宽度
     * @param h      h要绘制的图片的高度
     */
    public void drawImageWheel(Canvas canvas, Bitmap bitmap, int x, int y, int w, int h) {
        imageDst.left = x;
        imageDst.top = y + offsetY;
        imageDst.right = x + w;
        imageDst.bottom = y + h + offsetY;

        canvas.drawBitmap(bitmap, null, imageDst, null);
    }

    private float degrees = 0; //秒针每秒偏移的角度
    private float degreesUnit = 2f;
    private float degreesMax = 360.0f;

    private float offsetY = 0.0f;   // Y轴偏移值
    private float offsetUnit = 0.2f;
    private float offsetMin = -3.0f;
    private float offsetMax = 3.0f;

    private boolean offsetOk = false;

    private MyThread myThread;

    private class MyThread extends Thread {

        @Override
        public void run() {
            while (true) {
                if (offsetOk) {
                    offsetY += offsetUnit;
                    offsetOk = !(offsetY > 0 && offsetY >= offsetMax);
                } else {
                    offsetY -= offsetUnit;
                    offsetOk = offsetY < 0 && offsetY <= offsetMin;
                }

                Log.i(TAG, offsetY + "   || ");
                if (degrees >= degreesMax) {
                    degrees = 0;
                }

                degrees += degreesUnit;

                car_wheel_front_degrees_bitmap = rotate(car_wheel_front_bitmap, degrees);
                car_wheel_behind_degrees_bitmap = rotate(car_wheel_behind_bitmap, degrees);

                postInvalidate();//重新绘制

                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap rotateBitmap;
    private Canvas canvas;
    private Matrix matrix = new Matrix();

    private Bitmap rotate(Bitmap bitmap, float degrees) {
        if (rotateBitmap == null) {
            rotateBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        }
        if (canvas == null) {
            canvas = new Canvas(rotateBitmap);
        }
        if (matrix == null) {
            matrix = new Matrix();
        }

        matrix.reset();
        matrix.postRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

        canvas.drawBitmap(bitmap, matrix, new Paint());

        return rotateBitmap;
    }
}