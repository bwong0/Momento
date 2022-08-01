package com.example.momento.admin.admin;

import androidx.appcompat.app.AppCompatActivity;
import com.example.momento.R;
import com.example.momento.database.AccountDB;
import com.example.momento.database.FamilyDB;
import com.example.momento.database.PatientDB;
import com.example.momento.database.ServerCallback;
import com.example.momento.family.ProfileCreation;
import com.example.momento.ui.login.Persons;
import com.example.momento.ui.login.familyRegister;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class familyCreation extends AppCompatActivity {
    String uid;

    PatientDB patient;

    List<String> familyUids;

    TextView familyName1;
    TextView familyName2;
    TextView familyName3;
    TextView familyName4;
    TextView familyName5;
    TextView familyName6;

    ImageButton imageButton1;
    ImageButton imageButton2;
    ImageButton imageButton3;
    ImageButton imageButton4;
    ImageButton imageButton5;
    ImageButton imageButton6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_creation);

        uid = getIntent().getStringExtra("uid");
        patient = new PatientDB(uid, new ServerCallback() {
            @Override
            public void isReadyCallback(boolean isReady) {

            }
        });
        familyUids = patient.getFamilyList();
        ArrayList<FamilyDB> familyDBArrayList = new ArrayList<>();

        ArrayList<TextView> NamesArrayList = new ArrayList<>();
        familyName1 = (TextView) findViewById(R.id.familyNameAdmin1);
        familyName2 = (TextView) findViewById(R.id.familyNameAdmin2);
        familyName3 = (TextView) findViewById(R.id.familyNameAdmin3);
        familyName4 = (TextView) findViewById(R.id.familyNameAdmin4);
        familyName5 = (TextView) findViewById(R.id.familyNameAdmin5);
        familyName6 = (TextView) findViewById(R.id.familyNameAdmin6);

        NamesArrayList.add(familyName1);
        NamesArrayList.add(familyName2);
        NamesArrayList.add(familyName3);
        NamesArrayList.add(familyName4);
        NamesArrayList.add(familyName5);
        NamesArrayList.add(familyName6);

        ArrayList<ImageButton> ImageButtonArrayList = new ArrayList<>();
        imageButton1 = (ImageButton) findViewById(R.id.familyAdmin1);
        imageButton2 = (ImageButton) findViewById(R.id.familyAdmin2);
        imageButton3 = (ImageButton) findViewById(R.id.familyAdmin3);
        imageButton4 = (ImageButton) findViewById(R.id.familyAdmin4);
        imageButton5 = (ImageButton) findViewById(R.id.familyAdmin5);
        imageButton6 = (ImageButton) findViewById(R.id.familyAdmin6);

        ImageButtonArrayList.add(imageButton1);
        ImageButtonArrayList.add(imageButton2);
        ImageButtonArrayList.add(imageButton3);
        ImageButtonArrayList.add(imageButton4);
        ImageButtonArrayList.add(imageButton5);
        ImageButtonArrayList.add(imageButton6);


//        String uri = "@drawable/empty";
//        int defaultImage = getResources().getIdentifier(uri,null,getPackageName());
        int size = familyUids.size();

        for (int i = 0; i < size; i++) {
            String cur = familyUids.get(i);
            FamilyDB temp = new FamilyDB(cur, new ServerCallback() {
                @Override
                public void isReadyCallback(boolean isReady) {

                }
            });
            familyDBArrayList.add(temp);

            NamesArrayList.get(i).setText(temp.getFirstName()+" "+temp.getLastName());
//            ImageButtonArrayList.get(i).setImageDrawable(temp.getProfile);
//

//            } else {
//                NamesArrayList.get(i).setText(admin.getPatints().get(i).names);
//                int profilePicture = getResources().getIdentifier(admin.getPatients.get(i).getImage(), null, getPackageName());
//                Drawable res = getResources().getDrawable(profilePicture);
//                ImageButtonArrayList.get(i).setImageDrawable(res);


        }

        imageButton1.setOnClickListener(v -> openProfileCreation(familyName1, uid));
        imageButton2.setOnClickListener(v -> openProfileCreation(familyName2, uid));
        imageButton3.setOnClickListener(v -> openProfileCreation(familyName3, uid));
        imageButton4.setOnClickListener(v -> openProfileCreation(familyName4, uid));
        imageButton5.setOnClickListener(v -> openProfileCreation(familyName5, uid));
        imageButton6.setOnClickListener(v -> openProfileCreation(familyName6, uid));
    }
    public void openProfileCreation(TextView patient, String uid){
        Intent intent;
        if (patient.getText() == "Create New Profile")
            intent = new Intent(this, familyRegister.class);
        else {
            intent = new Intent(this, ProfileCreation.class);
            intent.putExtra("uid",uid);
        }

        startActivity(intent);
    }
}
