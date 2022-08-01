package com.example.momento.family;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher; //For Launch Gallery Intent
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.momento.R;
import com.example.momento.database.DatabaseCallbacks;
import com.example.momento.database.FamilyDB;
import com.example.momento.ui.login.Persons;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
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
    ImageButton profileCreationImage;

    FamilyDB familyDb;

    /****DEBUG****/
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private final static String TAG = "ProfileCreation";
    /****DEBUG****/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        /**Temporary Authentication for Development ***/
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("peterfan01@gmail.com", "test123")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = mAuth.getCurrentUser();
                            familyDb = new FamilyDB(firebaseUser.getUid());
                        } else {
                            //
                        }
                    }
                });

        /*****/

        ImageView pictureProfileCreation = (ImageView) findViewById(R.id.profileCreationImage);
        title = (EditText) findViewById(R.id.ProfileCreationTitle);
        relationship = (EditText) findViewById(R.id.editRelationship);
        prompt_1_upload = (Button) findViewById(R.id.prompt_1_upload);
        prompt_2_upload = (Button) findViewById(R.id.prompt_2_upload);
        prompt_3_upload = (Button) findViewById(R.id.prompt_3_upload);
        profileCreationImage = (ImageButton) findViewById((R.id.profileCreationImage));

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
                videoChooser(1);
            }
        });
        prompt_2_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoChooser(2);
            }
        });
        prompt_3_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoChooser(3);
            }
        });
        profileCreationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { imageChooser(); }
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
    public void videoChooser(int n) {
        String v = "video/*";
        switch(n)
        {
            case 1:
                launchGalleryVideo1.launch(v); // pass the constant to compare it with the returned requestCode
                break;
            case 2:
                launchGalleryVideo2.launch(v); // pass the constant to compare it with the returned requestCode
                break;
            case 3:
                launchGalleryVideo3.launch(v); // pass the constant to compare it with the returned requestCode
                break;
        }
    }

    public void imageChooser() {
        String i = "image/*";
        launchGalleryPhoto.launch(i);
    }

    ActivityResultLauncher<String> launchGalleryVideo1 = registerForActivityResult( //Launch for Videos
            new ActivityResultContracts.GetContent(),
            resultUri -> {
                // Do something with resultUri
                familyDb.uploadVideo(this, 0, resultUri, new DatabaseCallbacks() {
                    @Override
                    public void uriCallback(Uri uri) {
                        Log.d(TAG, "Done. Upload is at " + uri.toString());
                    }

                    @Override
                    public void fileCallback(File file) {

                    }
                    @Override
                    public void progressCallback(double progress) {
                        Log.d(TAG, "Upload is " + progress + "% done");
                    }
                    @Override
                    public void failureCallback(boolean hasFailed, String message) {
                        if (hasFailed) {
                            Log.d(TAG, "Failed: " + message);
                        }
                    }
                });
            }
    );

    ActivityResultLauncher<String> launchGalleryVideo2 = registerForActivityResult( //Launch for Videos
            new ActivityResultContracts.GetContent(),
            resultUri -> {
                familyDb.uploadVideo(this, 1, resultUri, new DatabaseCallbacks() {
                    @Override
                    public void uriCallback(Uri uri) {
                        Log.d(TAG, "Done. Upload is at " + uri.toString());
                    }

                    @Override
                    public void fileCallback(File file) {

                    }
                    @Override
                    public void progressCallback(double progress) {
                        Log.d(TAG, "Upload is " + progress + "% done");
                    }
                    @Override
                    public void failureCallback(boolean hasFailed, String message) {
                        if (hasFailed) {
                            Log.d(TAG, "Failed: " + message);
                        }
                    }
                });
            }
    );

    ActivityResultLauncher<String> launchGalleryVideo3 = registerForActivityResult( //Launch for Videos
            new ActivityResultContracts.GetContent(),
            resultUri -> {
                familyDb.uploadVideo(this, 2, resultUri, new DatabaseCallbacks() {
                    @Override
                    public void uriCallback(Uri uri) {
                        Log.d(TAG, "Done. Upload is at " + uri.toString());
                    }

                    @Override
                    public void fileCallback(File file) {

                    }
                    @Override
                    public void progressCallback(double progress) {
                        Log.d(TAG, "Upload is " + progress + "% done");
                    }
                    @Override
                    public void failureCallback(boolean hasFailed, String message) {
                        if (hasFailed) {
                            Log.d(TAG, "Failed: " + message);
                        }
                    }
                });
            }
    );

    ActivityResultLauncher<String> launchGalleryPhoto = registerForActivityResult( //Launch for Videos
            new ActivityResultContracts.GetContent(),
            resultUri -> {
//                // Upload profile picture for a Family account
//                familyDb.storage.uploadProfilePic(this, resultUri, new DatabaseCallbacks() {
//                    @Override
//                    public void failureCallback(boolean hasFailed, String msg) {
//                        if (hasFailed) {
//                            // TODO: Frontend. Do something if upload fails.
//                            Log.d(TAG, "Upload failed. " + msg);
//                        }
//                    }
//                    @Override
//                    public void uriCallback(Uri uri) {
//                        // TODO: do something with the Uri from Firebase, download and set displayed picture?
//                        if (uri != null) {
//                            Log.d(TAG, "Uri after upload: " + uri.toString());
//                        }
//                    }
//                    @Override
//                    public void fileCallback(File aFile) { // Not used but have to keep it here.
//                    }
//                    @Override
//                    public void progressCallback(double progress) {
//                        // TODO: do something with the percentage. Display a busy spinning overlay?
//                        Log.d(TAG, "Upload is " + progress + "% done");
//                    }
//                });
            }
    );

}