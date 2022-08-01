package com.example.momento.admin.admin;

import androidx.appcompat.app.AppCompatActivity;
import com.example.momento.R;
import com.example.momento.database.AdminDB;
import com.example.momento.database.DatabaseCallbacks;
import com.example.momento.database.PatientDB;
import com.example.momento.database.ServerCallback;
import com.example.momento.family.ProfileCreation;
import com.example.momento.ui.login.patientRegister;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class patientCreation extends AppCompatActivity {

    private static final String TAG = "PatientCreation";

    // UI components
    public TextView patientName1;
    public TextView patientName2;
    public TextView patientName3;
    public TextView patientName4;
    public TextView patientName5;
    public TextView patientName6;
    ArrayList<TextView> NamesArrayList; // Array containing TextView variables

    public ImageButton imageButton1;
    public ImageButton imageButton2;
    public ImageButton imageButton3;
    public ImageButton imageButton4;
    public ImageButton imageButton5;
    public ImageButton imageButton6;
    ArrayList<ImageButton> ImageButtonArrayList; // Array containing ImageButtons

    // Server
    String adminUid;
    private AdminDB admin;
    private List<String> patientUids;
    PatientDB patient1;
    PatientDB patient2;
    PatientDB patient3;
    PatientDB patient4;
    PatientDB patient5;
    PatientDB patient6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get PatientList from Server
        adminUid = getIntent().getStringExtra("uid");

        // Set Components for Rendering
        setContentView(R.layout.activity_patient_creation);

        // binding Textview under profilePic to a variable (default: "Create New Patient")
        patientName1 = (TextView) findViewById(R.id.patientNameAdmin1);
        patientName2 = (TextView) findViewById(R.id.patientNameAdmin2);
        patientName3 = (TextView) findViewById(R.id.patientNameAdmin3);
        patientName4 = (TextView) findViewById(R.id.patientNameAdmin4);
        patientName5 = (TextView) findViewById(R.id.patientNameAdmin5);
        patientName6 = (TextView) findViewById(R.id.patientNameAdmin6);

        NamesArrayList = new ArrayList<>();
        NamesArrayList.add(patientName1);   // not sure if order is correct
        NamesArrayList.add(patientName2);
        NamesArrayList.add(patientName3);
        NamesArrayList.add(patientName4);
        NamesArrayList.add(patientName5);
        NamesArrayList.add(patientName6);

        // binding ImageButton profilePic (default: @drawable/empty)
        imageButton1 = (ImageButton) findViewById(R.id.patientAdmin1);
        imageButton2 = (ImageButton) findViewById(R.id.patientAdmin2);
        imageButton3 = (ImageButton) findViewById(R.id.patientAdmin3);
        imageButton4 = (ImageButton) findViewById(R.id.patientAdmin4);
        imageButton5 = (ImageButton) findViewById(R.id.patientAdmin5);
        imageButton6 = (ImageButton) findViewById(R.id.patientAdmin6);

        ImageButtonArrayList = new ArrayList<>();
        ImageButtonArrayList.add(imageButton1);
        ImageButtonArrayList.add(imageButton2);
        ImageButtonArrayList.add(imageButton3);
        ImageButtonArrayList.add(imageButton4);
        ImageButtonArrayList.add(imageButton5);
        ImageButtonArrayList.add(imageButton6);

        // Set all imageButtons to open patientRegister by default
        imageButton1.setOnClickListener(v -> openProfileCreation(adminUid));
        imageButton2.setOnClickListener(v -> openProfileCreation(adminUid));
        imageButton3.setOnClickListener(v -> openProfileCreation(adminUid));
        imageButton4.setOnClickListener(v -> openProfileCreation(adminUid));
        imageButton5.setOnClickListener(v -> openProfileCreation(adminUid));
        imageButton6.setOnClickListener(v -> openProfileCreation(adminUid));

        NamesArrayList.get(0).setVisibility(View.INVISIBLE);
        NamesArrayList.get(1).setVisibility(View.INVISIBLE);
        NamesArrayList.get(2).setVisibility(View.INVISIBLE);
        NamesArrayList.get(3).setVisibility(View.INVISIBLE);
        NamesArrayList.get(4).setVisibility(View.INVISIBLE);
        NamesArrayList.get(5).setVisibility(View.INVISIBLE);
        ImageButtonArrayList.get(0).setVisibility(View.INVISIBLE);
        ImageButtonArrayList.get(1).setVisibility(View.INVISIBLE);
        ImageButtonArrayList.get(2).setVisibility(View.INVISIBLE);
        ImageButtonArrayList.get(3).setVisibility(View.INVISIBLE);
        ImageButtonArrayList.get(4).setVisibility(View.INVISIBLE);
        ImageButtonArrayList.get(5).setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get patientList from Server
        admin = new AdminDB(adminUid, new ServerCallback() {
            @Override
            public void isReadyCallback(boolean isReady) {
                patientUids = admin.getPatientList(); // TODO: local variable too slow?
                Log.d(TAG, "admin.getPatientList: " + patientUids);
                int arrSize = patientUids.size();
                if (arrSize > 0) {
                    patient1 = new PatientDB(patientUids.get(0), new ServerCallback() {
                        @Override
                        public void isReadyCallback(boolean isReady) {
                            if (isReady) {
                                NamesArrayList.get(0).setText(patient1.getFirstName() + " " + patient1.getLastName());
                                NamesArrayList.get(0).setVisibility(View.VISIBLE);
                                ImageButtonArrayList.get(0).setVisibility(View.VISIBLE);
                                ImageButtonArrayList.get(0).setOnClickListener(v -> openPatientProfileEdit(patientUids.get(0)));

                                patient1.getProfilePicFile(new DatabaseCallbacks() {
                                    @Override
                                    public void uriCallback(Uri uri) {}
                                    @Override
                                    public void fileCallback(File file) {
                                        Log.d(TAG, file.getName());
                                        ImageButtonArrayList.get(0).setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                                    }
                                    @Override
                                    public void progressCallback(double percentage) {}
                                    @Override
                                    public void failureCallback(boolean hasFailed, String message) {
                                        String emptyUri = "@drawable/empty";
                                        Drawable res = Drawable.createFromPath(emptyUri);
                                        ImageButtonArrayList.get(0).setImageDrawable(res);
                                    }
                                });
                            }
                        }
                    });
                } // end of if
                if (arrSize > 1) {
                    patient2 = new PatientDB(patientUids.get(1), new ServerCallback() {
                        @Override
                        public void isReadyCallback(boolean isReady) {
                            if (isReady) {
                                NamesArrayList.get(1).setText(patient2.getFirstName() + " " + patient2.getLastName());
                                NamesArrayList.get(1).setVisibility(View.VISIBLE);
                                ImageButtonArrayList.get(1).setVisibility(View.VISIBLE);
                                ImageButtonArrayList.get(1).setOnClickListener(v -> openPatientProfileEdit(patientUids.get(1)));

                                patient2.getProfilePicFile(new DatabaseCallbacks() {
                                    @Override
                                    public void uriCallback(Uri uri) {}
                                    @Override
                                    public void fileCallback(File file) {
                                        Log.d(TAG, file.getName());
                                        ImageButtonArrayList.get(1).setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                                    }
                                    @Override
                                    public void progressCallback(double percentage) {}
                                    @Override
                                    public void failureCallback(boolean hasFailed, String message) {
                                        String emptyUri = "@drawable/empty";
                                        Drawable res = Drawable.createFromPath(emptyUri);
                                        ImageButtonArrayList.get(1).setImageDrawable(res);
                                    }
                                });
                            }
                        }
                    });
                } // end of if
                if (arrSize > 2) {
                    patient3 = new PatientDB(patientUids.get(2), new ServerCallback() {
                        @Override
                        public void isReadyCallback(boolean isReady) {
                            if (isReady) {
                                NamesArrayList.get(2).setText(patient3.getFirstName() + " " + patient3.getLastName());
                                NamesArrayList.get(2).setVisibility(View.VISIBLE);
                                ImageButtonArrayList.get(2).setVisibility(View.VISIBLE);
                                ImageButtonArrayList.get(2).setOnClickListener(v -> openPatientProfileEdit(patientUids.get(2)));

                                patient3.getProfilePicFile(new DatabaseCallbacks() {
                                    @Override
                                    public void uriCallback(Uri uri) {}
                                    @Override
                                    public void fileCallback(File file) {
                                        Log.d(TAG, file.getName());
                                        ImageButtonArrayList.get(2).setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                                    }
                                    @Override
                                    public void progressCallback(double percentage) {}
                                    @Override
                                    public void failureCallback(boolean hasFailed, String message) {
                                        String emptyUri = "@drawable/empty";
                                        Drawable res = Drawable.createFromPath(emptyUri);
                                        ImageButtonArrayList.get(2).setImageDrawable(res);
                                    }
                                });
                            }
                        }
                    });
                } // end of if
                if (arrSize > 3) {
                    patient4 = new PatientDB(patientUids.get(3), new ServerCallback() {
                        @Override
                        public void isReadyCallback(boolean isReady) {
                            if (isReady) {
                                NamesArrayList.get(3).setText(patient4.getFirstName() + " " + patient4.getLastName());
                                NamesArrayList.get(3).setVisibility(View.VISIBLE);
                                ImageButtonArrayList.get(3).setVisibility(View.VISIBLE);
                                ImageButtonArrayList.get(3).setOnClickListener(v -> openPatientProfileEdit(patientUids.get(3)));

                                patient4.getProfilePicFile(new DatabaseCallbacks() {
                                    @Override
                                    public void uriCallback(Uri uri) {}
                                    @Override
                                    public void fileCallback(File file) {
                                        Log.d(TAG, file.getName());
                                        ImageButtonArrayList.get(3).setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                                    }
                                    @Override
                                    public void progressCallback(double percentage) {}
                                    @Override
                                    public void failureCallback(boolean hasFailed, String message) {
                                        String emptyUri = "@drawable/empty";
                                        Drawable res = Drawable.createFromPath(emptyUri);
                                        ImageButtonArrayList.get(3).setImageDrawable(res);
                                    }
                                });
                            }
                        }
                    });
                } // end of if
                if (arrSize > 4) {
                    patient5 = new PatientDB(patientUids.get(4), new ServerCallback() {
                        @Override
                        public void isReadyCallback(boolean isReady) {
                            if (isReady) {
                                NamesArrayList.get(4).setText(patient5.getFirstName() + " " + patient5.getLastName());
                                NamesArrayList.get(4).setVisibility(View.VISIBLE);
                                ImageButtonArrayList.get(4).setVisibility(View.VISIBLE);
                                ImageButtonArrayList.get(4).setOnClickListener(v -> openPatientProfileEdit(patientUids.get(4)));

                                patient5.getProfilePicFile(new DatabaseCallbacks() {
                                    @Override
                                    public void uriCallback(Uri uri) {}
                                    @Override
                                    public void fileCallback(File file) {
                                        Log.d(TAG, file.getName());
                                        ImageButtonArrayList.get(4).setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                                    }
                                    @Override
                                    public void progressCallback(double percentage) {}
                                    @Override
                                    public void failureCallback(boolean hasFailed, String message) {
                                        String emptyUri = "@drawable/empty";
                                        Drawable res = Drawable.createFromPath(emptyUri);
                                        ImageButtonArrayList.get(4).setImageDrawable(res);
                                    }
                                });
                            }
                        }
                    });
                } // end of if
                if (arrSize > 5) {
                    patient6 = new PatientDB(patientUids.get(5), new ServerCallback() {
                        @Override
                        public void isReadyCallback(boolean isReady) {
                            if (isReady) {
                                NamesArrayList.get(5).setText(patient6.getFirstName() + " " + patient6.getLastName());
                                NamesArrayList.get(5).setVisibility(View.VISIBLE);
                                ImageButtonArrayList.get(5).setVisibility(View.VISIBLE);
                                ImageButtonArrayList.get(5).setOnClickListener(v -> openPatientProfileEdit(patientUids.get(5)));

                                patient6.getProfilePicFile(new DatabaseCallbacks() {
                                    @Override
                                    public void uriCallback(Uri uri) {}
                                    @Override
                                    public void fileCallback(File file) {
                                        Log.d(TAG, file.getName());
                                        ImageButtonArrayList.get(5).setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                                    }
                                    @Override
                                    public void progressCallback(double percentage) {}
                                    @Override
                                    public void failureCallback(boolean hasFailed, String message) {
                                        String emptyUri = "@drawable/empty";
                                        Drawable res = Drawable.createFromPath(emptyUri);
                                        ImageButtonArrayList.get(5).setImageDrawable(res);
                                    }
                                });
                            }
                        }
                    });
                } // end of if
                // add one more for patientCreation
                if (arrSize != 6) {
                    NamesArrayList.get(arrSize).setVisibility(View.VISIBLE);
                    ImageButtonArrayList.get(arrSize).setVisibility(View.VISIBLE);
                }
            }
        });

    }


    public void openProfileCreation(String uid){
        Intent intent;
        intent = new Intent(this, patientRegister.class);
        intent.putExtra("adminUid", uid);
        startActivity(intent);
    }

    public void openPatientProfileEdit(String uid) {
        // TODO: Make an Activity based on ProfileCreation for "Register New Family" (to familyRegister form), and
        // "Manage Family" (display all 6 family, no action for now)
        Intent intent = new Intent(this, patientProfile.class);
        intent.putExtra("patientUid", uid);
        startActivity(intent);
    }
}