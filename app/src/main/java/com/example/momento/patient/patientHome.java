package com.example.momento.patient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.momento.database.DatabaseCallbacks;
import com.example.momento.database.FamilyDB;
import com.example.momento.database.PatientDB;
import com.example.momento.database.ServerCallback;

import androidx.appcompat.app.AppCompatActivity;

import com.example.momento.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class patientHome extends AppCompatActivity {

    private static final String TAG = "PatientHome";

    // UI Components
    TextView famName1;
    TextView famName2;
    TextView famName3;
    TextView famName4;
    TextView famName5;
    TextView famName6;
    ArrayList<TextView> famNameArr;

    ImageButton famProfile1;
    ImageButton famProfile2;
    ImageButton famProfile3;
    ImageButton famProfile4;
    ImageButton famProfile5;
    ImageButton famProfile6;
    ArrayList<ImageButton> famProfileArr;

    final String emptyUri = "@drawable/empty";
    Drawable res;

    // Server
    String patientUid;
    PatientDB patientDb;
    private List<String> familyUids;
    FamilyDB famDb1;
    FamilyDB famDb2;
    FamilyDB famDb3;
    FamilyDB famDb4;
    FamilyDB famDb5;
    FamilyDB famDb6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

        patientUid = getIntent().getStringExtra("uid");
        res = Drawable.createFromPath(emptyUri);

        // binding Textview under profilePic to a variable (default: "Create New Patient")
        famName1 = (TextView) findViewById(R.id.patientName1);
        famName2 = (TextView) findViewById(R.id.patientName2);
        famName3 = (TextView) findViewById(R.id.patientName3);
        famName4 = (TextView) findViewById(R.id.patientName4);
        famName5 = (TextView) findViewById(R.id.patientName5);
        famName6 = (TextView) findViewById(R.id.patientName6);
        famNameArr = new ArrayList<>();
        famNameArr.add(famName1);
        famNameArr.add(famName2);
        famNameArr.add(famName3);
        famNameArr.add(famName4);
        famNameArr.add(famName5);
        famNameArr.add(famName6);

        // binding ImageButton profilePic (default: @drawable/empty)
        famProfile1 = (ImageButton) findViewById(R.id.patientProfile1);
        famProfile2 = (ImageButton) findViewById(R.id.patientProfile2);
        famProfile3 = (ImageButton) findViewById(R.id.patientProfile3);
        famProfile4 = (ImageButton) findViewById(R.id.patientProfile4);
        famProfile5 = (ImageButton) findViewById(R.id.patientProfile5);
        famProfile6 = (ImageButton) findViewById(R.id.patientProfile6);
        famProfileArr = new ArrayList<>();
        famProfileArr.add(famProfile1);
        famProfileArr.add(famProfile2);
        famProfileArr.add(famProfile3);
        famProfileArr.add(famProfile4);
        famProfileArr.add(famProfile5);
        famProfileArr.add(famProfile6);

        for (TextView fam : famNameArr) {
            fam.setVisibility(View.INVISIBLE);
        }
        for (ImageButton famPic : famProfileArr) {
            famPic.setVisibility(View.INVISIBLE);
        }
    } // End of onCreate()

    @Override
    protected void onResume() {
        super.onResume();

        patientDb = new PatientDB(patientUid, new ServerCallback() {
            @Override
            public void isReadyCallback(boolean isReady) {
                if (isReady) {
                    familyUids = patientDb.getFamilyList();
                    int arrSize = familyUids.size();
                    if (arrSize > 0) {
                        famDb1 = new FamilyDB(familyUids.get(0), new ServerCallback() {
                            @Override
                            public void isReadyCallback(boolean isReady) {
                                if (isReady) {
                                    famNameArr.get(0).setText(
                                            famDb1.getFirstName() + " " + famDb1.getLastName()
                                    );
                                    famNameArr.get(0).setVisibility(View.VISIBLE);
                                    famProfileArr.get(0).setVisibility(View.VISIBLE);
                                    famProfileArr.get(0).setOnClickListener(v -> openFamily(familyUids.get(0)));

                                    famDb1.getProfilePicFile(new DatabaseCallbacks() {
                                        @Override
                                        public void uriCallback(Uri uri) {}
                                        @Override
                                        public void fileCallback(File file) {
                                            famProfileArr.get(0).setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                                        }
                                        @Override
                                        public void progressCallback(double percentage) {}
                                        @Override
                                        public void failureCallback(boolean hasFailed, String message) {
                                            famProfileArr.get(0).setImageDrawable(res);
                                        }
                                    });
                                }
                            }
                        });
                    } // end of if arrSize
                    if (arrSize > 1) {
                        famDb2 = new FamilyDB(familyUids.get(1), new ServerCallback() {
                            @Override
                            public void isReadyCallback(boolean isReady) {
                                if (isReady) {
                                    famNameArr.get(1).setText(
                                            famDb2.getFirstName() + " " + famDb2.getLastName()
                                    );
                                    famNameArr.get(1).setVisibility(View.VISIBLE);
                                    famProfileArr.get(1).setVisibility(View.VISIBLE);
                                    famProfileArr.get(1).setOnClickListener(v -> openFamily(familyUids.get(1)));

                                    famDb2.getProfilePicFile(new DatabaseCallbacks() {
                                        @Override
                                        public void uriCallback(Uri uri) {}
                                        @Override
                                        public void fileCallback(File file) {
                                            famProfileArr.get(1).setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                                        }
                                        @Override
                                        public void progressCallback(double percentage) {}
                                        @Override
                                        public void failureCallback(boolean hasFailed, String message) {
                                            famProfileArr.get(1).setImageDrawable(res);
                                        }
                                    });
                                }
                            }
                        });
                    } // end of if arrSize
                    if (arrSize > 2) {
                        famDb3 = new FamilyDB(familyUids.get(2), new ServerCallback() {
                            @Override
                            public void isReadyCallback(boolean isReady) {
                                if (isReady) {
                                    famNameArr.get(2).setText(
                                            famDb3.getFirstName() + " " + famDb3.getLastName()
                                    );
                                    famNameArr.get(2).setVisibility(View.VISIBLE);
                                    famProfileArr.get(2).setVisibility(View.VISIBLE);
                                    famProfileArr.get(2).setOnClickListener(v -> openFamily(familyUids.get(2)));

                                    famDb3.getProfilePicFile(new DatabaseCallbacks() {
                                        @Override
                                        public void uriCallback(Uri uri) {}
                                        @Override
                                        public void fileCallback(File file) {
                                            famProfileArr.get(2).setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                                        }
                                        @Override
                                        public void progressCallback(double percentage) {}
                                        @Override
                                        public void failureCallback(boolean hasFailed, String message) {
                                            famProfileArr.get(2).setImageDrawable(res);
                                        }
                                    });
                                }
                            }
                        });
                    } // end of if arrSize
                    if (arrSize > 3) {
                        famDb4 = new FamilyDB(familyUids.get(3), new ServerCallback() {
                            @Override
                            public void isReadyCallback(boolean isReady) {
                                if (isReady) {
                                    famNameArr.get(3).setText(
                                            famDb4.getFirstName() + " " + famDb4.getLastName()
                                    );
                                    famNameArr.get(3).setVisibility(View.VISIBLE);
                                    famProfileArr.get(3).setVisibility(View.VISIBLE);
                                    famProfileArr.get(3).setOnClickListener(v -> openFamily(familyUids.get(3)));

                                    famDb4.getProfilePicFile(new DatabaseCallbacks() {
                                        @Override
                                        public void uriCallback(Uri uri) {}
                                        @Override
                                        public void fileCallback(File file) {
                                            famProfileArr.get(3).setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                                        }
                                        @Override
                                        public void progressCallback(double percentage) {}
                                        @Override
                                        public void failureCallback(boolean hasFailed, String message) {
                                            famProfileArr.get(3).setImageDrawable(res);
                                        }
                                    });
                                }
                            }
                        });
                    } // end of if arrSize
                    if (arrSize > 4) {
                        famDb5 = new FamilyDB(familyUids.get(4), new ServerCallback() {
                            @Override
                            public void isReadyCallback(boolean isReady) {
                                if (isReady) {
                                    famNameArr.get(4).setText(
                                            famDb5.getFirstName() + " " + famDb5.getLastName()
                                    );
                                    famNameArr.get(4).setVisibility(View.VISIBLE);
                                    famProfileArr.get(4).setVisibility(View.VISIBLE);
                                    famProfileArr.get(4).setOnClickListener(v -> openFamily(familyUids.get(4)));

                                    famDb5.getProfilePicFile(new DatabaseCallbacks() {
                                        @Override
                                        public void uriCallback(Uri uri) {}
                                        @Override
                                        public void fileCallback(File file) {
                                            famProfileArr.get(4).setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                                        }
                                        @Override
                                        public void progressCallback(double percentage) {}
                                        @Override
                                        public void failureCallback(boolean hasFailed, String message) {
                                            famProfileArr.get(4).setImageDrawable(res);
                                        }
                                    });
                                }
                            }
                        });
                    } // end of if arrSize
                    if (arrSize > 5) {
                        famDb6 = new FamilyDB(familyUids.get(5), new ServerCallback() {
                            @Override
                            public void isReadyCallback(boolean isReady) {
                                if (isReady) {
                                    famNameArr.get(5).setText(
                                            famDb6.getFirstName() + " " + famDb6.getLastName()
                                    );
                                    famNameArr.get(5).setVisibility(View.VISIBLE);
                                    famProfileArr.get(5).setVisibility(View.VISIBLE);
                                    famProfileArr.get(5).setOnClickListener(v -> openFamily(familyUids.get(5)));

                                    famDb6.getProfilePicFile(new DatabaseCallbacks() {
                                        @Override
                                        public void uriCallback(Uri uri) {}
                                        @Override
                                        public void fileCallback(File file) {
                                            famProfileArr.get(5).setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                                        }
                                        @Override
                                        public void progressCallback(double percentage) {}
                                        @Override
                                        public void failureCallback(boolean hasFailed, String message) {
                                            famProfileArr.get(5).setImageDrawable(res);
                                        }
                                    });
                                }
                            }
                        });
                    } // end of if arrSize
                }
            }
        });
    } // End of onResume()

    public void openFamily(String uid) {
        Intent intent = new Intent(this, profiles.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }
}