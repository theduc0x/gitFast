package com.allsoft.testyoutubenhanh;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class MainActivity extends AppCompatActivity {
    StyledPlayerView spvVideo;
    ExoPlayer exoPlayer;
    MediaSource mediaSourceAudio, mediaSourceVideo;
    Button btOpen;
    //Minimum Video you want to buffer while Playing
    private int MIN_BUFFER_DURATION = 200;
    //Max Video you want to buffer during PlayBack
    private int MAX_BUFFER_DURATION = 1000;
    //Min Video you want to buffer before start Playing it
    private int MIN_PLAYBACK_START_BUFFER = 150;
    //Min video You want to buffer when user resumes video
    private int MIN_PLAYBACK_RESUME_BUFFER = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spvVideo = findViewById(R.id.spv_video);
        btOpen = findViewById(R.id.bt_open);

        String url = "https://www.youtube.com/watch?v=pf9vN_Jm9Rs";
        String url1 = "https://www.youtube.com/watch?v=IZ_1y36i2qg&list=RDBiR7P5n0yHM&index=27";

//        LoadControl loadControl = new DefaultLoadControl.Builder()
//                .setAllocator(new DefaultAllocator(true, 16))
//                .setBufferDurationsMs(MIN_BUFFER_DURATION,
//                        MAX_BUFFER_DURATION,
//                        MIN_PLAYBACK_START_BUFFER,
//                        MIN_PLAYBACK_RESUME_BUFFER)
//                .setTargetBufferBytes(-1)
//                .setPrioritizeTimeOverSizeThresholds(true).build();

        TrackSelector trackSelector = new DefaultTrackSelector(this);
        exoPlayer = new ExoPlayer.Builder(this).build();
//                .setTrackSelector(trackSelector).setLoadControl(loadControl).build();

        setVideoPath(url1);
        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });


    }

    public void setVideoPath(String url) {

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Player.Listener.super.onPlayerError(error);
                Toast.makeText(MainActivity.this, "Can't play video", Toast.LENGTH_SHORT).show();
            }

//            @Override
//            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                Player.Listener.super.onPlayerStateChanged(playWhenReady, playbackState);
//                if (playbackState == Player.STATE_BUFFERING) {
//                    pbLoading.setVisibility(View.VISIBLE);
//                } else if (playbackState == Player.STATE_READY) {
//                    pbLoading.setVisibility(View.GONE);
//                }
//            }
        });

        spvVideo.setPlayer(exoPlayer);
        playVideoYoutube(url);
//        exoPlayer.prepare();
//                    exoPlayer.seekTo(0);
//                    exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        exoPlayer.prepare();
//        exoPlayer.setWakeMode(2);
        exoPlayer.setHandleAudioBecomingNoisy(true);
        exoPlayer.setPlayWhenReady(true);
//        exoPlayer.setvideo
    }

    @SuppressLint("StaticFieldLeak")
    private void playVideoYoutube(String youtubeUrl) {
        new YouTubeExtractor(this) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                String videoUrl = "";
                if (ytFiles != null) {
                    int videoTag = 137;
                    int audioTag = 140;

                    List<Integer> iTags = new ArrayList<>();
                    iTags.add(18);
                    iTags.add(22);
                    iTags.add(137);

                    if (ytFiles.get(137) == null) {
                        if (ytFiles.get(136) == null) {
                            if (ytFiles.get(135) == null) {
                                videoUrl = ytFiles.get(134).getUrl();
                            } else
                            {
                                videoUrl = ytFiles.get(135).getUrl();
                            }
                        } else {
                            videoUrl = ytFiles.get(136).getUrl();
                        }
                    } else {
                        videoUrl = ytFiles.get(137).getUrl();
                    }

                    mediaSourceVideo = new ProgressiveMediaSource.Factory(
                            new DefaultDataSource.Factory(MainActivity.this)).createMediaSource(
                            MediaItem.fromUri(ytFiles.get(18).getUrl()));
                    mediaSourceAudio = new ProgressiveMediaSource.Factory(
                            new DefaultDataSource.Factory(MainActivity.this)).createMediaSource(
                            MediaItem.fromUri(ytFiles.get(audioTag).getUrl()));


//                    exoPlayer.setMediaSource(new MergingMediaSource(
//                                    true,
//                                    mediaSourceVideo,
//                                    mediaSourceAudio),
//                            true
//                    );
                    exoPlayer.setMediaSource(mediaSourceVideo, true);
                }
            }
        }.extract(youtubeUrl);
    }
}
