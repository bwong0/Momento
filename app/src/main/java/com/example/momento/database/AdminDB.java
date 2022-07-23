package com.example.momento.database;

import java.util.ArrayList;
import java.util.List;

public class AdminDB extends AccountDB {

    private List<String> patientList;
    private final int MAX_PATIENT_COUNT = 6;

    /**
     * Constructor for AdminDB Class.
     * @param uid Should match UID for the account on Firebase Authentication.
     * @param type AccountType Enum.
     * @param firstName
     * @param lastName
     * @param email
     * @param address
     */
    public AdminDB(String uid, AccountType type, String firstName, String lastName,
                   String email, String address) {
        super(uid, type, firstName, lastName, email, address);
        patientList = new ArrayList<>();
    }

    /**
     * Get the list of patient UIDs.
     * @return A String array of patients.
     */
    public String[] getPatientList() {
        return patientList.toArray(new String[6]);
    }

    /**
     * Add a patient UID
     * @param patientUid
     */
    public void addPatient(String patientUid) {
        if (patientList.size() < MAX_PATIENT_COUNT && !(patientUid.contains(patientUid))) {
            patientList.add(patientUid);
            // TODO: update Firebase
        } else {
            // TODO: implement list full logic
        }
    }

    public void removePatient(String patientUid) {
        patientList.remove(patientUid);
        // TODO: update Firebase
    }

}
