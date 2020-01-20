package com.example.bulletscreen;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.bulletscreen.bullet.VoiceBullet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MulMediaPlayerController {
    private static final String TAG = "MulMediaPlayer";
    private final ProgressRunnable progressRunnable;
    private List<VoiceBullet> barrages = new ArrayList<>();

    public void reset() {
        barrages.clear();
        for (MediaPlayer mediaPlayer : playerHashMap.values()) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        playerHashMap.clear();
    }

    private static class Singleton {
        static final MulMediaPlayerController context = new MulMediaPlayerController();
    }

    public static MulMediaPlayerController getInstance() {
        return Singleton.context;
    }

    private HashMap<String, MediaPlayer> playerHashMap;

    private MulMediaPlayerController() {
        playerHashMap = new HashMap<>();
        progressRunnable = new ProgressRunnable();
    }

    private Context context;

    public void initContext(Context context) {
        this.context = context;
    }


    public void onlyPlay(String id) {
        for (int i = 0; i < playerHashMap.size(); i++) {
            VoiceBullet barrage = barrages.get(i);
            MediaPlayer mediaPlayer = playerHashMap.get(barrage.getId());
            if (id.equals(barrage.getId())) {
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(0, 0);
                }
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(1, 1);
                }
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
        barrages.add(barrage);
        MediaPlayer mediaPlayer = playerHashMap.get(barrage.getId());
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            playerHashMap.put(barrage.getId(), mediaPlayer);
        }
        try {
            File file = new File(barrage.getVoiceUrl());


            //获得播放源访问入口
//            AssetFileDescriptor afd = context.getResources().openRawResourceFd(rawSource); // 注意这里的区别
//            //给MediaPlayer设置播放源
//            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

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
            e.printStackTrace();
        }
    }


    public void startPlay(int currentPosition) {
        if (barrages == null || barrages.size() <= 0) {
            return;
        }

        for (int i = 0; i < playerHashMap.size(); i++) {
            VoiceBullet barrage = barrages.get(i);
            MediaPlayer mediaPlayer = playerHashMap.get(barrage.getId());
            if (mediaPlayer != null && !mediaPlayer.isPlaying() && currentPosition > barrage.getVideoPosition()) {
                mediaPlayer.seekTo((int) (currentPosition - barrage.getVideoPosition()));
                mediaPlayer.start();
            }
        }

//        handler.post(progressRunnable);
    }

    private Handler handler = new Handler(Looper.getMainLooper());


    private class ProgressRunnable implements Runnable {

        @Override
        public void run() {
            MainActivity mainActivity = (MainActivity) (MulMediaPlayerController.this.context);
//            int currentPosition = mainActivity.videoView.getCurrentPosition();
//            int duration = mainActivity.videoView.getDuration();
//
//
//            for (int i = 0; i < playerHashMap.size(); i++) {
//                Barrage barrage = barrages.get(i);
//                MediaPlayer mediaPlayer = playerHashMap.get(barrage.getId());
//                if (mediaPlayer != null && !mediaPlayer.isPlaying() && currentPosition > barrage.getStartOffsetTime()) {
//                    mediaPlayer.seekTo((int) (currentPosition - barrage.getStartOffsetTime()));
//                    mediaPlayer.start();
//                }
//            }
//
//
//            Log.d(TAG, "====currentPosition:" + currentPosition + " duration: " + duration);
//            handler.postDelayed(progressRunnable, 50);
        }
    }

}
