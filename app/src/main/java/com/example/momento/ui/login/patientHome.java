package com.example.momento.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import com.example.momento.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.TextView;

import org.w3c.dom.Text;

public class patientHome extends AppCompatActivity {
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;

    public static final String EXTRA_TEXT = "com.example.momento.ui.login.EXTRA_TEXT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        // initialize imageView
        // with method findViewById()
        imageView1 = findViewById(R.id.person1);
        imageView2 = findViewById(R.id.person2);
        imageView3 = findViewById(R.id.person3);
        imageView4 = findViewById(R.id.person4);
        imageView5 = findViewById(R.id.person5);
        imageView6 = findViewById(R.id.person6);

        // Apply OnClickListener  to imageView to
        // switch from one activity to another
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIV1();
            }
        });


        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIV2();
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIV3();
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIV4();
            }
        });
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIV5();
            }
        });
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIV6();
            }
        });
    }
    public void openIV1(){
        TextView textView1 = (TextView) findViewById(R.id.personName1);
        String text = textView1.getText().toString();

        Intent intent = new Intent(this, profiles.class);
        intent.putExtra(EXTRA_TEXT,text);
        startActivity(intent);
    }
    public void openIV2(){
        TextView textView1 = (TextView) findViewById(R.id.personName2);
        String text = textView1.getText().toString();

        Intent intent = new Intent(this, profiles.class);
        intent.putExtra(EXTRA_TEXT,text);
        startActivity(intent);
    }
    public void openIV3(){
        TextView textView1 = (TextView) findViewById(R.id.personName3);
        String text = textView1.getText().toString();

        Intent intent = new Intent(this, profiles.class);
        intent.putExtra(EXTRA_TEXT,text);
        startActivity(intent);
    }
    public void openIV4(){
        TextView textView1 = (TextView) findViewById(R.id.personName4);
        String text = textView1.getText().toString();

        Intent intent = new Intent(this, profiles.class);
        intent.putExtra(EXTRA_TEXT,text);
        startActivity(intent);
    }
    public void openIV5(){
        TextView textView1 = (TextView) findViewById(R.id.personName5);
        String text = textView1.getText().toString();

        Intent intent = new Intent(this, profiles.class);
        intent.putExtra(EXTRA_TEXT,text);
        startActivity(intent);
    }
    public void openIV6(){
        TextView textView1 = (TextView) findViewById(R.id.personName6);
        String text = textView1.getText().toString();

        Intent intent = new Intent(this, profiles.class);
        intent.putExtra(EXTRA_TEXT,text);
        startActivity(intent);
    }
}