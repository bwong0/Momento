package com.example.momento.family;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher; //For Launch Gallery Intent
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;

import com.example.momento.BuildConfig;
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
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ProfileCreation extends AppCompatActivity {

    EditText title;
    Button logoutButton;
    Button prompt_1_upload;
    Button prompt_2_upload;
    Button prompt_3_upload;
    ImageView profileCreationImage;
    Button updateName;
    Button updatePicture;
    ProgressBar spinning_wheel;
    String uri = "@drawable/empty";
    Drawable res;

    String uid;
    FamilyDB profile;

    Uri photoUri;
    String uuid;
    File outputDir;
    File file;

    private final static String TAG = "ProfileCreation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        uid = getIntent().getStringExtra("uid");

        // UI Componenets
        res = Drawable.createFromPath(uri);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());

        profileCreationImage = (ImageView) findViewById((R.id.profileCreationImage)); // picture
        title = (EditText) findViewById(R.id.ProfileCreationTitle);

        prompt_1_upload = (Button) findViewById(R.id.prompt_1_upload);
        prompt_2_upload = (Button) findViewById(R.id.prompt_2_upload);
        prompt_3_upload = (Button) findViewById(R.id.prompt_3_upload);

        spinning_wheel = (ProgressBar) findViewById(R.id.progressBar);
        spinning_wheel.setVisibility(View.GONE);

        updateName = (Button) findViewById((R.id.updateName));
        updatePicture = (Button) findViewById((R.id.updatePicture));

        // Connect to Database & update profile name and picture
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
            public void onClick(View v) {
//                imageChooser();
                takePhoto();
            }
        });

    }

    public void takePhoto() {
        uuid = UUID.randomUUID().toString();
        outputDir = this.getCacheDir();
        Log.d(TAG, "TakePhoto()" + uuid);

        try {
            file = File.createTempFile( uuid, ".jpg", outputDir );
        } catch(IOException e ) {
            Log.d(TAG, e.getMessage());
            return;
        }

        photoUri = Uri.fromFile(file);


        Log.d(TAG, photoUri.toString());


        launchCamera.launch(photoUri);
    }


    ActivityResultLauncher<Uri> launchCamera = registerForActivityResult( //Launch for Videos
            new ActivityResultContracts.TakePicture(),
            result -> {
                Log.d(TAG, result.toString());
                Log.d(TAG, photoUri.toString());
                if(!result) { //If the user clicks back
                    return;
                }

                spinning_wheel.setVisibility(View.VISIBLE); //Loading starts when the upload activity Starts
                // Upload profile picture for a Family account
                profile.uploadProfilePic(this, photoUri, new DatabaseCallbacks() {
                    @Override
                    public void failureCallback(boolean hasFailed, String msg) {
                        if (hasFailed) {
                            // TODO: Frontend. Do something if upload fails.
                            Log.d(TAG, "Upload failed. " + msg);
                            spinning_wheel.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void uriCallback(Uri uri) {
                        // TODO: do something with the Uri from Firebase, download and set displayed picture?
                        if (uri != null) {
                            Log.d(TAG, "Uri after upload: " + uri.toString());
                            spinning_wheel.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void fileCallback(File aFile) { // Not used but must keep it here.
                    }
                    @Override
                    public void progressCallback(double progress) {
                        // TODO: do something with the percentage. Display a busy spinning overlay?
                        Log.d(TAG, "Upload is " + progress + "% done");
                        if(progress < 100)
                            spinning_wheel.setVisibility(View.VISIBLE);
                    }
                });
                spinning_wheel.setVisibility(View.GONE);
            }
    );

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
                if(resultUri == null) //If the user clicks back
                    return;

                spinning_wheel.setVisibility(View.VISIBLE); //Loading starts when the upload activity Starts

                // Do something with resultUri
                profile.uploadVideo(this, 0, resultUri, new DatabaseCallbacks() {
                    @Override
                    public void uriCallback(Uri uri) {
                        Log.d(TAG, "Done. Upload is at " + uri.toString());
                        // Stop spinning animation here. This callback happens when upload finishes.
                        spinning_wheel.setVisibility(View.GONE);
                    }
                    @Override
                    public void fileCallback(File file) { }
                    @Override
                    public void progressCallback(double progress) {
                        Log.d(TAG, "Upload is " + progress + "% done");
                        // Control spinning animation using this callback
                        // Start spinning animation here when progress < 100
                        if(progress < 100)
                            spinning_wheel.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void failureCallback(boolean hasFailed, String message) {
                        if (hasFailed) {
                            Log.d(TAG, "Failed: " + message);
                            // Stop spinning animation, and display pop-up warning onFailure
                            spinning_wheel.setVisibility(View.GONE);
                        }
                    }
                });
                spinning_wheel.setVisibility(View.GONE);
            }
    );

    ActivityResultLauncher<String> launchGalleryVideo2 = registerForActivityResult( //Launch for Videos
            new ActivityResultContracts.GetContent(),
            resultUri -> {
                if(resultUri == null) //If the user clicks back
                    return;

                spinning_wheel.setVisibility(View.VISIBLE); //Loading starts when the upload activity Starts

                profile.uploadVideo(this, 1, resultUri, new DatabaseCallbacks() {
                    @Override
                    public void uriCallback(Uri uri) {
                        Log.d(TAG, "Done. Upload is at " + uri.toString());
                        spinning_wheel.setVisibility(View.GONE);
                    }
                    @Override
                    public void fileCallback(File file) { }
                    @Override
                    public void progressCallback(double progress) {
                        Log.d(TAG, "Upload is " + progress + "% done");
                        if(progress < 100)
                            spinning_wheel.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void failureCallback(boolean hasFailed, String message) {
                        if (hasFailed) {
                            Log.d(TAG, "Failed: " + message);
                            spinning_wheel.setVisibility(View.GONE);
                        }
                    }
                });
                spinning_wheel.setVisibility(View.GONE);
            }
    );

    ActivityResultLauncher<String> launchGalleryVideo3 = registerForActivityResult( //Launch for Videos
            new ActivityResultContracts.GetContent(),
            resultUri -> {
                if(resultUri == null) //If the user clicks back
                    return;

                spinning_wheel.setVisibility(View.VISIBLE); //Loading starts when the upload activity Starts

                profile.uploadVideo(this, 2, resultUri, new DatabaseCallbacks() {
                    @Override
                    public void uriCallback(Uri uri) {
                        Log.d(TAG, "Done. Upload is at " + uri.toString());
                        spinning_wheel.setVisibility(View.GONE);
                    }
                    @Override
                    public void fileCallback(File file) { }
                    @Override
                    public void progressCallback(double progress) {
                        Log.d(TAG, "Upload is " + progress + "% done");
                        if(progress < 100)
                            spinning_wheel.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void failureCallback(boolean hasFailed, String message) {
                        if (hasFailed) {
                            Log.d(TAG, "Failed: " + message);
                            spinning_wheel.setVisibility(View.GONE);
                        }
                    }
                });
                spinning_wheel.setVisibility(View.GONE);
            }
    );

    ActivityResultLauncher<String> launchGalleryPhoto = registerForActivityResult( //Launch for Videos
            new ActivityResultContracts.GetContent(),
            resultUri -> {
                if(resultUri == null) //If the user clicks back
                    return;

                spinning_wheel.setVisibility(View.VISIBLE); //Loading starts when the upload activity Starts

                // Upload profile picture for a Family account
                profile.uploadProfilePic(this, resultUri, new DatabaseCallbacks() {
                    @Override
                    public void failureCallback(boolean hasFailed, String msg) {
                        if (hasFailed) {
                            // TODO: Frontend. Do something if upload fails.
                            Log.d(TAG, "Upload failed. " + msg);
                            spinning_wheel.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void uriCallback(Uri uri) {
                        // TODO: do something with the Uri from Firebase, download and set displayed picture?
                        if (uri != null) {
                            Log.d(TAG, "Uri after upload: " + uri.toString());
                            spinning_wheel.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void fileCallback(File aFile) { // Not used but must keep it here.
                    }
                    @Override
                    public void progressCallback(double progress) {
                        // TODO: do something with the percentage. Display a busy spinning overlay?
                        Log.d(TAG, "Upload is " + progress + "% done");
                        if(progress < 100)
                            spinning_wheel.setVisibility(View.VISIBLE);
                    }
                });
                spinning_wheel.setVisibility(View.GONE);
            }
    );

    public void logout() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        finish();
    }
}