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

    Bullet(BulletScreenView parent, int videoPosition) {
        this.parent = parent;
        this.videoPosition = videoPosition;
        density = parent.getResources().getDisplayMetrics().density;
    }

    void onDraw(Canvas canvas, Paint paint) {
        draw(canvas, paint);
    }

    abstract void draw(Canvas canvas, Paint paint);

    boolean isOutOfScreen() {
        return rectF.left > parent.getRight() || rectF.right < parent.getLeft();
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
