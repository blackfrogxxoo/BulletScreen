package com.example.bulletscreen.bullet;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 带头像（bitmap -> url)
 * 可播放声音（player -> url）
 * 可能需要绘制内部动效（画线条）
 */
public class VoiceBullet extends Bullet {
    public static final int VOICE_IN_DURATION = 200;
    private static final int DP_WIDTH = 134;
    private static final int DP_HEIGHT = 30;
    private static final int ONCE_DRAW_TIMES = 20;
    private static final float DP_LINE_WIDTH = 1f;
    private static final float DP_LINE_DIVIDER = 2f;
    private static final float DP_AVATAR_PADDING = 2f;
    final String bitmapUrl;
    final Matrix matrix = new Matrix();
    String voiceUrl;
    Bitmap bitmap;
    boolean loadingBitmap;
    private int drawTimes = new Random().nextInt(ONCE_DRAW_TIMES);
    private List<FtLine> ftLines = new ArrayList<>();
    boolean playing;

    VoiceBullet(BulletScreenView parent, String id, int videoPosition, String bitmapUrl, int duration) {
        super(parent, id, videoPosition);
        this.bitmapUrl = bitmapUrl;
        this.duration = duration;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    private float calculateWidth() {
        return DP_WIDTH * density;
    }


    @Override
    void draw(Canvas canvas, Paint paint) {
        float radius = DP_HEIGHT * density;
        rectF.left = point.x;
        rectF.top = point.y;
        rectF.right = rectF.left + calculateWidth();
        rectF.bottom = rectF.top + DP_HEIGHT * density;
        paint.setColor(Color.parseColor("#80000000"));
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setColor(Color.WHITE);
        if (bitmap != null) {
            float size = rectF.bottom - rectF.top - 2 * DP_AVATAR_PADDING * density;
            matrix.setScale(size / bitmap.getWidth(), size / bitmap.getHeight());
            matrix.postTranslate(point.x + DP_AVATAR_PADDING * density, point.y + DP_AVATAR_PADDING * density);
            canvas.drawBitmap(bitmap, matrix, paint);
        }
        if(!parent.isPaused()) {
            drawTimes ++; // 用drawTimes来模拟Animator
            if(drawTimes > ONCE_DRAW_TIMES) {
                drawTimes = 0;
            }
            float x = 0;
            if(ftLines.size() == 0) {
                for(int i = 0; i < sFtLines.length && x < rectF.width() - 1.2 * rectF.height(); i ++) {
                    x = i * density * (DP_LINE_WIDTH + DP_LINE_DIVIDER) + 0.5f * density * DP_LINE_WIDTH;
                    float y = rectF.height() / 2;
                    Point point = new Point();
                    point.x = (int) x;
                    point.y = (int) y;
                    float[] o = sFtLines[i];
                    FtLine ftLine = new FtLine(point, 0.7f * rectF.height() * o[0], 0.7f * rectF.height() * o[1], o[2]);
                    ftLines.add(ftLine);
                }
            }
        }
        paint.setStrokeWidth(density * DP_LINE_WIDTH);
        paint.setStrokeCap(Paint.Cap.ROUND);
        for(FtLine ftLine : ftLines) {
            float paddingLeft = rectF.height() + 5 * density;
            ftLine.onDraw(canvas, paint, point, paddingLeft, drawTimes, ONCE_DRAW_TIMES);
        }
        paint.setStrokeCap(Paint.Cap.BUTT);
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    static class FtLine {
        Point point;
        float maxHeight;
        float minHeight;
        float startFraction;
        FtLine(Point point, float minHeight, float maxHeight, float startFraction) {
            this.point = point;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
            this.startFraction = startFraction;
        }
        void onDraw(Canvas canvas, Paint paint, Point parentPoint, float paddingLeft, int drawTimes, int onceDrawTimes) {
            float drawHeight = minHeight + (maxHeight - minHeight) * calculateRate(drawTimes, onceDrawTimes);
            canvas.drawLine(point.x + parentPoint.x + paddingLeft, point.y - drawHeight / 2 + parentPoint.y,
                    point.x + parentPoint.x + paddingLeft, point.y + drawHeight / 2 + parentPoint.y, paint);
        }


        /**
         * 三角波
         */
        private float calculateRate(int drawTimes, int onceDrawTimes) {
            float baseFraction = 1f * drawTimes / onceDrawTimes;
            float realFraction = baseFraction + startFraction;
            if(realFraction > 1) realFraction -= 1;
            if(realFraction > 0.5) {
                return 2f - 2 * realFraction;
            } else {
                return 2 * realFraction;
            }
        }
    }

    static float[][] sFtLines = new float[][]{
            {0.1f, 0.2f, 0.7f},
            {0.3f, 0.6f, 0.2f},
            {0.4f, 0.7f, 0.3f},
            {0.5f, 0.6f, 0.4f},
            {0.6f, 0.8f, 0.5f},
            {0.4f, 0.6f, 0.2f},
            {0.5f, 0.6f, 0.1f},
            {0.3f, 0.4f, 0.0f},
            {0.2f, 0.5f, 0.3f},
            {0.1f, 0.4f, 0.9f},
            {0.3f, 0.6f, 0.8f},
            {0.5f, 0.9f, 0.2f},
            {0.4f, 0.8f, 0.2f},
            {0.4f, 0.6f, 0.5f},
            {0.3f, 0.5f, 0.6f},
            {0.4f, 0.6f, 0.4f},
            {0.5f, 0.6f, 0.5f},
            {0.4f, 0.5f, 0.1f},
            {0.3f, 0.5f, 0.2f},
            {0.2f, 0.4f, 0.6f},
            {0.4f, 0.7f, 0.7f},
            {0.5f, 0.6f, 0.6f},
            {0.6f, 0.9f, 0.8f},
            {0.5f, 0.7f, 0.3f},
            {0.4f, 0.6f, 0.5f},
            {0.3f, 0.5f, 0.3f},
            {0.4f, 0.8f, 0.2f},
            {0.3f, 0.4f, 0.4f},
            {0.4f, 0.6f, 0.5f},
            {0.2f, 0.5f, 0.1f},
    };
}
