package com.example.momento.database;

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




}
