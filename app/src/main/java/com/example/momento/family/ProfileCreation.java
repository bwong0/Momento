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

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);


        ImageView pictureProfileCreation = (ImageView) findViewById(R.id.profileCreationImage);
        title = (EditText) findViewById(R.id.ProfileCreationTitle);
        relationship = (EditText) findViewById(R.id.editRelationship);
        prompt_1_upload = (Button) findViewById(R.id.prompt_1_upload);

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

        // creating an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("video/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it with the returned requestCode
        //try 1: startActivityForResult(Intent.createChooser(i, "Select Media"), SELECT_VIDEO);
        //try 2:
        launchSomeActivity(i);

        //try 3:
        /*int result_launcher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // parse result and perform action
            }*/
    }

    //try 1
    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // compare the resultCode with the SELECT_PICTURE constant
            if (requestCode == SELECT_VIDEO) {
                // Get the url of the image from data
                Uri selectedVideoUri = data.getData();
                if (null != selectedVideoUri) {
                    // Gets the uri //TODO for upload to database: what happens with the returned video?

                }
            }
        }
    }*/ // NOT WORKING

    //try 2:
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

     //try 3:

}