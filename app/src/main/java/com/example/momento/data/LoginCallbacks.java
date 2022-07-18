package com.example.momento.data;

import com.example.momento.data.model.LoggedInUser;

/**
 * Callback functions to be invoked within Firebase functions.
 */
public interface LoginCallbacks {
    void onLogin(Result<LoggedInUser> result);
}
