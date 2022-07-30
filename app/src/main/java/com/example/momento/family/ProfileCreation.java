package com.example.momento.family;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher; //For Launch Gallery Intent
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.momento.R;
import com.example.momento.ui.login.Persons;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfileCreation extends AppCompatActivity implements Serializable {
    ArrayList<Persons> ArrayListProfiles;
    int SELECT_VIDEO = 200;

    EditText title;
    EditText relationship;
    Persons profile;
    Button update;
    Button clear;
    Button prompt_1_upload;
    Button prompt_2_upload;
    Button prompt_3_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);


        ImageView pictureProfileCreation = (ImageView) findViewById(R.id.profileCreationImage);
        title = (EditText) findViewById(R.id.ProfileCreationTitle);
        relationship = (EditText) findViewById(R.id.editRelationship);
        prompt_1_upload = (Button) findViewById(R.id.prompt_1_upload);
        prompt_2_upload = (Button) findViewById(R.id.prompt_2_upload);
        prompt_3_upload = (Button) findViewById(R.id.prompt_3_upload);

        profile = (Persons) getIntent().getSerializableExtra("person");

        update = (Button) findViewById((R.id.updateButton));
        clear =(Button) findViewById(R.id.clearButton);
        String uri = "@drawable/empty";
        if(profile.profilePresent == true){
            title.setText(profile.getName());
            if(profile.getImage() != ""){
                int profilePicture = getResources().getIdentifier(profile.getImage(),null,getPackageName());
                Drawable res = getResources().getDrawable(profilePicture);
                pictureProfileCreation.setImageDrawable(res);
            }
            else{
                int defaultImage = getResources().getIdentifier(uri, null, getPackageName());
                Drawable res = getResources().getDrawable(defaultImage);
                pictureProfileCreation.setImageDrawable(res);
            }
        }


        else {
            title.setText("New Profile");

            int defaultImage = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(defaultImage);
            pictureProfileCreation.setImageDrawable(res);
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean titleIS = true;
                boolean relationshipIS = true;
                if(title.getText().toString().trim() == "" || title.getText().toString() == "New Profile"){
                    titleIS = false;
                }

                if(relationship.getText().toString().trim() == "" || relationship.getText().toString() == "Relationship"){
                    titleIS = false;
                }

                if (titleIS == true && relationshipIS == true){
                    profile.setName(title.getText().toString());
                    profile.setRelationship(relationship.getText().toString());
                    profile.setProfilePresent(true);
                    update(profile);
                }
                else if(titleIS == false){
                    title.setError("Please enter a name");
                }
                else if(relationshipIS == false){
                    relationship.setError("Please enter a relationship");
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile = new Persons();
                clear(profile);
            }
        });

        prompt_1_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoChooser();
            }
        });
        prompt_2_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoChooser();
            }
        });
        prompt_3_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoChooser();
            }
        });

    }
    public void update (Persons persons){

        Intent intent = new Intent(this, ProfileCreation.class);
        intent.putExtra("person", persons);
        startActivity(intent);
    }
    public void clear(Persons persons){
        Intent intent = new Intent(this, ProfileCreation.class);
        intent.putExtra("person", persons);
        startActivity(intent);
    }

    // this function is triggered when the Select Prompt 1 Video Button is clicked
    public void videoChooser() {

        Intent i = new Intent();// creating an instance of the intent of the type image
        i.setType("video/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i); // pass the constant to compare it with the returned requestCode
    }

     ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedVideoUri = data.getData(); //TODO: @Peter for the upload

                    }
                }
            });


}