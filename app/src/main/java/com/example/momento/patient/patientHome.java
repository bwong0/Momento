package com.example.momento.patient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.momento.database.FamilyDB;
import com.example.momento.database.PatientDB;
import com.example.momento.ui.login.Persons;

import androidx.appcompat.app.AppCompatActivity;

import com.example.momento.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class patientHome extends AppCompatActivity implements Serializable {


//    public static final String EXTRA_TEXT = "com.example.momento.ui.login.EXTRA_TEXT";
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        // initialize imageView
        // with method findViewById()

        uid = getIntent().getStringExtra("uid");
        PatientDB patient = new PatientDB(uid);
        List<String> profileUids = patient.getFamilyList();

        ArrayList<FamilyDB> familyDBArrayList = new ArrayList<>();


        TextView patientName1 = (TextView) findViewById(R.id.patientName1);
        TextView patientName2 = (TextView) findViewById(R.id.patientName2);
        TextView patientName3 = (TextView) findViewById(R.id.patientName3);
        TextView patientName4 = (TextView) findViewById(R.id.patientName4);
        TextView patientName5 = (TextView) findViewById(R.id.patientName5);
        TextView patientName6 = (TextView) findViewById(R.id.patientName6);

        ArrayList<TextView> patientNameArrayList = new ArrayList<>();
        patientNameArrayList.add(patientName1);
        patientNameArrayList.add(patientName2);
        patientNameArrayList.add(patientName3);
        patientNameArrayList.add(patientName4);
        patientNameArrayList.add(patientName5);
        patientNameArrayList.add(patientName6);

        ImageButton patientProfile1 = (ImageButton) findViewById(R.id.patientProfile1);
        ImageButton patientProfile2 = (ImageButton) findViewById(R.id.patientProfile2);
        ImageButton patientProfile3 = (ImageButton) findViewById(R.id.patientProfile3);
        ImageButton patientProfile4 = (ImageButton) findViewById(R.id.patientProfile4);
        ImageButton patientProfile5 = (ImageButton) findViewById(R.id.patientProfile5);
        ImageButton patientProfile6 = (ImageButton) findViewById(R.id.patientProfile6);

        ArrayList<ImageButton> patientProfileArrayList = new ArrayList<>();
        patientProfileArrayList.add(patientProfile1);
        patientProfileArrayList.add(patientProfile2);
        patientProfileArrayList.add(patientProfile3);
        patientProfileArrayList.add(patientProfile4);
        patientProfileArrayList.add(patientProfile5);
        patientProfileArrayList.add(patientProfile6);

        int size = profileUids.size();


      for (int i = 0; i < size; i++){
          String cur = profileUids.get(i);
          FamilyDB temp = new FamilyDB(cur);
          familyDBArrayList.add(temp);
          patientNameArrayList.get(i).setVisibility(View.VISIBLE);
          patientNameArrayList.get(i).setText(temp.getFirstName() + " " + temp.getLastName());
          patientProfileArrayList.get(i).setVisibility(View.VISIBLE);
//          patientProfileArrayList.get(i).setImageDrawable(temp.image);
      }



//         Apply OnClickListener  to imageView to
//         switch from one activity to another

        patientProfile1.setOnClickListener(v -> openProfiles(uid));
        patientProfile2.setOnClickListener(v -> openProfiles(uid));
        patientProfile3.setOnClickListener(v -> openProfiles(uid));
        patientProfile4.setOnClickListener(v -> openProfiles(uid));
        patientProfile5.setOnClickListener(v -> openProfiles(uid));
        patientProfile6.setOnClickListener(v -> openProfiles(uid));


    }
    public void openProfiles(String uid){
        Intent intent = new Intent(this, profiles.class);
        intent.putExtra("uid", uid );
        startActivity(intent);
    }
//    public void openIV2(){
//        TextView textView1 = findViewById(R.id.personName2);
//        String text = textView1.getText().toString();
//
//        Intent intent = new Intent(this, profiles.class);
//        intent.putExtra(EXTRA_TEXT,text);
//        startActivity(intent);
//    }
//    public void openIV3(){
//        TextView textView1 = findViewById(R.id.personName3);
//        String text = textView1.getText().toString();
//
//        Intent intent = new Intent(this, profiles.class);
//        intent.putExtra(EXTRA_TEXT,text);
//        startActivity(intent);
//    }
//    public void openIV4(){
//        TextView textView1 = findViewById(R.id.personName4);
//        String text = textView1.getText().toString();
//
//        Intent intent = new Intent(this, profiles.class);
//        intent.putExtra(EXTRA_TEXT,text);
//        startActivity(intent);
//    }
//    public void openIV5(){
//        TextView textView1 = findViewById(R.id.personName5);
//        String text = textView1.getText().toString();
//
//        Intent intent = new Intent(this, profiles.class);
//        intent.putExtra(EXTRA_TEXT,text);
//        startActivity(intent);
//    }
//    public void openIV6(){
//        TextView textView1 = findViewById(R.id.personName6);
//        String text = textView1.getText().toString();
//
//        Intent intent = new Intent(this, profiles.class);
//        intent.putExtra(EXTRA_TEXT,text);
//        startActivity(intent);
//    }
}