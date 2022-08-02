package com.example.momento.patient;

import androidx.appcompat.app.AppCompatActivity;
import com.example.momento.R;
import com.example.momento.database.DatabaseCallbacks;
import com.example.momento.database.FamilyDB;
import com.example.momento.database.ServerCallback;
import com.example.momento.database.VideoInfo;
import com.example.momento.ui.login.Persons;

import android.graphics.drawable.Drawable;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class profiles extends AppCompatActivity {

    ImageView profileImageView;
    TextView profileName;
    TextView prompt1;
    TextView prompt2;
    TextView prompt3;
    String uri = "@drawable/empty";
    Drawable res;

    FamilyDB familyDb;
    String familyUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        profileName = (TextView) findViewById(R.id.profileName);
        prompt1 = findViewById((R.id.prompt_1));
        prompt2 = findViewById((R.id.prompt_2));
        prompt3 = findViewById((R.id.prompt_3));
        res = Drawable.createFromPath(uri);
        // default values
        profileImageView.setImageDrawable(res);
//        profileName.setText("no name");

        prompt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openVidPlayer(0);}
        });

        prompt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openVidPlayer(1);}
        });

        prompt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openVidPlayer(2);}
        });



        familyUid = getIntent().getStringExtra("uid");

        familyDb = new FamilyDB(familyUid, new ServerCallback() {
            @Override
            public void isReadyCallback(boolean isReady) {
                if (isReady) {
                    // set name
                    profileName.setText(familyDb.getFirstName() + " " + familyDb.getLastName());
                    // set profile picture
                    familyDb.getProfilePicFile(new DatabaseCallbacks() {
                        @Override
                        public void uriCallback(Uri uri) {}
                        @Override
                        public void fileCallback(File file) {
                            profileImageView.setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                        }
                        @Override
                        public void progressCallback(double percentage) {}
                        @Override
                        public void failureCallback(boolean hasFailed, String message) {
                            profileImageView.setImageDrawable(res);
                        }
                    });
                }
            }
        });
    } // End of onCreate

    private void openVidPlayer(int index){
        // TODO: add code to launch video player activity
        String url = familyDb.getVideoUrl(index);
        Intent intent = new Intent(this, com.example.momento.mediaPlayer.mediaPlayerActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("url", url);
        intent.putExtra("uid", familyUid);
        startActivity(intent);
    }
}