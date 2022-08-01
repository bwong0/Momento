package com.example.momento.database;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class for syncing "Patient" values from Firebase.
 * <p>
 *     Note, Date can only be stored as String on Firebase.
 *     Structure on Firebase:
 *     patient_uid: {
 *         "healthCardNum" : String,
 *         "birthDate" : String,
 *         "dateLastOpened" : String,
 *         "usageStreak" : Long,
 *         "familyList: List<String>
 *     }
 * </p>
 */
public class PatientDB extends AccountDB {

    private static final String TAG = "PatientDB";

    // Constants matching the keys in Firebase
    public final String PATIENT_NODE = "Patients";
    private static final String HEALTHCARDNUM = "healthCardNum";
    private static final String BIRTHDATE = "birthDate";
    private static final String FAMILYLIST = "familyList";
    private static final String DATELASTOPENED = "dateLastOpened";
    private static final String USAGESTREAK = "usageStreak";

    private final int MAX_NUM_FAMILIES = 6;
    private List<String> familyList = new ArrayList<>(MAX_NUM_FAMILIES);
    private String healthCardNum;
    private Date birthDate; // see Date, DateFormat class
    private DateFormat df = DateFormat.getDateInstance(); // Date formatter class
    private Date dateLastOpened;
    private int usageStreak; // TODO: implement usage logic

    private final ValueEventListener patientListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            usePatientSnapshot(snapshot);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            // Reading database failed, log a message
            Log.w(TAG, "loadPost:onCancelled", error.toException());
        }
    };


    /* Constructors */

    /**
     * Constructor for PatientDB. Use this during Login.
     * Use this constructor when there is already an Account on Firebase.
     * @param uid Should match UID for the account on Firebase Authentication.
     */
    public PatientDB(String uid, ServerCallback cb) {
        // Retrieve "Account" info
        super(uid);
        // Check if "Patient" entry exists, if so, update class members
        DatabaseReference thisPatientRef = mDatabase.child(PATIENT_NODE).child(this.getUid());
        thisPatientRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        // fetch value and store into a Map
                        DataSnapshot snapshot = task.getResult();
                        // if the UID is already in Firebase, fetch data members
                            if (snapshot.exists()) {
                                // update Class members after success
                                usePatientSnapshot(snapshot);
                                // Initiate Listener to the Account
                                thisPatientRef.addValueEventListener(patientListener);
                                cb.isReadyCallback(true);
                            } else {
                                // No Patient entry in Realtime Database
                                cb.isReadyCallback(false);
                            }
                    } else {
                        Log.d(TAG, "failed: " + String.valueOf(task.getResult().getValue()));
                        cb.isReadyCallback(false);
                    }
                }
            });
    }
    // reference should point to the specific patient (information from LoggedInUser.java)

    /**
     * Constructor for FamilyDB Class. Use this during Registration.
     * @param uid Should match UID for the account on Firebase Authentication.
     * @param type AccountType Enum.
     * @param firstName
     * @param lastName
     * @param email
     * @param address
     * @param healthCardNum
     * @param birthDate
     */
    public PatientDB(String uid, AccountType type, String firstName, String lastName,
                    String email, String address, String healthCardNum, Date birthDate,
                    ServerCallback cb) {
        // Creates an entry in "Accounts" on Firebase
        super(uid, type, firstName, lastName, email, address);
        // Creates a Map entry of "Patient"
        Map<String,Object> patient = new HashMap<>();
        patient.put(HEALTHCARDNUM, healthCardNum);
        patient.put(BIRTHDATE, df.format(birthDate));
        Date now = new Date();
        patient.put(DATELASTOPENED, df.format(now));
        patient.put(FAMILYLIST, familyList);
//        patient.put("usageStreak", ); // TODO: usage streak
        // Upload Map to Firebase
        DatabaseReference thisPatientRef = mDatabase.child(PATIENT_NODE).child(this.getUid());
        thisPatientRef.setValue(patient)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Initiate Listener
                    thisPatientRef.addValueEventListener(patientListener);
                    cb.isReadyCallback(true);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Write failed
                    // TODO: Handle write failure?
                    cb.isReadyCallback(false);
                }
            });
    }

    /* Methods */

    /**
     * Get the list of UIDs associated with Family accounts.
     * @return An ArrayList<String>
     */
    public List<String> getFamilyList() { return this.familyList; }

    /**
     * Add a Family account's UID to this Patient.
     * <p>
     *     Pre-condition: This Patient's UID must be in Firebase.Patients
     * </p>
     * @param familyUid
     */
    public void addFamily(String familyUid) {
        if (familyList.size() < MAX_NUM_FAMILIES && !(familyList.contains(familyUid))) {
            familyList.add(familyUid);
            // Update Firebase
            DatabaseReference thisPatientRef = mDatabase.child(PATIENT_NODE).child(this.getUid());
            thisPatientRef.child(FAMILYLIST).setValue(familyList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Re-initiate Listener to the Patient to be sure (maybe not necessary
                        // because we have other fields that keep this entry alive on Firebase
                        // even when "familyList" is empty.
                        thisPatientRef.addValueEventListener(patientListener);
                        Log.d(TAG, "added family to list successfully.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Database write failed. Revert changes.
                        PatientDB.this.familyList.remove(familyUid);
                    }
                });
        } else if (familyList.contains(familyUid)) {
            Log.w(TAG, "Family " + familyUid + "already in the List.");
        } else if (familyList.size() == MAX_NUM_FAMILIES) {
            // TODO: implement list full logic
            Log.w(TAG, "familyList is full.");
        }
    }

    /**
     * Remove a Family UID from this Patient's family list. Update Firebase if successful.
     * @param familyUid
     */
    public void removeFamily(String familyUid) {
        if (this.familyList.remove(familyUid)) {
            // Update Firebase if uid in the List and removed
            DatabaseReference thisPatientRef = mDatabase.child(PATIENT_NODE).child(this.getUid());
            thisPatientRef.child(FAMILYLIST).setValue(familyList)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Database update failed. Revert local changes.
                        PatientDB.this.familyList.add(familyUid);
                    }
                });
        }
    }

    /**
     * Get Patient's health card number.
     * @return Patient's health card number as String.
     */
    public String getHealthCardNum() { return this.healthCardNum; }

    /**
     * Set Patient's health card number. Update Firebase.
     * @param cardNum Health card number as String.
     */
    public void setHealthCardNum(String cardNum) {
        DatabaseReference thisPatientRef = mDatabase.child(PATIENT_NODE).child(this.getUid());
        thisPatientRef.child(HEALTHCARDNUM).setValue(cardNum)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    PatientDB.this.healthCardNum = cardNum;
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Write failed
                    // ...
                }
            });
    }

    /**
     * Get Patient's birth date as Date.
     * @return Patient's birth date as Date.
     */
    public Date getBirthDate() { return this.birthDate; }

    /**
     * Set Patient's birth date. Update Firebase.
     * @param aDate
     */
    public void setBirthDate(Date aDate) {
        String dateString = df.format(aDate);
        DatabaseReference thisPatientRef = mDatabase.child(PATIENT_NODE).child(this.getUid());
        thisPatientRef.child(BIRTHDATE).setValue(dateString)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    PatientDB.this.birthDate = aDate;
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Write failed
                    // ...
                }
            });
    }

    /**
     * Get Date when this patient's UID was used.
     * @return A Date object.
     */
    public Date getDateLastOpened() { return this.dateLastOpened; }

    /**
     * Set Patient's date last opened the app. Update Firebase.
     * @param aDate
     */
    public void setDateLastOpened(Date aDate) {
        String dateString = df.format(aDate);
        DatabaseReference thisPatientRef = mDatabase.child(PATIENT_NODE).child(this.getUid());
        thisPatientRef.child(DATELASTOPENED).setValue(dateString)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    PatientDB.this.dateLastOpened = aDate;
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Write failed
                    // ...
                }
            });
    }

    /**
     * Helper function to parse DataSnapshot from Firebase to update class memebers.
     * @param snapshot
     */
    private void usePatientSnapshot (DataSnapshot snapshot) {
        Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
        PatientDB.this.healthCardNum = (String) data.get(HEALTHCARDNUM);
        try {
            PatientDB.this.birthDate = df.parse((String) data.get(BIRTHDATE));
            PatientDB.this.dateLastOpened = df.parse((String) data.get(DATELASTOPENED));
        } catch (ParseException e) {
            Log.e(TAG, "DateFormat ParseException: Parsing date String from Firebase.");
        }
//      TODO:  PatientDB.this.usageStreak = (int) data.get(USAGESTREAK);
        PatientDB.this.familyList = (List<String>) data.get(FAMILYLIST);
    }


    /**
     * Set an Account's Profile Picture.
     * @param context Usually "this", Activity or Service
     * @param fileUri Uri of the file to be uploaded.
     * @param cb Callback after completing uploadTask. uriCallback, failureCallback, progressCallback.
     */
    public void uploadProfilePic(Context context, Uri fileUri, DatabaseCallbacks cb) {
        this.storage.uploadProfilePic(context, fileUri, new DatabaseCallbacks() {
            @Override
            public void uriCallback(Uri uri) {
                cb.uriCallback(uri);
            }
            @Override
            public void fileCallback(File file) { }
            @Override
            public void progressCallback(double percentage) {
                cb.progressCallback(percentage);
            }
            @Override
            public void failureCallback(boolean hasFailed, String message) {
                cb.failureCallback(hasFailed, message);
            }
        });
    }

    /**
     * Function calls to download profile picture from Database. A <File> object is returned via callback.
     * @param cb fileCallback, failureCallback implemented.
     */
    public void getProfilePicFile(DatabaseCallbacks cb) {
        this.storage.downloadProfilePic(new DatabaseCallbacks() {
            @Override
            public void uriCallback(Uri uri) { }
            @Override
            public void fileCallback(File file) {
                cb.fileCallback(file); // downloaded File object
            }
            @Override
            public void progressCallback(double percentage) { }
            @Override
            public void failureCallback(boolean hasFailed, String message) {
                cb.failureCallback(hasFailed, message);
            }
        });
    }

    /**
     * Function calls to get profile picture uri from Database. A <Uri> object is returned via callback.
     * @param cb uriCallback, failureCallback implemented.
     */
    public void getProfilePicUri(DatabaseCallbacks cb) {
        this.storage.getProfilePicDownloadUri(new DatabaseCallbacks() {
            @Override
            public void uriCallback(Uri uri) {
                cb.uriCallback(uri);
            }
            @Override
            public void fileCallback(File file) { }
            @Override
            public void progressCallback(double percentage) { }
            @Override
            public void failureCallback(boolean hasFailed, String message) {
                cb.failureCallback(hasFailed, message);
            }
        });
    }


}
