package com.example.momento.ui.login;

import androidx.appcompat.app.AppCompatActivity;




import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import com.example.momento.R;
import com.example.momento.family.familyHome;
import com.example.momento.patient.patientHome;


public class Home extends AppCompatActivity implements Serializable {

    Button patientB;
    Button familyB;
    Button adminB;
    Persons person1;
    Persons person2;
    Persons person3;
    Persons person4;
    Persons person5;
    Persons person6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       patientB = (Button) findViewById(R.id.p);
       familyB = (Button) findViewById(R.id.f);
       adminB = (Button) findViewById(R.id.a);

        person1 = new Persons("@drawable/lebron_james","Lebron James",true);
        person2 = new Persons();
        person3 = new Persons();
        person4 = new Persons();
        person5 = new Persons();
        person6 = new Persons();

        ArrayList<Persons> ArrayListProfile = new ArrayList<>();
        ArrayListProfile.add(person1);
        ArrayListProfile.add(person2);
        ArrayListProfile.add(person3);
        ArrayListProfile.add(person4);
        ArrayListProfile.add(person5);
        ArrayListProfile.add(person6);

//        Intent toPatientHome = new Intent(this, patientHome.class);
//        Intent toProfile = new Intent(this, ProfileCreation.class);
//        toPatientHome.putExtra("profileArrayList", ArrayListProfile);
//        toProfile.putExtra("profileArrayList", ArrayListProfile);

       familyB.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               openFamily(ArrayListProfile);
           }
       });

        patientB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openPatient(ArrayListProfile);
            }
        });

        adminB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdmin(ArrayListProfile);
            }
        });



    }
    public void openFamily(ArrayList<Persons> ArrayListProfile){
        Intent intent = new Intent(this, familyHome.class);
        intent.putExtra("ArrayListProfile", ArrayListProfile);
        startActivity(intent);
    }
    public void openPatient(ArrayList<Persons> ArrayListProfile){
        Intent intent = new Intent(this, patientHome.class);
        intent.putExtra("ArrayListProfile", ArrayListProfile);
        startActivity(intent);
    }
    public void openAdmin(ArrayList<Persons> ArrayListProfile){
        Intent intent = new Intent(this, adminHome.class);
        intent.putExtra("ArrayListProfile", ArrayListProfile);
        startActivity(intent);
    }
}