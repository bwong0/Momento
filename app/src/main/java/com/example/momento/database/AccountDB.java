package com.example.momento.database;

import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.InvalidPropertiesFormatException;
import java.util.regex.Pattern;

public class AccountDB {

    // TODO: make getter/setter for each field. Updates database.
    private String uid;
    private String accType;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private Boolean isActive;
    private DatabaseReference mDatabase;

    public AccountDB() {
       // Default constructor required for calls to DataSnapshot.getValue(AccountDB.class)
    }

    /**
     * Constructor for AccountDB Class.
     * @param uid Should match UID from Firebase Authentication. Will be used as primary key.
     * @param type Should use ENUM from AccountType
     * @param firstName
     * @param lastName
     * @param email
     * @param address
     */
    public AccountDB(String uid, AccountType type, String firstName, String lastName,
              String email, String address) {
        // Initialize class data members
        this.uid = uid;
        setAccType(type);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.isActive = true;
        // Initialize database reference point
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // TODO: toMap and push to mDatabase.child("Accounts")
    }


    /**
     * Get Account's UID, or primary key.
     * @return String of UID.
     */
    public String getUid() { return this.uid; }
    // No setter allowed for uid.

    /**
     * Get Account's user type: ADMIN, PATIENT, or FAMILY.
     * @return Enum AccountType or null.
     */
    public AccountType getAccType() {
        return AccountType.valueOf(this.accType);
    }
    /**
     * Set Account's user type: ADMIN, PATIENT, or FAMILY.
     * Data in the class is not updated until success callback.
     */
    public void setAccType(AccountType type) {
        mDatabase.child("Accounts").child(uid).child("accountType").setValue(type.toString())
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    AccountDB.this.accType = type.toString();
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

    public String getFirstName() { return this.firstName; }
    public void setFirstName(String firstName) {
        mDatabase.child("Accounts").child(uid).child("firstName").setValue(firstName)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    AccountDB.this.firstName = firstName;
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

    public String getLastName() { return this.lastName; }
    public void setLastName(String lastName) {
        AccountDB.this.lastName = lastName;
        mDatabase.child("Accounts").child(uid).child("lastName").setValue(lastName)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    AccountDB.this.lastName = lastName;
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

    public String getEmail() { return this.email; }
    public void setEmail(String email) throws InvalidPropertiesFormatException {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mDatabase.child("Accounts").child(uid).child("email").setValue(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AccountDB.this.email = email;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });
        } else {
            throw new InvalidPropertiesFormatException("Invalid email address.");
        }
    }

    public String getAddress() { return this.address; }
    public void setAddress(String address) {
        mDatabase.child("Accounts").child(uid).child("address").setValue(address)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AccountDB.this.address = address;
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

    public Boolean getIsActive() { return this.isActive; }
    public void setIsActive(Boolean boo) {
        mDatabase.child("Accounts").child(uid).child("isActive").setValue(boo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AccountDB.this.isActive = boo;
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


}
