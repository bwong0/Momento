package com.example.momento.database;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.content.ContentResolver;

import androidx.annotation.NonNull;
import androidx.core.content.MimeTypeFilter;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

/**
 * Class for accessing Firebase Storage
 * <p>
 * Structure:
 * {
 *     uid : {
 *         profilePic : <IMAGE>,
 *         vid1 : <VIDEO>,
 *         vid2 : <VIDEO>,
 *         vid3 : <VIDEO>
 *     },
 *     uid : { ... }
 * }
 *
 * Reference paths and names can contain any sequence of valid Unicode characters, but certain restrictions are imposed including:
 * * Total length of reference.fullPath must be between 1 and 1024 bytes when UTF-8 encoded.
 * * No Carriage Return or Line Feed characters.
 * * Avoid using #, [, ], *, or ?, as these do not work well with other tools such as the Firebase Realtime Database or gsutil.
 * </p>
 */
public class StorageDB {

    private static final String TAG = "StorageDB";

    /* Constants */
    private static final String PROFILE_PIC = "profilePic";
    private static final String VIDEO_1 = "vid1";
    private static final String VIDEO_2 = "vid2";
    private static final String VIDEO_3 = "vid3";
    private static final long PROFILE_PIC_SIZE_LIMIT = 5000000; // 5MB
    private static final long VID_SIZE_LIMIT = 1000000000 ; // 1GB

    /* Class Members */
    protected Uri profilePicDownloadUri;
    protected Uri vid1DownloadUri;
    protected Uri vid2DownloadUri;
    protected Uri vid3DownloadUri;

    private String uid;
    private FirebaseStorage storage;
    private StorageReference storageRef; // root reference
    private StorageReference accountRef;
    private StorageReference profilePicRef;
    private StorageReference vid1Ref;
    private StorageReference vid2Ref;
    private StorageReference vid3Ref;


    /* Constructors */

    /**
     * Use this constructor for Accounts that are already in Realtime Database
     * @param uid
     */
    StorageDB(String uid) {
        this.uid = uid;
        this.storage = FirebaseStorage.getInstance();
        this.storageRef = storage.getReference();
        this.accountRef = storageRef.child(uid);
        this.profilePicRef = accountRef.child(PROFILE_PIC);
        this.vid1Ref = accountRef.child(VIDEO_1);
        this.vid2Ref = accountRef.child(VIDEO_2);
        this.vid3Ref = accountRef.child(VIDEO_3);
    }

    /* Methods */

    /**
     * Set an Account's Profile Picture.
     * @param context Usually "this", Activity or Service
     * @param fileUri Uri of the file to be uploaded.
     * @param cb Callback after completing uploadTask. Usage: uriCb Null if failed. Uri object is successful.
     */
    public void uploadProfilePic(Context context, Uri fileUri, DatabaseCallbacks cb) {
        // Determine MIME type is of /image and get file size (byte), file name
        String mimeType = getMimeType(context, fileUri);
        boolean isImage = MimeTypeFilter.matches(mimeType, "image/*");
        long fileSize = getFileSize(context, fileUri);
        //
        if (!isImage) {
            // TODO: Not an image
            cb.failureCallback(true, "Not an image.");
            Log.w(TAG, "Not an image for ProfilePic.");
        } else if (fileSize > PROFILE_PIC_SIZE_LIMIT ){
            // TODO: File too large
            cb.failureCallback(true, "File size > 5MB.");
            Log.w(TAG, "File size exceeds 5 MB");
        } else {
            // Proceed to upload...
            UploadTask uploadTask = profilePicRef.putFile(fileUri);
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // TODO: Handle unsuccessful uploads
                    cb.failureCallback(true, exception.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    cb.progressCallback(progress);
                    Log.d(TAG, "Upload is " + progress + "% done");
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "Upload is paused");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    cb.failureCallback(false,"Successful upload!");
                }
            });


            // After Upload completed, get Download URL, convert to URI.
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        cb.failureCallback(true, task.getException().getMessage());
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return profilePicRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        // Also store Uri in the class for faster access
                        StorageDB.this.profilePicDownloadUri = downloadUri;
                        // Callback to return URI
                        cb.uriCallback(downloadUri);
                        cb.failureCallback(false, "Download Uri retrieved.");
                    } else {
                        // Handle failures
                        cb.failureCallback(false, "Upload successful, but failed to retrieve Uri.");
                    }
                }
            });
        }
    }

    /**
     * Download the Account's profile picture. File object is returned via Callback.
     * @param cb File callback. Null if download failed.
     */
    public void downloadProfilePic(DatabaseCallbacks cb) {
        // Create a temp file (profilePic.tmp)
        try {
            File localFile = File.createTempFile(PROFILE_PIC, null);
            profilePicRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    cb.fileCallback(localFile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    cb.failureCallback(true, "Download failed.");
                }
            });
        } catch (Exception e) {
            if (e instanceof IOException) {
                Log.w(TAG, "File could not be created.");
                cb.failureCallback(true, "File could not be created.");
            } else if (e instanceof SecurityException) {
                Log.w(TAG, "A security manager exists and its " +
                        "SecurityManager.checkWrite(java.lang.String) " +
                        "method does not allow a file to be created");
                cb.failureCallback(true, "A security manager exists and its " +
                        "SecurityManager.checkWrite(java.lang.String) " +
                        "method does not allow a file to be created");
            } else {
                Log.w(TAG, "Unexpected error occurred.");
                cb.failureCallback(true,"Unexpected error occurred.");
            }
        }
    }

    /**
     * Get the Uri of the profile picture instead of a File.
     * @return Callback Uri of the profile picture, or Failure Callback.
     */
    public void getProfilePicDownloadUri(DatabaseCallbacks cb) {
        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                cb.uriCallback(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                cb.failureCallback(true, exception.getMessage());
            }
        });
    }

    /**
     * Upload Video to Firebase Storage. Get download Uri back if successful.
     * @param context "this"
     * @param index 0, 1, or 2.
     * @param fileUri Uri of the file to be uploaded.
     * @param cb Override to do something with the callbacks.
     */
    public void uploadVideo(Context context, int index, Uri fileUri, DatabaseCallbacks cb) {
        // Determine MIME type is of /video and get file size (byte), file name
        String mimeType = getMimeType(context, fileUri);
        boolean isVideo = MimeTypeFilter.matches(mimeType, "video/*");
        long fileSize = getFileSize(context, fileUri);
        //
        if (!isVideo) {
            cb.failureCallback(true, "Not an image.");
            Log.w(TAG, "Not an image for ProfilePic.");
        } else if (fileSize > VID_SIZE_LIMIT ){
            cb.failureCallback(true, "File size > 1GB.");
            Log.w(TAG, "File size exceeds 1 GB");
        } else {
            // Determine which video prompt reference to use
            StorageReference ref;
            if (index == 0) {
                ref = vid1Ref;
            } else if (index == 1) {
                ref = vid2Ref;
            } else if (index == 2) {
                ref = vid3Ref;
            } else {
                cb.failureCallback(true, "Incorrect video index.");
                return;
            }
            // Proceed to upload...
            UploadTask uploadTask = ref.putFile(fileUri);
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    cb.failureCallback(true, exception.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    cb.progressCallback(progress);
                    Log.d(TAG, "Upload is " + progress + "% done");
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "Upload is paused");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    cb.failureCallback(false,"Successful upload!");
                }
            });

            // After Upload completed, get Download URL, convert to URI.
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        cb.failureCallback(true, task.getException().getMessage());
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        // Also store Uri in the class for faster access
                        if (index == 0) {
                            StorageDB.this.vid1DownloadUri = downloadUri;
                        } else if (index == 1) {
                            StorageDB.this.vid2DownloadUri = downloadUri;
                        } else if (index == 2) {
                            StorageDB.this.vid3DownloadUri = downloadUri;
                        }
                        // Callback to return URI
                        cb.uriCallback(downloadUri);
                        cb.failureCallback(false, "Download Uri retrieved.");
                    } else {
                        // Handle failures
                        cb.failureCallback(false, "Upload successful, but failed to retrieve Uri.");
                    }
                }
            });
        }
    }
//    TODO: getVid1();
//



    /* Helper Functions */

    private String getMimeType(Context context, Uri uri) {
//        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
//        String mimeType = MimeTypeMap.getSingleton()
//                .getMimeTypeFromExtension(fileExtension.toLowerCase());
//        return mimeType;
        String mimeType = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private long getFileSize(Context context, Uri uri) {
        Cursor returnCursor = context.getContentResolver()
                        .query(uri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         * move to the first row in the Cursor, get the data,
         * and display it.
         */
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        long fileSize = returnCursor.getLong(sizeIndex);
        return fileSize;
    }

    private String getFileName(Context context, Uri uri) {
        Cursor returnCursor = context.getContentResolver()
                .query(uri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         * move to the first row in the Cursor, get the data,
         * and display it.
         */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String fileName = returnCursor.getString(nameIndex);
        return fileName;
    }
}
