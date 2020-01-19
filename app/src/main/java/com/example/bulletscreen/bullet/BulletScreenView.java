package com.example.bulletscreen.bullet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bulletscreen.CircleTransformWithBorder;
import com.example.bulletscreen.R;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BulletScreenView extends BaseSurfaceView {
    private static final String TAG = "BulletScreenView";
    private Paint paint;
    private List<Bullet> bulletList = new CopyOnWriteArrayList<>();
    private GestureDetector gestureDetector;
    private OnBulletClickListener onBulletClickListener;
    private int videoPosition;
    private boolean paused;


    public BulletScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return getTouchedBullet(e) != null;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if(onBulletClickListener != null) {
                    onBulletClickListener.onBulletClick(getTouchedBullet(e));
                }
                return true;
            }
        };
        gestureDetector = new GestureDetector(context, gestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private Bullet getTouchedBullet(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        for(Bullet bullet : bulletList) {
            if (!bullet.isOutOfScreen()) {
                if(bullet.rectF.left < x && bullet.rectF.right > x
                        && bullet.rectF.top < y && bullet.rectF.bottom > y) {
                    return bullet;
                }
            }
        }
        return null;
    }

    @Override
    void drawS(Canvas canvas) {
        for(Bullet bullet : bulletList) {
            if(bullet.point == null) {
                bullet.point = new Point();
                bullet.point.y = (int) (Math.random() * getHeight()); // TODO 优化弹幕之间的距离
                bullet.point.x = getWidth();
            } else {
                if(!paused) {
                    float dx = (bullet.rectF.width() + getWidth()) / bullet.duration;
                    int x = (int) (dx * (bullet.videoPosition - videoPosition) + getWidth());
                    bullet.point.x = x;
                }
            }

            if(bullet instanceof VoiceBullet) {
                final VoiceBullet vb = (VoiceBullet) bullet;
                if(vb.bitmap == null && !vb.loadingBitmap) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            RequestBuilder<Bitmap> requestBuilder = Glide.with(getContext()).asBitmap().load(R.drawable.ic_launcher_foreground);
                            Glide.with(getContext()).asBitmap().load(vb.bitmapUrl)
                                    .apply(new RequestOptions().transform(new CircleTransformWithBorder( 0, Color.TRANSPARENT)))
                                    .thumbnail(requestBuilder).into(
                                        new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                vb.bitmap = resource;
                                                vb.loadingBitmap = false;
                                            }
                                        }
                                    );
                        }
                    });

                    vb.loadingBitmap = true;
                }
            }
            bullet.onDraw(canvas, paint);
        }
    }

    public void addBullet(Bullet bullet) {
        bulletList.add(bullet);
        Collections.sort(bulletList); // TODO 后续可做index的范围控制
    }

    public void setBullets(List<Bullet> bullets) {
        bulletList.clear();
        bulletList.addAll(bullets);
        Collections.sort(bulletList);
    }
    public void addBullets(List<Bullet> bullets) {
        bulletList.addAll(bullets);
        Collections.sort(bulletList);
    }

    public void onVideoPrepare() {
        // TODO 云端获取弹幕
    }

    public void onVideoStart() { // 包括重放
        // TODO 开始输入弹幕
    }

    public void onVideoPause() {
        // TODO 弹幕暂停
        paused = true;
    }

    public void onVideoResume() {
        // TODO 弹幕继续
        paused = false;
    }

    public void onVideoStop() {
        // do nothing
    }

    public void onVideoPosition(int position) {
        this.videoPosition = position;
    }

    public void setOnBulletClickListener(OnBulletClickListener listener) {
        this.onBulletClickListener = listener;
    }

    public boolean isPaused() {
        return paused;
    }

    public interface OnBulletClickListener {
        void onBulletClick(Bullet bullet);
    }

}
