package com.example.bulletscreen.bullet;

import android.media.MediaPlayer;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum BulletHelper {
    INSTANCE;
    private Map<String, MediaPlayer> mediaPlayerMap = new HashMap<>();
    private List<VoiceBullet> voiceBulletList = new ArrayList<>();
    private BulletScreenView bulletScreenView;

    /**
     * 切换视频时调用，瞬发
     */
    public void setBulletScreenView(BulletScreenView bulletScreenView) {
        releaseAll();
        this.bulletScreenView = bulletScreenView;
    }

    private void addVoiceBullet(VoiceBullet bullet) {
        List<VoiceBullet> bullets = new ArrayList<>();
        bullets.add(bullet);
        addVoiceBullets(bullets);
    }

    private void addVoiceBullets(List<VoiceBullet> bullets) {
        voiceBulletList.addAll(bullets);
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

    /**
     * 切换视频后，拉取数据，再add
     */
    public void addBullets(List<Bullet> bullets) {
        bulletScreenView.setBullets(bullets);
        for(Bullet bullet : bullets) {
            if(bullet instanceof VoiceBullet) {
                addVoiceBullet((VoiceBullet) bullet);
            }
        }
    }

    public void pauseAll() {
        Set<String> keySet = mediaPlayerMap.keySet();
        for(String key : keySet) {
            MediaPlayer mp = mediaPlayerMap.get(key);
            mp.pause();
        }
        bulletScreenView.onVideoPause();
    }

    public void startAll() {
        Set<String> keySet = mediaPlayerMap.keySet();
        for(String key : keySet) {
            MediaPlayer mp = mediaPlayerMap.get(key);
            mp.start();
        }
        bulletScreenView.onVideoResume();
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
        for(VoiceBullet vb : voiceBulletList) {
            if (vb.videoPosition < position && !vb.playing) {
                start(vb.id);
                vb.playing = true;
            }
        }
    }

    public void release(String id) {
        MediaPlayer mp = mediaPlayerMap.get(id);
        mp.release();
        mp = null;
        mediaPlayerMap.remove(id);
        VoiceBullet toRemove = null;
        for(VoiceBullet vb : voiceBulletList) {
            if(TextUtils.equals(vb.getId(), id)) {
                toRemove = vb;
                break;
            }
        }
        if(toRemove != null) {
            voiceBulletList.remove(toRemove);
            toRemove = null;
        }
    }

    private void releaseAll() {
        Set<String> keySet = mediaPlayerMap.keySet();
        for (String id : keySet) {
            MediaPlayer mp = mediaPlayerMap.get(id);
            mp.release();
            mp = null;
        }
        mediaPlayerMap.clear();
        voiceBulletList.clear();
    }

    public void onVideoRestart() {
        for(VoiceBullet vb : voiceBulletList) {
            vb.playing = false;
        }
    }
}
