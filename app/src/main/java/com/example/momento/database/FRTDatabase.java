package com.example.momento.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FRTDatabase {

    private static final String TAG = "FRTDatabase";

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FRTDatabase() {
        this.database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference("test");

        this.myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Listener: Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Listener: Failed to read value.", error.toException());
            }
        });
    }

    public void setTestValue(String value) {
        myRef.child("test").setValue(value);
        Log.v(TAG, "setTestValue(): " + value);
    }

    public void getTestValue() {
        myRef.child("test").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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


//        FRTDatabase testDatabase = new FRTDatabase();
//
//        testDatabase.getTestValue(); // Print the current value in the database. Expect "testValue"
//        testDatabase.setTestValue("testValue2");
//        testDatabase.getTestValue(); // Print the current value in the database. Expect "testValue2"


}
