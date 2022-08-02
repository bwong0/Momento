package com.example.momento.admin.admin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.momento.R;
import com.example.momento.database.DatabaseCallbacks;
import com.example.momento.database.PatientDB;
import com.example.momento.database.ServerCallback;
import com.example.momento.patient.patientHome;
import com.example.momento.ui.login.familyRegister;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class patientProfile extends AppCompatActivity {

    private final static String TAG = "PatientProfile";
    Button logoutButton;
    EditText title;
    Button prompt_1_upload;
    Button prompt_2_upload;
    Button prompt_3_upload;
    ImageView profileCreationImage;
    Button updateName;
    Button updatePicture;
    ProgressBar spinning_wheel;
    Drawable res;

    String uid;
    PatientDB patientDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        uid = getIntent().getStringExtra("patientUid");
        String uri = "@drawable/empty";
        res = Drawable.createFromPath(uri);

        title = (EditText) findViewById(R.id.ProfileCreationTitle);
        prompt_1_upload = (Button) findViewById(R.id.prompt_1_upload);
        prompt_2_upload = (Button) findViewById(R.id.prompt_2_upload);
        prompt_3_upload = (Button) findViewById(R.id.prompt_3_upload);
        profileCreationImage = (ImageView) findViewById((R.id.profileCreationImage));
        spinning_wheel = (ProgressBar) findViewById(R.id.progressBar);
        spinning_wheel.setVisibility(View.GONE);
        updateName = (Button) findViewById((R.id.updateName));
        updatePicture = (Button) findViewById((R.id.updatePicture));
        logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());

        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        updateName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String newname = title.getText().toString().trim();
                AlertDialog alertDialog = new AlertDialog.Builder(patientProfile.this).create();
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
                    patientDB.setFirstName(split[0]);
                    patientDB.setLastName("");
                    alertDialog.setMessage("Name has been updated.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else if (split.length == 2){
                    patientDB.setFirstName(split[0]);
                    patientDB.setLastName(split[1]);
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
        // TODO: *************  remove setText after we clean up XML **************
        prompt_1_upload.setText("Register New Family");
        prompt_2_upload.setText("View Family");
        prompt_3_upload.setText("Delete Patient");
        prompt_1_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Finish update UI text
                // goto familyRegister
                goToFamilyRegister(uid);
            }
        });
        prompt_2_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Finish update UI text
                // goto familyHome
                goToFamilyHome(uid);
            }
        });
        prompt_3_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Delete Account function
            }
        });
        updatePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { imageChooser(); }
        });

    } // end of onCreate()

    @Override
    protected void onResume() {
        super.onResume();
        patientDB = new PatientDB(uid, new ServerCallback() {
            @Override
            public void isReadyCallback(boolean isReady) {
                if (isReady) {
                    title.setText(patientDB.getFirstName() + " " + patientDB.getLastName());
                    patientDB.getProfilePicFile(new DatabaseCallbacks() {
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
                            profileCreationImage.setImageDrawable(patientProfile.this.res);
                        }
                    });
                }
            }
        });
    }

    private void goToFamilyRegister(String uid) {
        Intent intent = new Intent(this, familyRegister.class);
        intent.putExtra("patientUid", uid);
        startActivity(intent);
    }

    private void goToFamilyHome(String uid) {
        Intent intent = new Intent(this, patientHome.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    public void imageChooser() {
        String i = "image/*";
        launchGalleryPhoto.launch(i);
    }

    public void logout() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        finish();
    }

    ActivityResultLauncher<String> launchGalleryPhoto = registerForActivityResult( //Launch for Videos
            new ActivityResultContracts.GetContent(),
            resultUri -> {
                if(resultUri == null) //If the user clicks back
                    return;
                spinning_wheel.setVisibility(View.VISIBLE); //Loading starts when the upload activity Starts

                // Upload profile picture for a Family account
                patientDB.uploadProfilePic(this, resultUri, new DatabaseCallbacks() {
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
                            onResume();
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
    private void hideKeyboard(View view) {
        InputMethodManager manager =
                (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE
                );
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}