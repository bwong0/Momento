package com.example.momento.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.momento.R;

import java.util.ArrayList;

class Persons{
    public ImageView image;
    public TextView name;

    public Persons(ImageView inputImage, TextView InputText){
        image = inputImage;
        name = InputText;

    }

    public  Persons(){
        image = null;
        name = null;
    }

    public TextView getName() {
        return name;
    }

    public ImageView getImage() {
        return image;
    }
}
public class patientHome extends AppCompatActivity {


    public static final String EXTRA_TEXT = "com.example.momento.ui.login.EXTRA_TEXT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        // initialize imageView
        // with method findViewById()


        Persons person1 = new Persons(findViewById(R.id.person1),findViewById(R.id.personName1));
        Persons person2 = new Persons(findViewById(R.id.person2),findViewById(R.id.personName2));
        Persons person3 = new Persons(findViewById(R.id.person3),findViewById(R.id.personName3));
        Persons person4 = new Persons(findViewById(R.id.person4),findViewById(R.id.personName4));
        Persons person5 = new Persons(findViewById(R.id.person5),findViewById(R.id.personName5));
        Persons person6 = new Persons(findViewById(R.id.person6),findViewById(R.id.personName6));

        ArrayList<Persons> ArrayListPerson = new ArrayList<>();
        ArrayListPerson.add(person1);
        ArrayListPerson.add(person2);
        ArrayListPerson.add(person3);
        ArrayListPerson.add(person4);
        ArrayListPerson.add(person5);
        ArrayListPerson.add(person6);

        for (Persons person: ArrayListPerson){
            if(person.getName().getText() == null || person.getName().getText() == ""){
                person.getImage().setVisibility(View.GONE);
            }
        }



        // Apply OnClickListener  to imageView to
        // switch from one activity to another

        person1.getImage().setOnClickListener(v -> openIV1());
        person2.getImage().setOnClickListener(v -> openIV2());
        person3.getImage().setOnClickListener(v -> openIV3());
        person4.getImage().setOnClickListener(v -> openIV4());
        person5.getImage().setOnClickListener(v -> openIV5());
        person6.getImage().setOnClickListener(v -> openIV6());
    }
    public void openIV1(){
        TextView textView1 = findViewById(R.id.personName1);
        String text = textView1.getText().toString();

        Intent intent = new Intent(this, profiles.class);
        intent.putExtra(EXTRA_TEXT,text);
        startActivity(intent);
    }
    public void openIV2(){
        TextView textView1 = findViewById(R.id.personName2);
        String text = textView1.getText().toString();

        Intent intent = new Intent(this, profiles.class);
        intent.putExtra(EXTRA_TEXT,text);
        startActivity(intent);
    }
    public void openIV3(){
        TextView textView1 = findViewById(R.id.personName3);
        String text = textView1.getText().toString();

        Intent intent = new Intent(this, profiles.class);
        intent.putExtra(EXTRA_TEXT,text);
        startActivity(intent);
    }
    public void openIV4(){
        TextView textView1 = findViewById(R.id.personName4);
        String text = textView1.getText().toString();

        Intent intent = new Intent(this, profiles.class);
        intent.putExtra(EXTRA_TEXT,text);
        startActivity(intent);
    }
    public void openIV5(){
        TextView textView1 = findViewById(R.id.personName5);
        String text = textView1.getText().toString();

        Intent intent = new Intent(this, profiles.class);
        intent.putExtra(EXTRA_TEXT,text);
        startActivity(intent);
    }
    public void openIV6(){
        TextView textView1 = findViewById(R.id.personName6);
        String text = textView1.getText().toString();

        Intent intent = new Intent(this, profiles.class);
        intent.putExtra(EXTRA_TEXT,text);
        startActivity(intent);
    }
}