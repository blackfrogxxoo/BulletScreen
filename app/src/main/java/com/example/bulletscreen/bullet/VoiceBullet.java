package com.example.bulletscreen.bullet;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 带头像（bitmap -> url)
 * 可播放声音（player -> url）
 * 可能需要绘制内部动效（画线条）
 */
class VoiceBullet extends Bullet {
    private static final int DP_HEIGHT = 30;
    private static final double ONCE_DRAW_TIMES = 100;
    private static final int DP_LINE_WIDTH = 3;
    private static final int DP_LINE_DIVIDER = 1;
    private static final int LINE_COUNT_ONE_WAVE = 20;
    final String bitmapUrl;
    final Matrix matrix = new Matrix();
    String voiceUrl;
    Bitmap bitmap;
    boolean loadingBitmap;
    private int drawTimes;
    private Map<Integer, Float> ftHeights = new HashMap<>();

    VoiceBullet(BulletScreenView parent, int videoPosition, String bitmapUrl, int duration) {
        super(parent, videoPosition);
        this.bitmapUrl = bitmapUrl;
        this.duration = duration;
    }

    private float calculateWidth() {
        return Math.min(parent.getWidth() / 2, duration / 10);
    }

    @Override
    void draw(Canvas canvas, Paint paint) {
        float radius = DP_HEIGHT * density;
        rectF.left = point.x;
        rectF.top = point.y;
        rectF.right = rectF.left + calculateWidth();
        rectF.bottom = rectF.top + DP_HEIGHT * density;
        paint.setColor(Color.parseColor("#88000000"));
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setColor(Color.WHITE);
        if (bitmap != null) {
            float size = rectF.bottom - rectF.top;
            matrix.setScale(size / bitmap.getWidth(), size / bitmap.getHeight());
            matrix.postTranslate(point.x, point.y);
            canvas.drawBitmap(bitmap, matrix, paint);
        }
        drawTimes ++;
        if(drawTimes > ONCE_DRAW_TIMES) {
            drawTimes = 0;
        }
        float x = 0;
        boolean heightInited = ftHeights.size() != 0;
        for(int i = 0; x < rectF.width() - 1.2 * rectF.height(); i ++) {
            x = i * density * (DP_LINE_WIDTH + DP_LINE_DIVIDER) + 0.5f * density * DP_LINE_WIDTH;
            float y = rectF.height() / 2;
            float lineHeight;
            if(!heightInited) {
                lineHeight = (float) (Math.random() * rectF.height() * 0.6f);
                ftHeights.put(i, lineHeight);
            } else if(drawTimes % 7 != 0){
                lineHeight = ftHeights.get(i);
            } else {
                lineHeight = (float) (Math.random() * rectF.height() * 0.6f);
                ftHeights.put(i, lineHeight);
            }
            float startY = y - lineHeight / 2;
            float stopY = y + lineHeight / 2;
            canvas.drawLine(point.x + rectF.height() + x, point.y + startY, point.x + rectF.height() + x, point.y + stopY, paint);
        }
    }

}
