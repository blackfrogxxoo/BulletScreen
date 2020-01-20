package com.example.bulletscreen.bullet;

import android.text.TextUtils;

import com.example.bulletscreen.Comments;

import java.util.ArrayList;
import java.util.List;

public class BulletGenerator {
    public static TextBullet generate(BulletScreenView parent, int videoPosition, String text, boolean local) {
        return new TextBullet(parent, videoPosition, text, local);
    }

    public static VoiceBullet generate(BulletScreenView parent, int videoPosition, String avatar, int duration) {
        return new VoiceBullet(parent, videoPosition, avatar, duration);
    }

    public static List<Bullet> generate(BulletScreenView bulletScreenView, Comments comments) {
        List<Bullet> bullets = new ArrayList<>();
        List<Comments.RowsBean> commentList = comments.rows;
        for(Comments.RowsBean comment : commentList) {
            int videoPosition = (int) (Math.random() * 10000);
            if(!TextUtils.isEmpty(comment.audio_file_addr)) {
                int duration = (int) (Math.random() * 13000 + 3000);
                bullets.add(generate(bulletScreenView, videoPosition, comment.user_info.avatar_url, duration));
            } else {
                bullets.add(generate(bulletScreenView, videoPosition, comment.contents, false));
            }
        }
        return bullets;
    }
}
