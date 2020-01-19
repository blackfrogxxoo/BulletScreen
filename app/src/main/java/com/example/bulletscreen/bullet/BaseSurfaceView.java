package com.example.bulletscreen.bullet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.concurrent.TimeUnit;

public abstract class BaseSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private final SurfaceHolder surfaceHolder;
    protected final float density;
    protected DrawThread drawThread;

    public BaseSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setZOrderOnTop(true);
        density = getResources().getDisplayMetrics().density;
        surfaceHolder = getHolder();
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(drawThread == null) {
            drawThread = new DrawThread();
        }
        drawThread.running = true;
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(drawThread != null) {
            drawThread.running = false;
            drawThread = null;
        }
    }

    abstract void drawS(Canvas canvas);

    private class DrawThread extends Thread {
        boolean running = true;
        @Override
        public void run() {
            while (running) {
                Canvas canvas = null;
                try {
                    synchronized (surfaceHolder) {
                        canvas = surfaceHolder.lockCanvas();
                        if(canvas != null) {
                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                            drawS(canvas);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
