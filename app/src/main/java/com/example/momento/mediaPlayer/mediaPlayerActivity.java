package com.example.momento.mediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

import com.example.momento.R;
import com.example.momento.database.FamilyDB;
import com.example.momento.database.ServerCallback;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

public class mediaPlayerActivity extends AppCompatActivity {

    protected ExoPlayer player;
    protected StyledPlayerView playerView;
    protected String videoUrl = "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4";
    private String familyUid;
    private FamilyDB familyDb;
    private int videoIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        playerView = findViewById(R.id.idStyledPlayerView);

        videoIndex = getIntent().getIntExtra("index", 0);
        videoUrl = getIntent().getStringExtra("url");
        familyUid = getIntent().getStringExtra("uid");

        player = new ExoPlayer.Builder(mediaPlayerActivity.this).build();
        playerView.setPlayer(player);
        // Build the media item.
        Uri videoUri = Uri.parse(videoUrl);
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        // Set the media item to be played.
        player.setMediaItem(mediaItem);
        // Prepare the player.
        player.prepare();

        familyDb = new FamilyDB(familyUid, new ServerCallback() {
            @Override
            public void isReadyCallback(boolean isReady) {
                if (isReady) {
                    // Start the playback.
                    player.play();
                    familyDb.incrementVideoPlayCount(videoIndex);
                    player.addListener(new Player.Listener() {
                        @Override
                        public void onPlaybackStateChanged(int i) {
                            if (i == Player.STATE_ENDED) {
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }
}