package com.example.momento.ui.login;

import com.example.momento.database.AccountType;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private AccountType accountType;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName, AccountType type) {
        this.displayName = displayName;
        this.accountType = type;
    }

    String getDisplayName() {
        return displayName;
    }

    AccountType getAccountType() { return accountType; }

}