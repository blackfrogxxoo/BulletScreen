package com.example.bulletscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.bulletscreen.bullet.Bullet;
import com.example.bulletscreen.bullet.BulletGenerator;
import com.example.bulletscreen.bullet.BulletScreenView;
import com.example.bulletscreen.bullet.BulletHelper;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String COMMENTS_TEXT =
            "{\"count\":82,\"total_page\":9,\"current_page\":3,\"page_size\":10,\"rows\":[{\"id\":\"40359\",\"pid\":\"0\",\"uid\":\"1052748\",\"contents\":\"\",\"audio_file_addr\":\"https:\\/\\/static1.kuaiyin123.cn\\/voice\\/voice5e1ede7fa814c876061832.amr\",\"audio_file_type\":\"amr\",\"audio_file_size\":\"28134\",\"audio_play_time\":\"18\",\"comments_type\":\"2\",\"create_time\":\"1579081343\",\"praise_num\":\"10\",\"user_info\":{\"user_id\":\"1052748\",\"nickname\":\"\\u8336\\u8272\\u6eab\\u7738\",\"avatar_url\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/rw5mLNEdt1N2VOhpj5zOVtUrbyu1dR3YmkTjNcqNDb01MFTU7xH5zeMM7BG77iaPmlPuemXLgpY8dSGvVkeKn8g\\/132\",\"sex\":\"\\u5973\",\"city\":\"\",\"age\":-1},\"is_praised\":0},{\"id\":\"39305\",\"pid\":\"0\",\"uid\":\"1058412\",\"contents\":\"\",\"audio_file_addr\":\"https:\\/\\/static1.kuaiyin123.cn\\/voice\\/voice5e1e8ce4ebafd309030187.amr\",\"audio_file_type\":\"amr\",\"audio_file_size\":\"79974\",\"audio_play_time\":\"50\",\"comments_type\":\"2\",\"create_time\":\"1579060452\",\"praise_num\":\"3\",\"user_info\":{\"user_id\":\"1058412\",\"nickname\":\"SDL  \\u76ae\\u5361\\u6770\\u745e\",\"avatar_url\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/yicwAG7FcGUXH0fvquW6ZaRYORJNU4RBXJ3B0xjfUic1lU5qT5zwdMKL6YM7AscaTjVMPuXfspib4IWLVsDW550Wg\\/132\",\"sex\":\"\\u7537\",\"city\":\"\",\"age\":-1},\"is_praised\":0},{\"id\":\"38980\",\"pid\":\"0\",\"uid\":\"1054238\",\"contents\":\"\",\"audio_file_addr\":\"https:\\/\\/static1.kuaiyin123.cn\\/voice\\/voice5e1e6a67e6347116149644.amr\",\"audio_file_type\":\"amr\",\"audio_file_size\":\"37734\",\"audio_play_time\":\"22\",\"comments_type\":\"2\",\"create_time\":\"1579051624\",\"praise_num\":\"4\",\"user_info\":{\"user_id\":\"1054238\",\"nickname\":\"\\u6653\\u6653.\",\"avatar_url\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/wb6ibhnIHzicxU5Rru3s8U1m6iamI7KdvzZBt1iawhGiacVQ3z4U7u4P0BB5tBMvoHuR5nPjmh0Dh6ARToN6amuc4Qw\\/132\",\"sex\":\"\\u5973\",\"city\":\"\\u53f0\\u5dde\",\"age\":-1},\"is_praised\":0},{\"id\":\"38484\",\"pid\":\"0\",\"uid\":\"1043615\",\"contents\":\"\",\"audio_file_addr\":\"https:\\/\\/static1.kuaiyin123.cn\\/voice\\/voice5e1dd0aa2cdad153399478.amr\",\"audio_file_type\":\"amr\",\"audio_file_size\":\"10822\",\"audio_play_time\":\"7\",\"comments_type\":\"2\",\"create_time\":\"1579012266\",\"praise_num\":\"4\",\"user_info\":{\"user_id\":\"1043615\",\"nickname\":\"A\\u4fe1\\u7528\\u5361&\\u517b\\u5361\\u673a~\\u5218\\u683718291890864\",\"avatar_url\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/Q0j4TwGTfTKY2qKktZffhnKoT9etTEuWDw2GGXS1F9tv39TZYOdmtGHStenoHrz5YR2FWp0icUTJFwtzNYJPh5A\\/132\",\"sex\":\"\\u5973\",\"city\":\"\",\"age\":23},\"is_praised\":0},{\"id\":\"38466\",\"pid\":\"0\",\"uid\":\"1044535\",\"contents\":\"\\u6b64\\u751f\\u4e0d\\u6362\",\"audio_file_addr\":\"\",\"audio_file_type\":\"\",\"audio_file_size\":\"0\",\"audio_play_time\":\"0\",\"comments_type\":\"1\",\"create_time\":\"1579011855\",\"praise_num\":\"1\",\"user_info\":{\"user_id\":\"1044535\",\"nickname\":\"\\u5b64\\u72ec\\u5416~\",\"avatar_url\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/Jd0kOrxeibvt9qLFicu1OPnBSdEI7Lp6hUbEiaPibSqVyF0qwlPINnhZ5MsG9fq7AB6DzTcjpVBbt70CGJDKMMEgVQ\\/132\",\"sex\":\"\\u7537\",\"city\":\"\\u5609\\u5174\",\"age\":16},\"is_praised\":0},{\"id\":\"38376\",\"pid\":\"0\",\"uid\":\"518688\",\"contents\":\"\\u6b4c\\u540d\\u4ec0\\u4e48\",\"audio_file_addr\":\"\",\"audio_file_type\":\"\",\"audio_file_size\":\"0\",\"audio_play_time\":\"0\",\"comments_type\":\"1\",\"create_time\":\"1579010306\",\"praise_num\":\"1\",\"user_info\":{\"user_id\":\"518688\",\"nickname\":\".\",\"avatar_url\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/qqvrleiaY02Brj3QkaO66BcZdWyeNdZZE4Ful67KXCgrf1mQomBVOCA7BlGpcfAGbhjq7FYFrE0m0EZcpUhp4lQ\\/132\",\"sex\":\"\\u4fdd\\u5bc6\",\"city\":\"\",\"age\":-1},\"is_praised\":0},{\"id\":\"38344\",\"pid\":\"0\",\"uid\":\"1036219\",\"contents\":\"\",\"audio_file_addr\":\"https:\\/\\/static1.kuaiyin123.cn\\/voice\\/voice5e1dc74a38d50118695513.amr\",\"audio_file_type\":\"amr\",\"audio_file_size\":\"60646\",\"audio_play_time\":\"38\",\"comments_type\":\"2\",\"create_time\":\"1579009866\",\"praise_num\":\"5\",\"user_info\":{\"user_id\":\"1036219\",\"nickname\":\"Unicorn.\",\"avatar_url\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/ZD2OiasPqROU6hTZgI9YFNwqda9jH7ZxtGgwou2HLCwlcDDfmvksASiccK4UAgrR0nsPAHRGdfWw6jz1diaNGBm0Q\\/132\",\"sex\":\"\\u5973\",\"city\":\"\\u846b\\u82a6\\u5c9b\",\"age\":-1},\"is_praised\":0},{\"id\":\"38189\",\"pid\":\"0\",\"uid\":\"1043265\",\"contents\":\"\",\"audio_file_addr\":\"https:\\/\\/static1.kuaiyin123.cn\\/voice\\/voice5e1dbbeced9be616581248.amr\",\"audio_file_type\":\"amr\",\"audio_file_size\":\"62886\",\"audio_play_time\":\"40\",\"comments_type\":\"2\",\"create_time\":\"1579006957\",\"praise_num\":\"4\",\"user_info\":{\"user_id\":\"1043265\",\"nickname\":\"\\u661f\\u8bfa\",\"avatar_url\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/Q0j4TwGTfTLAJ8r5xfBTcSbsdEyubUibbBjVuEtjZInHdDzvGgTBgZVBlNC4qg9Vrlicx9YhVIvoNRz1gb7yNPaw\\/132\",\"sex\":\"\\u5973\",\"city\":\"\\u5f90\\u5dde\",\"age\":-1},\"is_praised\":0},{\"id\":\"37610\",\"pid\":\"0\",\"uid\":\"1038665\",\"contents\":\"\",\"audio_file_addr\":\"https:\\/\\/static1.kuaiyin123.cn\\/voice\\/voice5e1d9ad30fc7f287777681.amr\",\"audio_file_type\":\"amr\",\"audio_file_size\":\"3558\",\"audio_play_time\":\"3\",\"comments_type\":\"2\",\"create_time\":\"1578998483\",\"praise_num\":\"2\",\"user_info\":{\"user_id\":\"1038665\",\"nickname\":\"\\u4f18\\u4f60\",\"avatar_url\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/Jg7KkTDicMu7bWKoqiatQhGFFicvZlYYDc0ibFtVodEKyeTvDInrdnR0bVxern5hT2BWuVAh65qB812zpHxVKJP6ww\\/132\",\"sex\":\"\\u4fdd\\u5bc6\",\"city\":\"\",\"age\":-1},\"is_praised\":0},{\"id\":\"37167\",\"pid\":\"0\",\"uid\":\"1038224\",\"contents\":\"\",\"audio_file_addr\":\"https:\\/\\/static1.kuaiyin123.cn\\/voice\\/voice5e1d7ab996417571759498.amr\",\"audio_file_type\":\"amr\",\"audio_file_size\":\"39142\",\"audio_play_time\":\"25\",\"comments_type\":\"2\",\"create_time\":\"1578990265\",\"praise_num\":\"9\",\"user_info\":{\"user_id\":\"1038224\",\"nickname\":\"\\u661f\\u8bed\\u661f\\u613f\",\"avatar_url\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/Q0j4TwGTfTKAXY2brIaKwRoIicGicfBqKfdBIfV3GOxyfI6YP89ibSPJ0msxTQredCDQxQzicLm3J0nVKdpOcHhcew\\/132\",\"sex\":\"\\u5973\",\"city\":\"\\u5415\\u6881\",\"age\":21},\"is_praised\":0}]}";
    private RecyclerView rv;
    private Adapter adapter;
    private Comments comments;
    private int rvPage = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileUtils.copyAssets2FileDir(this);
        rv = findViewById(R.id.rv);
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        final PagerSnapHelper mLinearSnapHelper = new PagerSnapHelper();
        mLinearSnapHelper.attachToRecyclerView(rv);
        comments = new Gson().fromJson(COMMENTS_TEXT, Comments.class);
        adapter = new Adapter(this, comments);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(rvPage == -1) {
                    rvPage = 0;
                    Holder holder = (Holder) rv.findViewHolderForLayoutPosition(rvPage);
                    onNewHolder(holder);
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = mLinearSnapHelper.findSnapView(llm);
                    int position = llm.getPosition(centerView);
                    if(position != rvPage) {
                        rvPage = position;
                        Holder holder = (Holder) rv.findViewHolderForLayoutPosition(rvPage);
                        onNewHolder(holder);
                        Log.e("Snapped Item Position:","" + rvPage);
                    }
                }
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(videoView != null) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int position = videoView.getCurrentPosition();
                                BulletHelper.INSTANCE.onVideoPosition(position);
                                holder.tvPosition.setText(position + "/" + holder.videoView.getDuration());
                            }
                        });
//                        MulMediaPlayerController.getInstance().startPlay(position);
                    } catch (Exception e) {

                    }
                }
            }
        }, 0, 5);
    }


    private VideoView videoView;
    private Holder holder;
    private void onNewHolder(@NonNull final Holder holder) {
        List<Bullet> bulletList = BulletGenerator.generate(holder.bulletScreenView, comments);
        BulletHelper.INSTANCE.setBulletScreenView(holder.bulletScreenView);
        BulletHelper.INSTANCE.addBullets(bulletList);
        if(this.holder != null) {
            holder.videoView.pause();
        }
        this.holder = holder;
        videoView = holder.videoView;
//        MulMediaPlayerController.getInstance().reset();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
                BulletHelper.INSTANCE.onVideoRestart();
            }
        });
        videoView.start();
    }

    boolean pauseByScreenOff;

    @Override
    protected void onPause() {
        super.onPause();
        if(videoView != null && videoView.isPlaying()) {
            pauseByScreenOff = true;
            videoView.pause();
            BulletHelper.INSTANCE.pauseAll();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pauseByScreenOff) {
            pauseByScreenOff = false;
            videoView.start();
            BulletHelper.INSTANCE.startAll();
        }
    }

    static class Adapter extends RecyclerView.Adapter<Holder> {

        private Context context;
        private Comments comments;

        Adapter(Context context, Comments comments) {
            this.context = context;
            this.comments = comments;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false);
            return new Holder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    holder.bulletScreenView.addBullet(
//                            BulletGenerator.generate(holder.bulletScreenView, (int) holder.fakeVideoDurationAnimator.getAnimatedValue(), "我的弹幕", true));
                }
            });
            holder.bulletScreenView.setOnBulletClickListener(new BulletScreenView.OnBulletClickListener() {
                @Override
                public void onBulletClick(Bullet bullet) {
                    if(bullet != null) {
                        Toast.makeText(context, bullet.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.videoView.isPlaying()) {
                        holder.videoView.pause();
                        BulletHelper.INSTANCE.pauseAll();
                    } else {
                        holder.videoView.start();
                        BulletHelper.INSTANCE.startAll();
                    }
                }
            });
            File file = new File(FileUtils.getExternalAssetsDir(context), "bg.mp4");
            Log.i("fuck", "onBindViewHolder: " + file.getAbsolutePath());
            holder.videoView.setVideoPath(file.getAbsolutePath());
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView tvPosition;
        Button button;
        BulletScreenView bulletScreenView;
        VideoView videoView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            button = itemView.findViewById(R.id.btn_send);
            bulletScreenView = itemView.findViewById(R.id.bulletScreenView);
            videoView = itemView.findViewById(R.id.videoView);
        }
    }
}
