package com.example.bulletscreen.bullet;

import android.media.MediaPlayer;

import com.example.bulletscreen.bullet.Bullet;
import com.example.bulletscreen.bullet.VoiceBullet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum VoiceBulletPlayer {
    INSTANCE;
    private Map<String, MediaPlayer> mediaPlayerMap = new HashMap<>();
    private List<VoiceBullet> bulletList = new ArrayList<>();
    private BulletScreenView bulletScreenView;

    /**
     * 切换视频时调用，瞬发
     */
    public void setBulletScreenView(BulletScreenView bulletScreenView) {
        this.bulletScreenView = bulletScreenView;
    }

    public void addBullet(VoiceBullet bullet) {
        List<VoiceBullet> bullets = new ArrayList<>();
        bullets.add(bullet);
        addBullets(bullets);
    }

    /**
     * 切换视频后，拉取数据，再add
     */
    public void addBullets(List<VoiceBullet> bullets) {
        bulletList.addAll(bullets);
        for(VoiceBullet bullet : bullets) {
            String id = bullet.getId();
            MediaPlayer mp = new MediaPlayer();
            mediaPlayerMap.put(id, mp);
            try {
                mp.setDataSource(bullet.getVoiceUrl());
                mp.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace(); // TODO
            }
        }
    }

    public void pauseAll() {
        Set<String> keySet = mediaPlayerMap.keySet();
        for(String key : keySet) {
            MediaPlayer mp = mediaPlayerMap.get(key);
            mp.pause();
        }
    }

    public void startAll() {
        Set<String> keySet = mediaPlayerMap.keySet();
        for(String key : keySet) {
            MediaPlayer mp = mediaPlayerMap.get(key);
            mp.start();
        }
    }

    public void pause(String id) {
        MediaPlayer mp = mediaPlayerMap.get(id);
        mp.pause();
    }

    public void start(String id) {
        MediaPlayer mp = mediaPlayerMap.get(id);
        mp.start();
    }

    public void onVideoPosition(int position) {
        bulletScreenView.onVideoPosition(position);
    }

    public void release(String id) {
        MediaPlayer mp = mediaPlayerMap.get(id);
        mp.release();
        mp = null;
        mediaPlayerMap.remove(id);
    }

}
