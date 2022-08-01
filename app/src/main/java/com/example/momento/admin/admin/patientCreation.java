package com.example.momento.admin.admin;

import androidx.appcompat.app.AppCompatActivity;
import com.example.momento.R;
import com.example.momento.database.AdminDB;
import com.example.momento.database.PatientDB;
import com.example.momento.database.ServerCallback;
import com.example.momento.ui.login.patientRegister;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class patientCreation extends AppCompatActivity implements Serializable {
    PatientDB patient1;
    PatientDB patient2;
    PatientDB patient3;
    PatientDB patient4;
    PatientDB patient5;
    PatientDB patient6;



    AdminDB admin;

    List<String> patientUids;

    TextView patientName1;
    TextView patientName2;
    TextView patientName3;
    TextView patientName4;
    TextView patientName5;
    TextView patientName6;

    ImageButton imageButton1;
    ImageButton imageButton2;
    ImageButton imageButton3;
    ImageButton imageButton4;
    ImageButton imageButton5;
    ImageButton imageButton6;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_creation);
        String uid;
        uid = getIntent().getStringExtra("uid");
        admin = new AdminDB(uid, new ServerCallback() {
            @Override
            public void isReadyCallback(boolean isReady) {

            }
        });
        patientUids = admin.getPatientList();
        ArrayList<PatientDB> patientDBArrayList = new ArrayList<>();

        ArrayList<TextView> NamesArrayList = new ArrayList<>();
        patientName1 = (TextView) findViewById(R.id.patientNameAdmin1);
        patientName2 = (TextView) findViewById(R.id.patientNameAdmin2);
        patientName3 = (TextView) findViewById(R.id.patientNameAdmin3);
        patientName4 = (TextView) findViewById(R.id.patientNameAdmin4);
        patientName5 = (TextView) findViewById(R.id.patientNameAdmin5);
        patientName6 = (TextView) findViewById(R.id.patientNameAdmin6);

        NamesArrayList.add(patientName1);
        NamesArrayList.add(patientName2);
        NamesArrayList.add(patientName3);
        NamesArrayList.add(patientName4);
        NamesArrayList.add(patientName5);
        NamesArrayList.add(patientName6);

        ArrayList<ImageButton> ImageButtonArrayList = new ArrayList<>();
        imageButton1 = (ImageButton) findViewById(R.id.patientAdmin1);
        imageButton2 = (ImageButton) findViewById(R.id.patientAdmin2);
        imageButton3 = (ImageButton) findViewById(R.id.patientAdmin3);
        imageButton4 = (ImageButton) findViewById(R.id.patientAdmin4);
        imageButton5 = (ImageButton) findViewById(R.id.patientAdmin5);
        imageButton6 = (ImageButton) findViewById(R.id.patientAdmin6);

        ImageButtonArrayList.add(imageButton1);
        ImageButtonArrayList.add(imageButton2);
        ImageButtonArrayList.add(imageButton3);
        ImageButtonArrayList.add(imageButton4);
        ImageButtonArrayList.add(imageButton5);
        ImageButtonArrayList.add(imageButton6);

//        String uri = "@drawable/empty";
//        int defaultImage = getResources().getIdentifier(uri,null,getPackageName());
//

        int size = patientUids.size();

        for (int i = 0; i < size; i++) {
            String cur = patientUids.get(i);
            PatientDB temp = new PatientDB(cur, new ServerCallback() {
                @Override
                public void isReadyCallback(boolean isReady) {
                    //
                }
            });
            patientDBArrayList.add(temp);

            NamesArrayList.get(i).setText(temp.getFirstName() + " " + temp.getLastName());
//            ImageButtonArrayList.get(i).setImageDrawable(temp.getProfile);
//            } else {
//                NamesArrayList.get(i).setText(admin.getPatints().get(i).names);
//                int profilePicture = getResources().getIdentifier(admin.getPatients.get(i).getImage(), null, getPackageName());
//                Drawable res = getResources().getDrawable(profilePicture);
//                ImageButtonArrayList.get(i).setImageDrawable(res);

            }


        imageButton1.setOnClickListener(v -> openProfileCreation(patientName1, uid));
        imageButton2.setOnClickListener(v -> openProfileCreation(patientName2, uid));
        imageButton3.setOnClickListener(v -> openProfileCreation(patientName3, uid));
        imageButton4.setOnClickListener(v -> openProfileCreation(patientName4, uid));
        imageButton5.setOnClickListener(v -> openProfileCreation(patientName5, uid));
        imageButton6.setOnClickListener(v -> openProfileCreation(patientName6, uid));
    }
    public void openProfileCreation(TextView patient, String uid){
        Intent intent;
        if (patient.getText() == "Create New Patient")
            intent = new Intent(this, patientRegister.class);
        else {
            intent = new Intent(this, familyCreation.class);
            intent.putExtra("uid", uid);
        }

        startActivity(intent);
    }
}