package com.example.momento.ui.login;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.momento.R;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfileCreation extends AppCompatActivity implements Serializable {
    ArrayList<Persons> ArrayListProfiles;
    TextView title;
    Persons profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);


        ImageView pictureProfileCreation = (ImageView) findViewById(R.id.profileCreationImage);
        title = (TextView) findViewById(R.id.ProfileCreationTitle);
        profile = (Persons) getIntent().getSerializableExtra("person");




        if(profile.profilePresent == true){
            title.setText(profile.getName());
            int profilePicture = getResources().getIdentifier(profile.getImage(),null,getPackageName());
            Drawable res = getResources().getDrawable(profilePicture);
            pictureProfileCreation.setImageDrawable(res);
        }

        else {
            title.setText("New Profile");
            String uri = "@drawable/empty";
            int defaultImage = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(defaultImage);
            pictureProfileCreation.setImageDrawable(res);
        }
    }
}