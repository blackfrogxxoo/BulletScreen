package com.example.bulletscreen.bullet;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

public abstract class Bullet implements Comparable {
    BulletScreenView parent;
    final float density;
    final int videoPosition;
    int duration;
    Point point;
    final RectF rectF = new RectF();
    String id;

    public int getVideoPosition() {
        return videoPosition;
    }

    Bullet(BulletScreenView parent, String id, int videoPosition) {
        this.parent = parent;
        this.videoPosition = videoPosition;
        density = parent.getResources().getDisplayMetrics().density;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    void onDraw(Canvas canvas, Paint paint) {
        draw(canvas, paint);
    }

    abstract void draw(Canvas canvas, Paint paint);

    abstract void prepareDraw(Paint paint);

    boolean isOutOfScreen() {
        return rectF.left >= parent.getRight() || rectF.right <= parent.getLeft();
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Bullet) {
            if (videoPosition > ((Bullet)o).videoPosition) {
                return 1;
            } else if(videoPosition < ((Bullet)o).videoPosition) {
                return -1;
            }
        }
        return 0;
    }
}
