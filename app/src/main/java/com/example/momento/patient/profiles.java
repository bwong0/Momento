package com.example.momento.patient;

import androidx.appcompat.app.AppCompatActivity;
import com.example.momento.R;
import com.example.momento.database.FamilyDB;
import com.example.momento.ui.login.Persons;

import android.graphics.drawable.Drawable;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class profiles extends AppCompatActivity {

    TextView prompt1;
    TextView prompt2;
    TextView prompt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        String uid = getIntent().getStringExtra("uid");
        FamilyDB profile = new FamilyDB(uid);

        ImageView profileImageView = (ImageView) findViewById(R.id.profileImageView);
        TextView profileName = (TextView) findViewById(R.id.profileName);

        profileName.setText(profile.getFirstName() + " " + profile.getFirstName());
//        int profilePicture = getResources().getIdentifier(profile.getImage(),null,getPackageName());
//        Drawable res = getResources().getDrawable(profilePicture);
//        profileImageView.setImageDrawable(res);

        // Initialize TextView objects
        prompt1 = findViewById((R.id.prompt_1));
        prompt2 = findViewById((R.id.prompt_2));
        prompt3 = findViewById((R.id.prompt_3));

        // Apply OnClickListener to TextView
        // navigates to another activity

        prompt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openVidPlayer();}
        });

        prompt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openVidPlayer();}
        });

        prompt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openVidPlayer();}
        });
    }

    private void openVidPlayer(){
        // TODO: add code to launch video player activity
        Intent intent = new Intent(this, com.example.momento.mediaPlayer.mediaPlayerActivity.class);
        startActivity(intent);
    }
}