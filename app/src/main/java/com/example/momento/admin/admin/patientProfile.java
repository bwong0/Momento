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
import com.example.momento.database.FamilyDB;
import com.example.momento.database.PatientDB;
import com.example.momento.database.ServerCallback;
import com.example.momento.patient.patientHome;
import com.example.momento.ui.login.familyRegister;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.List;

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

    long[] counts = new long[18];
    List<String> familyUids;
    FamilyDB famDB1, famDB2, famDB3, famDB4, famDB5, famDB6;

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
        prompt_3_upload.setText("Show Charts");
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
                // TODO: Show video count charts selections
                goToBarChartRedirect(counts);
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
                    if(patientDB.getFamilyList() != null) {
                        familyUids = patientDB.getFamilyList();
                        int arrSize = familyUids.size();
                        if (arrSize > 0) {
                            famDB1 = new FamilyDB(familyUids.get(0), new ServerCallback() {
                                @Override
                                public void isReadyCallback(boolean isReady) {
                                    if (isReady) {
                                        counts[0] = famDB1.getVideoPlayCount(0);
                                        counts[1] = famDB1.getVideoPlayCount(1);
                                        counts[2] = famDB1.getVideoPlayCount(2);
                                    }
                                }
                            });
                        }//end of if arrSize
                        if (arrSize > 1) {
                            famDB2 = new FamilyDB(familyUids.get(1), new ServerCallback() {
                                @Override
                                public void isReadyCallback(boolean isReady) {
                                    if (isReady) {
                                        counts[3] = famDB2.getVideoPlayCount(0);
                                        counts[4] = famDB2.getVideoPlayCount(1);
                                        counts[5] = famDB2.getVideoPlayCount(2);
                                    }
                                }
                            });
                        }//end of if arrSize
                        if (arrSize > 2) {
                            famDB3 = new FamilyDB(familyUids.get(2), new ServerCallback() {
                                @Override
                                public void isReadyCallback(boolean isReady) {
                                    if (isReady) {
                                        counts[6] = famDB3.getVideoPlayCount(0);
                                        counts[7] = famDB3.getVideoPlayCount(1);
                                        counts[8] = famDB3.getVideoPlayCount(2);
                                    }
                                }
                            });
                        }//end of if arrSize
                        if (arrSize > 3) {
                            famDB4 = new FamilyDB(familyUids.get(3), new ServerCallback() {
                                @Override
                                public void isReadyCallback(boolean isReady) {
                                    if (isReady) {
                                        counts[9] = famDB4.getVideoPlayCount(0);
                                        counts[10] = famDB4.getVideoPlayCount(1);
                                        counts[11] = famDB4.getVideoPlayCount(2);
                                    }
                                }
                            });
                        }//end of if arrSize
                        if (arrSize > 4) {
                            famDB5 = new FamilyDB(familyUids.get(4), new ServerCallback() {
                                @Override
                                public void isReadyCallback(boolean isReady) {
                                    if (isReady) {
                                        counts[12] = famDB5.getVideoPlayCount(0);
                                        counts[13] = famDB5.getVideoPlayCount(1);
                                        counts[14] = famDB5.getVideoPlayCount(2);
                                    }
                                }
                            });
                        }//end of if arrSize
                        if (arrSize > 5) {
                            famDB6 = new FamilyDB(familyUids.get(5), new ServerCallback() {
                                @Override
                                public void isReadyCallback(boolean isReady) {
                                    if (isReady) {
                                        counts[15] = famDB6.getVideoPlayCount(0);
                                        counts[16] = famDB6.getVideoPlayCount(1);
                                        counts[17] = famDB6.getVideoPlayCount(2);
                                    }
                                }
                            });
                        }
                    }//end of if arrSize
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

    private void goToBarChartRedirect(long[] videoCount){
        Intent intent = new Intent(this, barChartRedirect.class);
        intent.putExtra("video counts", videoCount);
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