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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public enum BulletHelper {
    INSTANCE;
    private Map<String, MediaPlayer> mediaPlayerMap = new HashMap<>();
    private List<VoiceBullet> voiceBulletList = new ArrayList<>();
    private BulletScreenView bulletScreenView;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private int position;

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
    public void addBullets(final List<Bullet> bullets) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bulletScreenView.setBullets(bullets);
                for(Bullet bullet : bullets) {
                    if(bullet instanceof VoiceBullet) {
                        addVoiceBullet((VoiceBullet) bullet);
                    }
                }
            }
        });
    }

    public void pauseAll() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Set<String> keySet = mediaPlayerMap.keySet();
                for(String key : keySet) {
                    MediaPlayer mp = mediaPlayerMap.get(key);
                    if (ifJump(key, mp)) continue;
                    mp.pause();
                }
                bulletScreenView.onVideoPause();
            }
        });
    }

    public void startAll() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Set<String> keySet = mediaPlayerMap.keySet();
                for(String key : keySet) {
                    MediaPlayer mp = mediaPlayerMap.get(key);
                    if (ifJump(key, mp)) continue;
                    mp.start();
                }
                bulletScreenView.onVideoResume();
            }
        });
    }

    private boolean ifJump(String key, MediaPlayer mp) {
        boolean jump = false;
        for (VoiceBullet vb : voiceBulletList) {
            if (TextUtils.equals(vb.getId(), key)) {
                int endPlayingPosition = vb.videoPosition + mp.getDuration();
                if (endPlayingPosition < position) jump = true;
                break;
            }
        }
        return jump;
    }

    public void pause(String id) {
        MediaPlayer mp = mediaPlayerMap.get(id);
        mp.pause();
    }

    public void start(String id, int offset) {
        MediaPlayer mp = mediaPlayerMap.get(id);
        mp.start();
        mp.seekTo(offset);
    }

    public void onVideoPosition(final int position) {
        this.position = position;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bulletScreenView.onVideoPosition(position);
                for(VoiceBullet vb : voiceBulletList) {
                    if (vb.videoPosition <= position && !vb.playing) {
                        start(vb.id, position - vb.videoPosition);
                        vb.playing = true;
                    }
                }
            }
        });
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
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Set<String> keySet = mediaPlayerMap.keySet();
                for (String id : keySet) {
                    MediaPlayer mp = mediaPlayerMap.get(id);
                    mp.release();
                    mp = null;
                }
                mediaPlayerMap.clear();
                voiceBulletList.clear();
            }
        });
    }

    public void onVideoRestart() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for(VoiceBullet vb : voiceBulletList) {
                    vb.playing = false;
                }
            }
        });
    }
}
