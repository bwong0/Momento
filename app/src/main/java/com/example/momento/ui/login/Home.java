package com.example.momento.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import com.example.momento.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    Button patientB;
    Button familyB;
    Button adminB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       patientB = (Button) findViewById(R.id.p);
       familyB = (Button) findViewById(R.id.f);
       adminB = (Button) findViewById(R.id.a);

       familyB.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               openFamily();
           }
       });

        patientB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPatient();
            }
        });

        adminB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAdmin();
            }
        });



    }
    public void openFamily(){
        Intent intent = new Intent(this, familyHome.class);
        startActivity(intent);
    }
    public void openPatient(){
        Intent intent = new Intent(this, patientHome.class);
        startActivity(intent);
    }
    public void openAdmin(){
        Intent intent = new Intent(this, adminHome.class);
        startActivity(intent);
    }
}