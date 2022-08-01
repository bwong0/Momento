package com.example.momento.family;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher; //For Launch Gallery Intent
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.momento.R;
import com.example.momento.database.DatabaseCallbacks;
import com.example.momento.database.FamilyDB;
import com.example.momento.database.ServerCallback;
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
    int SELECT_VIDEO = 200;

    EditText title;

    Button prompt_1_upload;
    Button prompt_2_upload;
    Button prompt_3_upload;
    ImageView profileCreationImage;
    Button updateName;
    Button updatePicture;

    String uid;
    FamilyDB profile;



    /****DEBUG****/
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private final static String TAG = "ProfileCreation";
    /****DEBUG****/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        uid = getIntent().getStringExtra("uid");
        String uri = "@drawable/empty";
        int defaultImage = getResources().getIdentifier(uri,null,getPackageName());
        Drawable res = getResources().getDrawable(defaultImage);




//        ImageView pictureProfileCreation = (ImageView) findViewById(R.id.profileCreationImage);
        title = (EditText) findViewById(R.id.ProfileCreationTitle);
//        relationship = (EditText) findViewById(R.id.editRelationship);
        prompt_1_upload = (Button) findViewById(R.id.prompt_1_upload);
        prompt_2_upload = (Button) findViewById(R.id.prompt_2_upload);
        prompt_3_upload = (Button) findViewById(R.id.prompt_3_upload);
        profileCreationImage = (ImageView) findViewById((R.id.profileCreationImage));

        updateName = (Button) findViewById((R.id.updateName));
        updatePicture = (Button) findViewById((R.id.updatePicture));
//        clear =(Button) findViewById(R.id.clearButton);




        profile = new FamilyDB(uid, new ServerCallback() {
            @Override
            public void isReadyCallback(boolean isReady) {
                if (isReady) {
                    title.setText(profile.getFirstName() + " " + profile.getLastName());
                    profile.getProfilePicFile(new DatabaseCallbacks() {
                        @Override
                        public void uriCallback(Uri uri) {
                        }
                        @Override
                        public void fileCallback(File file) {
                            profileCreationImage.setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                        }
                        @Override
                        public void progressCallback(double percentage) {
                        }
                        @Override
                        public void failureCallback(boolean hasFailed, String message) {
                            profileCreationImage.setImageDrawable(res);
                        }
                    });
                }
            }
        });


        updateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newname = title.getText().toString().trim();
                AlertDialog alertDialog = new AlertDialog.Builder(ProfileCreation.this).create();
                String[] split = newname.split(" ");
                if (title.getText().toString().trim().isEmpty()){
                    alertDialog.setMessage("Name is empty, Please fill it in.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                } else if (split.length == 1){
                    profile.setFirstName(split[0]);
                    profile.setLastName("");
                    alertDialog.setMessage("Name has been updated.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else if (split.length == 2){
                    profile.setFirstName(split[0]);
                    profile.setLastName(split[1]);
                    alertDialog.setMessage("Name has been updated.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else if (split.length > 2){
                    alertDialog.setMessage("First name and Last name Only.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
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
        updatePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { imageChooser(); }
        });

    }
//    public void update (Persons persons){
//
//        Intent intent = new Intent(this, ProfileCreation.class);
//        intent.putExtra("person", persons);
//        startActivity(intent);
//    }


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
                profile.uploadVideo(this, 0, resultUri, new DatabaseCallbacks() {
                    @Override
                    public void uriCallback(Uri uri) {
                        Log.d(TAG, "Done. Upload is at " + uri.toString());
                        // TODO: Stop spinning animation here. This callback happens when upload finishes.

                    }
                    @Override
                    public void fileCallback(File file) { }
                    @Override
                    public void progressCallback(double progress) {
                        Log.d(TAG, "Upload is " + progress + "% done");
                        // TODO: Control spinning animation using this callback
                        // TODO: Start spinning animation here when progress < 100
                    }
                    @Override
                    public void failureCallback(boolean hasFailed, String message) {
                        if (hasFailed) {
                            Log.d(TAG, "Failed: " + message);
                            // TODO: Stop spinning animation, and display pop-up warning onFailure
                        }
                    }
                });
            }
    );

    ActivityResultLauncher<String> launchGalleryVideo2 = registerForActivityResult( //Launch for Videos
            new ActivityResultContracts.GetContent(),
            resultUri -> {
                profile.uploadVideo(this, 1, resultUri, new DatabaseCallbacks() {
                    @Override
                    public void uriCallback(Uri uri) {
                        Log.d(TAG, "Done. Upload is at " + uri.toString());
                    }
                    @Override
                    public void fileCallback(File file) { }
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
                profile.uploadVideo(this, 2, resultUri, new DatabaseCallbacks() {
                    @Override
                    public void uriCallback(Uri uri) {
                        Log.d(TAG, "Done. Upload is at " + uri.toString());
                    }
                    @Override
                    public void fileCallback(File file) { }
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
                // Upload profile picture for a Family account
                profile.uploadProfilePic(this, resultUri, new DatabaseCallbacks() {
                    @Override
                    public void failureCallback(boolean hasFailed, String msg) {
                        if (hasFailed) {
                            // TODO: Frontend. Do something if upload fails.
                            Log.d(TAG, "Upload failed. " + msg);
                        }
                    }
                    @Override
                    public void uriCallback(Uri uri) {
                        // TODO: do something with the Uri from Firebase, download and set displayed picture?
                        if (uri != null) {
                            Log.d(TAG, "Uri after upload: " + uri.toString());
                        }
                    }
                    @Override
                    public void fileCallback(File aFile) { // Not used but must keep it here.
                    }
                    @Override
                    public void progressCallback(double progress) {
                        // TODO: do something with the percentage. Display a busy spinning overlay?
                        Log.d(TAG, "Upload is " + progress + "% done");
                    }
                });
            }
    );

}