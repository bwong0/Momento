package com.example.momento.data.model;

import com.example.momento.database.AccountDB;
import com.example.momento.database.AccountType;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {
    // TODO: Add fields for each account (email, firstName, lastName, etc.) For app to recognize who is logged in.
    private String userId;
    private String displayName;
    private AccountDB currentAccount;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
        this.currentAccount = new AccountDB(userId);
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public AccountType getAccountType() { return this.currentAccount.getAccType();}
}