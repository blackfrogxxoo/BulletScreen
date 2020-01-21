package com.example.bulletscreen;

import android.media.MediaPlayer;
import android.util.Log;

import com.example.bulletscreen.bullet.VoiceBullet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MulMediaPlayerController {
    private static final String TAG = "MulMediaPlayer";
    private List<VoiceBullet> barrages = new ArrayList<>();

    private HashMap<String, MediaPlayer> playerHashMap;
    private HashMap<String, Boolean> playerStatusHashMap;


    private static class Singleton {
        static final MulMediaPlayerController context = new MulMediaPlayerController();
    }

    public static MulMediaPlayerController getInstance() {
        return Singleton.context;
    }


    private MulMediaPlayerController() {
        playerHashMap = new HashMap<>();
        playerStatusHashMap = new HashMap<>();
    }


    public void reset() {
        Log.d(TAG, "=====reset");
        barrages.clear();
        for (MediaPlayer mediaPlayer : playerHashMap.values()) {
            mediaPlayer.release();
        }
        playerHashMap.clear();
        playerStatusHashMap.clear();
    }

    /*
       只播放某个弹幕
    */
    public void playForBarrage(String id) {
        for (int i = 0; i < playerHashMap.size(); i++) {
            VoiceBullet barrage = barrages.get(i);
            MediaPlayer mediaPlayer = playerHashMap.get(barrage.getId());
            if (id.equals(barrage.getId())) {
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(1, 1);
                }
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(0, 0);
                }
            }
        }
    }

    public void pauseAllBarrage() {
        for (int i = 0; i < playerHashMap.size(); i++) {
            VoiceBullet barrage = barrages.get(i);
            MediaPlayer mediaPlayer = playerHashMap.get(barrage.getId());
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                Log.d(TAG, "=====pauseAllBarrage:" + i + barrage.getId());
            }
        }
    }


    public void resumeAllBarrage() {
        for (int i = 0; i < playerHashMap.size(); i++) {
            VoiceBullet barrage = barrages.get(i);
            MediaPlayer mediaPlayer = playerHashMap.get(barrage.getId());
            if (mediaPlayer != null) {
                mediaPlayer.start();
                Log.d(TAG, "=====pauseAllBarrage:" + i + barrage.getId());
            }
        }

    }


    /*
    静音某个弹幕
     */
    public void muteForBarrage(String id) {
        for (int i = 0; i < playerHashMap.size(); i++) {
            VoiceBullet barrage = barrages.get(i);
            MediaPlayer mediaPlayer = playerHashMap.get(barrage.getId());
            if (id.equals(barrage.getId())) {
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(0, 0);
                }
            }
        }
    }

    /*
       外部音乐播放进度回调时调用
     */
    public void onPositionChange(int currentPosition) {
        if (barrages == null || barrages.size() <= 0) {
            return;
        }

        for (int i = 0; i < playerHashMap.size(); i++) {
            VoiceBullet barrage = barrages.get(i);
            MediaPlayer mediaPlayer = playerHashMap.get(barrage.getId());
            Boolean isPlay = false;
            Boolean play = playerStatusHashMap.get(barrage.getId());
            if (play != null) {
                isPlay = play;
            }
            if (mediaPlayer != null && !isPlay && currentPosition > barrage.getVideoPosition()) {
                mediaPlayer.seekTo((int) (currentPosition - barrage.getVideoPosition()));
                mediaPlayer.start();
                playerStatusHashMap.put(barrage.getId(), true);
            }

        }


    }


    /*
       仅当外部视频seek时操作
     */
    public void seek(int currentPosition) {
        if (barrages == null || barrages.size() <= 0) {
            return;
        }

        for (int i = 0; i < playerHashMap.size(); i++) {
            VoiceBullet barrage = barrages.get(i);
            MediaPlayer mediaPlayer = playerHashMap.get(barrage.getId());
            if (mediaPlayer != null && currentPosition > barrage.getVideoPosition()) {
                mediaPlayer.seekTo((int) (currentPosition - barrage.getVideoPosition()));
                mediaPlayer.start();
            }
        }
    }


    public void addBarrages(List<VoiceBullet> barrages) {
        this.barrages = barrages;
        for (VoiceBullet barrage : barrages) {
            addBarrage(barrage);
        }
    }

    public void addBarrage(VoiceBullet barrage) {
        Log.d(TAG, "=====addBarrage");
        barrages.add(barrage);
        MediaPlayer mediaPlayer = playerHashMap.get(barrage.getId());
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            playerHashMap.put(barrage.getId(), mediaPlayer);
            playerStatusHashMap.put(barrage.getId(), false);
        }
        try {
            File file = new File(barrage.getVoiceUrl());
            mediaPlayer.setDataSource(file.getAbsolutePath());
            //设置准备就绪状态监听
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "======MediaPlayer onPrepared");
                    // 开始播放
//                    mp.start();
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.d(TAG, "======MediaPlayer onCompletion");
                }
            });

            mediaPlayer.prepare();
        } catch (IOException e) {
//            e.printStackTrace();
            Log.d(TAG, "======" + e.getLocalizedMessage());
        }
    }


}
