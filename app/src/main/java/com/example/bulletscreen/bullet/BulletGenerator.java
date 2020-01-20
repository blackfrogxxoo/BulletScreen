package com.example.bulletscreen.bullet;

import android.text.TextUtils;

import com.example.bulletscreen.Comments;
import com.example.bulletscreen.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BulletGenerator {
    public static TextBullet generate(BulletScreenView parent, String id, int videoPosition, String text, boolean local) {
        return new TextBullet(parent, id, videoPosition, text, local);
    }

    public static VoiceBullet generate(BulletScreenView parent, String id, int videoPosition, String avatar, int duration) {
        return new VoiceBullet(parent, id, videoPosition, avatar, duration);
    }

    public static List<Bullet> generate(BulletScreenView bulletScreenView, Comments comments) {
        List<Bullet> bullets = new ArrayList<>();
        List<Comments.RowsBean> commentList = comments.rows;
        int j = 0;
        for (int i = 0; i < commentList.size(); i++) {
            Comments.RowsBean comment = commentList.get(i);
            if (!TextUtils.isEmpty(comment.audio_file_addr)) {
                int videoPosition = positions[j ++];
                int duration = (int) (Math.random() * 13000 + 3000);
                VoiceBullet voiceBullet = generate(bulletScreenView, comment.id, videoPosition, comment.user_info.avatar_url, duration);
                File file = new File(FileUtils.getExternalAssetsDir(bulletScreenView.getContext()), mp3s[j - 1]);
                voiceBullet.voiceUrl = file.getAbsolutePath();
                bullets.add(voiceBullet);
                if(j >= 4) j = 0;
            } else {
                bullets.add(generate(bulletScreenView, comment.id, 3300, comment.contents, false));
            }

        }

        /*
        ArrayList<Barrage> barrages = new ArrayList<>();
        barrages.add(new Barrage("1", "1041569_0039553f381fd8d8fcb3656c81b3b06c.mp3", 600));
        barrages.add(new Barrage("2", "1457381_56d8fec7d2f7eb7cb7b301b97c3bf567.mp3", 1033));
        barrages.add(new Barrage("3", "760887_290b10ee3399e5d7a05e2aac42191a9f.mp3", 2316));
        barrages.add(new Barrage("4", "1553418_016b100bf23cf677d0f02d6421eca93d.mp3", 8442));
         */
        return bullets;
    }

    static String[] mp3s = new String[]{
            "1041569_0039553f381fd8d8fcb3656c81b3b06c.mp3",
            "1457381_56d8fec7d2f7eb7cb7b301b97c3bf567.mp3",
            "760887_290b10ee3399e5d7a05e2aac42191a9f.mp3",
            "1553418_016b100bf23cf677d0f02d6421eca93d.mp3"
    };

    static int[] positions = new int[] {
            600,
            1033,
            2316,
            8442
    };
}
