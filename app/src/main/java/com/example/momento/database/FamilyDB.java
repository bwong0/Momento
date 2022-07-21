package com.example.momento.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FamilyDB {


    private static final String TAG = "FamilyDB";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String uid;


    public void familyDB(String family_uid) {
        this.uid = family_uid;
    }
    public static class family_member {

        public String family_member_firstname;
        public String family_member_lastname;


        public family_member() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }
        public family_member(String family_member_firstname,String family_member_lastname) {
            this.family_member_firstname = family_member_firstname;
            this.family_member_lastname = family_member_lastname;
        }

    }


    public void writeNew_family_members(String family_member_firstname,String family_member_lastname, String family_uid) {

        family_member family_member = new family_member(family_member_firstname,family_member_lastname);
        myRef.child("families").child(family_uid).setValue(family_member);
    }

    public void getFamilyValue() {
        myRef.child("families").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                }
            }
        });
    }






}
