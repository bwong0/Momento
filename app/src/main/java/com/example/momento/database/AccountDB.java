package com.example.momento.database;

import android.util.Log;
import android.util.Patterns;

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

import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

/**
 * Class for syncing with "Account" on Firebase.
 * <p>
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
 * </p>
 */

public class AccountDB {

    private final static String TAG = "AccountDB";

    // Constants matching the keys on Firebase
    public final static String ACCOUNT_NODE = "Accounts";
    public final static String ACCOUNT_TYPE = "accountType";
    public final static String FIRSTNAME = "firstName";
    public final static String LASTNAME = "lastName";
    public final static String EMAIL = "email";
    public final static String ADDRESS = "address";
    public final static String ISACTIVE = "isActive";

    private String uid;
    private String accType;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private Boolean isActive;

    protected DatabaseReference mDatabase;
    private final ValueEventListener accountListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Map and use the values to update the class
            Map<String, Object> info =  (Map<String, Object>) dataSnapshot.getValue();
            AccountDB.this.accType = info.get(ACCOUNT_TYPE).toString();
            AccountDB.this.firstName = info.get(FIRSTNAME).toString();
            AccountDB.this.lastName = info.get(LASTNAME).toString();
            AccountDB.this.email = info.get(EMAIL).toString();
            AccountDB.this.address = info.get(ADDRESS).toString();
            AccountDB.this.isActive = (Boolean) info.get(ISACTIVE);
            Log.d(TAG, "ValueEventListener called: " + info.toString());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Reading database failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    };

    public StorageDB storage;

    /* Constructors */

    /**
     * Constructor for AccountDB. Use this constructor when there is already an Account on Firebase.
     * If the account UID is not found on Firebase, an entry is created on the Database.
     * @param uid
     */
    public AccountDB(String uid) {
        this.uid = uid;
        // Initialize database reference point
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Get values from Firebase and update class members
        mDatabase.child(ACCOUNT_NODE).child(uid).get()
            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        // fetch value and store into a Map
                        DataSnapshot snapshot = task.getResult();
                        Map<String, Object> info = (Map<String, Object>) snapshot.getValue();
                        // if the UID is already in Firebase, fetch data members
                        if (snapshot.exists()) {
                            // update Class members after success
                            AccountDB.this.accType = info.get(ACCOUNT_TYPE).toString();
                            AccountDB.this.firstName = info.get(FIRSTNAME).toString();
                            AccountDB.this.lastName = info.get(LASTNAME).toString();
                            AccountDB.this.email = info.get(EMAIL).toString();
                            AccountDB.this.address = info.get(ADDRESS).toString();
                            AccountDB.this.isActive = (Boolean) info.get(ISACTIVE);
                            // Initiate Listener to the Account
                            mDatabase.child(ACCOUNT_NODE).child(uid).addValueEventListener(accountListener);
                            // Initiate StorageDB
                            storage = new StorageDB(AccountDB.this.uid);
                        } else {
                            // add the UID to Firebase and instantiate a blank Account?
                            // TODO: Decide on this with team.
//                            Map<String, Object> blankInfo = AccountDB.this.toMap();
//                            mDatabase.child(ACCOUNT_NODE).child(uid).setValue(blankInfo);
                        }
                    } else {
                        Log.d(TAG, "failed: " + String.valueOf(task.getResult().getValue()));
                    }
                }
            }
        );
    }


    /**
     * Constructor used to create a new AccountDB object, and update Firebase.
     * <p>
     *     Use to add a new account on Firebase. If the UID is already there, this constructor will update the fields on Firebase.
     * </p>
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
        info.put(ACCOUNT_TYPE, type.toString());
        info.put(FIRSTNAME, firstName);
        info.put(LASTNAME, lastName);
        info.put(EMAIL, email);
        info.put(ADDRESS, address);
        info.put(ISACTIVE, (Boolean) true);
        // Initialize database reference point
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // push to mDatabase.child("Accounts")
        mDatabase.child(ACCOUNT_NODE).child(uid).setValue(info)
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
                    // Initiate Listener to the Account
                    mDatabase.child(ACCOUNT_NODE).child(uid).addValueEventListener(accountListener);
                    // Initiate StorageDB
                    storage = new StorageDB(AccountDB.this.uid);
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
        info.put(ACCOUNT_TYPE, accType);
        info.put(FIRSTNAME, firstName);
        info.put(LASTNAME, lastName);
        info.put(EMAIL, email);
        info.put(ADDRESS, address);
        info.put(ISACTIVE, isActive);
        return info;
    }

    /* other method */

    /**
     * Removes this account from Firebase.
     */
    public void removeThisAccount() {
        mDatabase.child(ACCOUNT_NODE).child(uid).removeValue()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // update Class members after success
                AccountDB.this.accType = null;
                AccountDB.this.firstName = null;
                AccountDB.this.lastName = null;
                AccountDB.this.email = null;
                AccountDB.this.address = null;
                AccountDB.this.isActive = false;
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
     * Set Account's user type: ADMIN, PATIENT, or FAMILY, and update Firebase.
     * <p>
     *     Pre-Condition: Account must already be in "Accounts" on Firebase.
     *     Post-Condition: Data in the class is not updated until success callback from Firebase.
     * </p>
     * @param type AccountType Enum
     */
    public void setAccType(AccountType type) {
        mDatabase.child(ACCOUNT_NODE).child(uid).child(ACCOUNT_TYPE).setValue(type.toString())
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

    /**
     * Get first name of the Account.
     * @return First name
     */
    public String getFirstName() { return this.firstName; }

    /**
     * Set first name of the Account, and update Firebase.
     * <p>
     *     Pre-Condition: Account must already be in "Accounts" of Firebase.
     * </p>
     * @param firstName
     */
    public void setFirstName(String firstName) {
        mDatabase.child(ACCOUNT_NODE).child(uid).child(FIRSTNAME).setValue(firstName)
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

    /**
     * Get last name of the Account.
     * @return Last name.
     */
    public String getLastName() { return this.lastName; }

    /**
     * Set last name of the Account, and update Firebase.
     * <p>
     *     Pre-Condition: The Account must already be in "Accounts" on Firebase.
     * </p>
     * @param lastName
     */
    public void setLastName(String lastName) {
        AccountDB.this.lastName = lastName;
        mDatabase.child(ACCOUNT_NODE).child(uid).child(LASTNAME).setValue(lastName)
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

    /**
     * Get the email for the Account.
     * @return email String
     */
    public String getEmail() { return this.email; }

    /**
     * Set the email for the Account, and update Firebase.
     * @param email String
     * <p>
     *     Pre-Condition: The Account must already be in "Accounts" on Firebase.
     * </p>
     * @throws InvalidPropertiesFormatException
     */
    public void setEmail(String email) throws InvalidPropertiesFormatException {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mDatabase.child(ACCOUNT_NODE).child(uid).child(EMAIL).setValue(email)
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

    /**
     * Get the Address of the Account.
     * @return Address. String.
     */
    public String getAddress() { return this.address; }

    /**
     * Set the Address for the Account, and update Firebase.
     * <p>
     *     Pre-Condition: The Account must already be in "Accounts" on Firebase.
     * </p>
     * @param address
     */
    public void setAddress(String address) {
        mDatabase.child(ACCOUNT_NODE).child(uid).child(ADDRESS).setValue(address)
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

    /**
     * Get isActive flag of the Account.
     * @return Boolean.
     */
    public Boolean getIsActive() { return this.isActive; }

    /**
     * Set isActive flag for the Account, and update Firebase.
     * <p>
     *     Pre-Condition: The Account must already be in "Accounts" on Firebase.
     * </p>
     * @param boo
     */
    public void setIsActive(Boolean boo) {
        mDatabase.child(ACCOUNT_NODE).child(uid).child(ISACTIVE).setValue(boo)
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
