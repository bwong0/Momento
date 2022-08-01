package com.example.momento.database;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.momento.ui.login.LoginUICallbacks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Class for syncing data with "Families" on Firebase.
 */
public class FamilyDB extends AccountDB {

    private static final String TAG = "FamilyDB";

    // Constants for matching keys on Firebase
    public final static String FAMILY_NODE = "Families";
    public final static String VIDEO_LIST = "videoList";


    private final int MAX_NUM_VIDEOS = 3;
    private List<VideoInfo> videoList = new ArrayList<>(MAX_NUM_VIDEOS);

    private final ValueEventListener familyListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            // update Class members after success
            Map<String, List<HashMap<String,Object>>> data
                    = (Map<String, List<HashMap<String,Object>>>) snapshot.getValue();
            List<HashMap<String,Object>> dataList = data.get(VIDEO_LIST);
            videoList.clear();
            for ( int i = 0; i < 3; i++) {
                HashMap<String, Object> each = dataList.get(i);
                VideoInfo newVid = new VideoInfo((String) each.get("url"), (long) each.get("playCount"));
                videoList.add(newVid);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            // Reading database failed, log a message
            Log.w(TAG, "loadPost:onCancelled", error.toException());
        }
    };

    /* Constructors */

    /**
     * Constructor for FamilyDB. Use this during Login.
     * Use this constructor when there is already an Account on Firebase.
     * @param uid Should match UID for the account on Firebase Authentication.
     */
    public FamilyDB(String uid) {
        // Retrieve "Account" info
        super(uid);
        // Check if "Family" entry exists, if so, update patientList
        mDatabase.child(FAMILY_NODE).child(this.getUid()).get()
            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                   // fetch value and store into a Map
                   DataSnapshot snapshot = task.getResult();
                   // if the UID is already in Firebase, fetch data members
                   if (snapshot.exists()) {
                       // update Class members after success
                       Map<String, List<HashMap<String,Object>>> data
                               = (Map<String, List<HashMap<String,Object>>>) snapshot.getValue();
                       List<HashMap<String,Object>> dataList = data.get(VIDEO_LIST);
                       FamilyDB.this.videoList.clear(); // reset
                       for ( int i = 0; i < 3; i++) {
                           HashMap<String, Object> each = dataList.get(i);
                           VideoInfo newVid = new VideoInfo((String) each.get("url"), (long) each.get("playCount"));
                           FamilyDB.this.videoList.add(newVid);
                       }
                       // Initiate Listener to the Account
                       mDatabase.child(FAMILY_NODE).child(uid).addValueEventListener(familyListener);
                   } else {
                       // if no data in Firebase Realtime Database, populate with default VideoInfo
                       // Creates an entry in "Families" on Firebase
                       Map<String, List<VideoInfo>> videos = new HashMap<>();
                       // populate videoList with default Videos
                       FamilyDB.this.videoList.add(new VideoInfo());
                       FamilyDB.this.videoList.add(new VideoInfo());
                       FamilyDB.this.videoList.add(new VideoInfo());
                       videos.put(VIDEO_LIST, FamilyDB.this.videoList);
                       mDatabase.child(FAMILY_NODE).child(FamilyDB.this.getUid()).setValue(videos)
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   // Initiate Listener
                                   mDatabase.child(FAMILY_NODE).child(uid).addValueEventListener(familyListener);
                               }
                           })
                           .addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   // Write failed
                                   // TODO: Handle write failure?
                               }
                           });
                   }
                } else {
                   Log.d(TAG, "failed: " + String.valueOf(task.getResult().getValue()));
                }
                }
           }
        );
    }

    /**
     * Constructor for FamilyDB Class. Use this during Registration.
     * @param uid Should match UID for the account on Firebase Authentication.
     * @param type AccountType Enum.
     * @param firstName
     * @param lastName
     * @param email
     * @param address
     */
    public FamilyDB(String uid, AccountType type, String firstName, String lastName,
                   String email, String address) {
        // Creates an entry in "Accounts" on Firebase
        super(uid, type, firstName, lastName, email, address);
        // Creates an entry in "Families" on Firebase
        Map<String, List<VideoInfo>> videos = new HashMap<>();
        // populate videoList with default Videos
        this.videoList.add(new VideoInfo());
        this.videoList.add(new VideoInfo());
        this.videoList.add(new VideoInfo());
        videos.put(VIDEO_LIST, this.videoList);
        mDatabase.child(FAMILY_NODE).child(this.getUid()).setValue(videos)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Initiate Listener
                    mDatabase.child(FAMILY_NODE).child(uid).addValueEventListener(familyListener);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Write failed
                    // TODO: Handle write failure?
                }
            });
    }

    /* Methods */

    /**
     * Upload a video to the specified index (0, 1, 2)
     * @param context
     * @param index 0, 1, 2
     * @param fileUri
     * @param cb
     */
    public void uploadVideo(Context context, int index, Uri fileUri, DatabaseCallbacks cb) {
        Log.d(TAG, "uploadVideo() called, current videoList: " + this.videoList.toString());
        this.storage.uploadVideo(context, index, fileUri, new DatabaseCallbacks() {
            @Override
            public void uriCallback(Uri uri) {
                cb.uriCallback(uri); // can do something with the uri through cb
                // update VideoInfo
                try {
                    updateVideoUrl(index, uri.toString());
                    resetVideoPlayCount(index);
                } catch (IndexOutOfBoundsException e) {
                    cb.failureCallback(true, "Index Out of Bound");
                }
            }
            @Override
            public void fileCallback(File file) { // not implemented
            }
            @Override
            public void progressCallback(double percentage) {
                cb.progressCallback(percentage);
            }
            @Override
            public void failureCallback(boolean hasFailed, String message) {
                cb.failureCallback(hasFailed, message);
            }
        });
    }


    /**
     * Update specific video's URL in the Video List at index 0 to 2. Updates Firebase.
     * Resets playCount for the new video.
     * @param index 0 to 2.
     * @param url String URL.
     * @throws IndexOutOfBoundsException
     */
    private void updateVideoUrl(int index, String url) throws IndexOutOfBoundsException {
        VideoInfo newVideo = new VideoInfo(url);
        VideoInfo backup = new VideoInfo(this.videoList.get(index)); // deepCopy if needing reversal
        // TODO: test if persistent
        this.videoList.set(index, newVideo);
        updateFirebase(index, backup);
    }

    /**
     * Get playCount (int) for the video at index.
     * @param index
     * @return Integer. Play count of the video.
     * @throws IndexOutOfBoundsException
     */
    public long getVideoPlayCount(int index) throws IndexOutOfBoundsException {
        return (this.videoList.get(index).getPlayCount());
    }

    /**
     * Reset playCount for the video at index. Update Firebase.
     * @param index
     * @throws IndexOutOfBoundsException
     */
    public void resetVideoPlayCount(int index) throws IndexOutOfBoundsException {
        VideoInfo backup = new VideoInfo(this.videoList.get(index)); // deepCopy if needing reversal
        this.videoList.get(index).resetPlayCount();
        updateFirebase(index, backup);
    }

    /**
     * Increment playCount for the video at index. Update Firebase.
     * @param index
     * @throws IndexOutOfBoundsException
     */
    public void incrementVideoPlayCount(int index) throws IndexOutOfBoundsException {
        VideoInfo backup = new VideoInfo(this.videoList.get(index)); // deepCopy if needing reversal
        this.videoList.get(index).incrementPlayCount();
        updateFirebase(index, backup);
        // TODO: implement atomic server-side increment?
    }

    /**
     * Update Firebase with current VideoInfo properties. Reverse if update fails.
     * @param index Index of the video to be updated.
     * @param backup VideoInfo deep copy of starting point for reversal.
     */
    private void updateFirebase(int index, VideoInfo backup) {
        Map<String, List<VideoInfo>> videos = new HashMap<>();
        videos.put(VIDEO_LIST, this.videoList);
        mDatabase.child(FAMILY_NODE).child(this.getUid()).setValue(videos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Initiate Listener to the Family
                        if (videoList.size() >= 1) {
                            mDatabase.child(FAMILY_NODE).child(FamilyDB.this.getUid())
                                    .addValueEventListener(familyListener);
                        }
                        Log.d(TAG, "added patient successfully.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed, revert changes
                        FamilyDB.this.videoList.set(index, backup);
                        Log.d(TAG, "failed to update Video.");
                    }
                });
    }
}
