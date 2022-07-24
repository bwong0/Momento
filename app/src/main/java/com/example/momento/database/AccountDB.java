package com.example.momento.database;

import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

/**
 * The Structure on Firebase Database is
 * Accounts : {
 *     uid : {
 *         firstName : String
 *         lastName : String
 *         email : String
 *         address : String
 *         isActive : Boolean
 *     },
 *     uid : { ... },
 * }
 */

public class AccountDB {

    private String uid;
    private String accType;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private Boolean isActive;

    private final String ACCOUNT_NODE = "Accounts";
    private DatabaseReference mDatabase;

    private AccountDB() {
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
        // Organize the children nodes into a map
        Map<String, Object> info = new HashMap<>();
        info.put("accountType", type.toString());
        info.put("firstName", firstName);
        info.put("lastName", lastName);
        info.put("email", email);
        info.put("address", address);
        info.put("isActive", (Boolean) true);
        // Initialize database reference point
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // push to mDatabase.child("Accounts")
        mDatabase.child(ACCOUNT_NODE).child(this.uid).setValue(info)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // update Class members after success
                    AccountDB.this.accType = type.toString();
                    AccountDB.this.firstName = firstName;
                    AccountDB.this.lastName = lastName;
                    AccountDB.this.email = email;
                    AccountDB.this.address = address;
                    AccountDB.this.isActive = true;
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
     * Convert AccountDB class information into a HashMap<String, Object>
     * @return Hashmap of all the data members that need to be on Firebase.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> info = new HashMap<>();
        info.put("accountType", accType);
        info.put("firstName", firstName);
        info.put("lastName", lastName);
        info.put("email", email);
        info.put("address", address);
        info.put("isActive", isActive);
        return info;
    }


    /* Getters and Setters */

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
        mDatabase.child(ACCOUNT_NODE).child(uid).child("accountType").setValue(type.toString())
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
        mDatabase.child(ACCOUNT_NODE).child(uid).child("firstName").setValue(firstName)
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
        mDatabase.child(ACCOUNT_NODE).child(uid).child("lastName").setValue(lastName)
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
            mDatabase.child(ACCOUNT_NODE).child(uid).child("email").setValue(email)
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
        mDatabase.child(ACCOUNT_NODE).child(uid).child("address").setValue(address)
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
        mDatabase.child(ACCOUNT_NODE).child(uid).child("isActive").setValue(boo)
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
