package com.example.momento.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    }

    /**
     * Setter for accType.
     */
    public void setAccType(AccountType type) {
        switch (type) {
            case ADMIN:
                this.accType = "ADMIN";
                break;
            case PATIENT:
                this.accType = "PATIENT";
                break;
            case FAMILY:
                this.accType = "FAMILY";
                break;
        }
    }

    /**
     * Setter for isActive.
     */
    public void setIsActive(Boolean boo) {
        this.isActive = boo;
    }

}
