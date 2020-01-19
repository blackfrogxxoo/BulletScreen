package com.example.bulletscreen.bullet;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class TextBullet extends Bullet {
    private static final int DP_PADDING = 4;
    String text;
    float textSize;
    int textColor = Color.WHITE;
    boolean local;

    TextBullet(BulletScreenView parent, int videoPosition, String text) {
        this(parent, videoPosition, text, false);
    }

    TextBullet(BulletScreenView parent, int videoPosition, String text, boolean local) {
        super(parent, videoPosition);
        this.duration = 6000;
        this.text = text;
        this.textSize = density * 14;
        this.local = local;
    }


    @Override
    void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(textSize);
        rectF.left = point.x;
        rectF.top = point.y;
        float textWidth = paint.measureText(text);
        rectF.right = rectF.left + textWidth + 2 * DP_PADDING * density;
        rectF.bottom = rectF.top + textSize + 2 * DP_PADDING * density;
        if(local) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(density);
            paint.setColor(Color.GREEN);
            canvas.drawRect(rectF, paint);
            paint.setStyle(Paint.Style.FILL);
        }
        paint.setColor(textColor);
        canvas.drawText(text, point.x + DP_PADDING * density, rectF.bottom - DP_PADDING * density - density, paint);
    }
}
