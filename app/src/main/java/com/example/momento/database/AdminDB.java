package com.example.momento.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDB extends AccountDB {

    private final String TAG = "AdminDB";

    private final int MAX_PATIENT_COUNT = 6;
    private List<String> patientList = new ArrayList<>(MAX_PATIENT_COUNT);

    private final String ADMIN_NODE = "Admins";
    private final ValueEventListener adminListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Map<String, List<String>> data = (Map<String, List<String>>) snapshot.getValue();
            AdminDB.this.patientList = data.get("patientList");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            // Reading database failed, log a message
            Log.w(TAG, "loadPost:onCancelled", error.toException());
        }
    };


    /* Constructors */

    /**
     * Constructor for AdminDB. Use this during Login.
     * Use this constructor when there is already an Account on Firebase.
     * @param uid Should match UID for the account on Firebase Authentication.
     */
    public AdminDB(String uid) {
        // Retrieve "Account" info
        super(uid);
        // Check if "Admin" entry exists, if so, update patientList
        mDatabase.child(ADMIN_NODE).child(this.getUid()).get()
            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        // fetch value and store into a Map
                        DataSnapshot snapshot = task.getResult();
                        // if the UID is already in Firebase, fetch data members
                        if (snapshot.exists()) {
                            // update Class members after success
                            Map<String, List<String>> data = (Map<String, List<String>>) snapshot.getValue();
                            AdminDB.this.patientList = data.get("patientList");
                            // Initiate Listener to the Account
                            mDatabase.child(ADMIN_NODE).child(uid).addValueEventListener(adminListener);
                        } else {
                            // add the UID to Firebase and instantiate a blank Admin?
                            // TODO: Decide on this with team.
                            // Map<String, Object> blankInfo =
                            // mDatabase.child(ADMIN_NODE).child(uid).setValue(blankInfo);
                        }
                    } else {
                        Log.d(TAG, "failed: " + String.valueOf(task.getResult().getValue()));
                    }
                }
                }
            );
    }


    /**
     * Constructor for AdminDB Class. Use this during Registration.
     * @param uid Should match UID for the account on Firebase Authentication.
     * @param type AccountType Enum.
     * @param firstName
     * @param lastName
     * @param email
     * @param address
     */
    public AdminDB(String uid, AccountType type, String firstName, String lastName,
                   String email, String address) {
        // Creates an entry in "Accounts" on Firebase
        super(uid, type, firstName, lastName, email, address);
        // Creates an entry in "Admins" on Firebase
        Map<String, List<String>> patients = new HashMap<>();
        patients.put("patientList", this.patientList);
        mDatabase.child(ADMIN_NODE).child(this.getUid()).setValue(patients)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Initiate Listener
                        mDatabase.child(ADMIN_NODE).child(uid).addValueEventListener(adminListener);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // TODO: Handle write failure?
                    }
                });
    }


    /* Methods */

    /**
     * Get the list of patient UIDs.
     * @return A List<String> array of patients.
     */
    public List<String> getPatientList() {
        return patientList;
    }

    /**
     * Add a patient UID and update Firebase.
     * @param patientUid
     */
    public void addPatient(String patientUid) {
        if (patientList.size() < MAX_PATIENT_COUNT && !(patientList.contains(patientUid))) {
            patientList.add(patientUid);
            // Update Firebase
            Map<String, List<String>> patients = new HashMap<>();
            patients.put("patientList", patientList);
            mDatabase.child(ADMIN_NODE).child(this.getUid()).setValue(patients)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Initiate Listener to the Admin
                        if (patientList.size() == 1) {
                            mDatabase.child(ADMIN_NODE).child(AdminDB.this.getUid())
                                .addValueEventListener(adminListener);
                        }
                        Log.d(TAG, "added patient successfully.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed, revert changes
                        AdminDB.this.patientList.remove(patientUid);
                        Log.d(TAG, "failed to addPatient.");
                    }
                });
        } else {
            // TODO: implement list full logic
            Log.d(TAG, "addPatient fail if bracket");
        }
    }

    /**
     * Remove a patient UID and update Firebase.
     * @param patientUid
     */
    public void removePatient(String patientUid) {
        this.patientList.remove(patientUid);
        // Update Firebase
        Map<String, List<String>> patients = new HashMap<>();
        patients.put("patientList", this.patientList);
        mDatabase.child(ADMIN_NODE).child(this.getUid()).setValue(patients)
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Write failed, revert changes
                    AdminDB.this.patientList.add(patientUid);
                }
            });
    }
}
