package com.allsoft.testyoutubenhanh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class MainActivity2 extends AppCompatActivity {
    private StyledPlayerView playerView;
    private ExoPlayer simpleExoPlayer;
    private static final String VIDEO_TEST_URL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        playerView = findViewById(R.id.spv_video_2);

        playVideo();
    }

    private void playVideo() {
        simpleExoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(simpleExoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(VIDEO_TEST_URL);
        simpleExoPlayer.setMediaItem(mediaItem);
        simpleExoPlayer.play();
    }
}