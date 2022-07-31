package com.example.momento.database;


import android.net.Uri;

import java.io.File;

/**
 * Callback functions to be invoked within Firebase Listeners.
 */
public interface DatabaseCallbacks {
    void uriCallback(Uri uri);
    void fileCallback(File file);
    void progressCallback(double percentage);
    void failureCallback(boolean hasFailed, String message);
}

