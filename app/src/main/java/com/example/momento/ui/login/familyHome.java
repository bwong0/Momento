package com.example.momento.ui.login;


import androidx.appcompat.app.AppCompatActivity;
import com.example.momento.R;
import com.example.momento.ui.login.Persons;
import android.app.Activity;
import com.example.momento.ui.login.Home;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;



public class familyHome extends AppCompatActivity implements Serializable {
    Persons familyPerson1;
    Persons familyPerson2;
    Persons familyPerson3;
    Persons familyPerson4;
    Persons familyPerson5;
    Persons familyPerson6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_home);

        TextView familyname1 = (TextView) findViewById(R.id.familyName1);
        TextView familyname2 = (TextView) findViewById(R.id.familyName2);
        TextView familyname3 = (TextView) findViewById(R.id.familyName3);
        TextView familyname4 = (TextView) findViewById(R.id.familyName4);
        TextView familyname5 = (TextView) findViewById(R.id.familyName5);
        TextView familyname6 = (TextView) findViewById(R.id.familyName6);

        ArrayList<TextView> familyNameArrayList = new ArrayList<>();
        familyNameArrayList.add(familyname1);
        familyNameArrayList.add(familyname2);
        familyNameArrayList.add(familyname3);
        familyNameArrayList.add(familyname4);
        familyNameArrayList.add(familyname5);
        familyNameArrayList.add(familyname6);

        ImageButton familyprofile1 = (ImageButton) findViewById(R.id.familyProfile1);
        ImageButton familyprofile2 = (ImageButton) findViewById(R.id.familyProfile2);
        ImageButton familyprofile3 = (ImageButton) findViewById(R.id.familyProfile3);
        ImageButton familyprofile4 = (ImageButton) findViewById(R.id.familyProfile4);
        ImageButton familyprofile5 = (ImageButton) findViewById(R.id.familyProfile5);
        ImageButton familyprofile6 = (ImageButton) findViewById(R.id.familyProfile6);


        ArrayList<ImageButton> familyProfileArrayList = new ArrayList<>();
        familyProfileArrayList.add(familyprofile1);
        familyProfileArrayList.add(familyprofile2);
        familyProfileArrayList.add(familyprofile3);
        familyProfileArrayList.add(familyprofile4);
        familyProfileArrayList.add(familyprofile5);
        familyProfileArrayList.add(familyprofile6);



//        familyPerson1 = new Persons(findViewById(R.id.familyProfile1));
//        familyPerson2 = new Persons(findViewById(R.id.familyProfile2));
//        familyPerson3 = new Persons(findViewById(R.id.familyProfile3));
//        familyPerson4 = new Persons(findViewById(R.id.familyProfile4));
//        familyPerson5 = new Persons(findViewById(R.id.familyProfile5));
//        familyPerson6 = new Persons(findViewById(R.id.familyProfile6));
//
//        ArrayList<Persons> FamilyPersons = new ArrayList<>();
//        FamilyPersons.add(familyPerson1);
//        FamilyPersons.add(familyPerson2);
//        FamilyPersons.add(familyPerson3);
//        FamilyPersons.add(familyPerson4);
//        FamilyPersons.add(familyPerson5);
//        FamilyPersons.add(familyPerson6);

        String uri = "@drawable/empty";
        int defaultImage = getResources().getIdentifier(uri,null,getPackageName());

        ArrayList<Persons> ArrayListProfiles = (ArrayList<Persons>) getIntent().getSerializableExtra("ArrayListProfile");

        for (int i = 0; i < 6; i++){
            if(ArrayListProfiles.get(i).profilePresent == false){
                familyNameArrayList.get(i).setText("Create New Profile");
                Drawable res = getResources().getDrawable(defaultImage);
                familyProfileArrayList.get(i).setImageDrawable(res);
            }
            else{
                ArrayListProfiles.get(i).profilePresent = true;
                familyNameArrayList.get(i).setText(ArrayListProfiles.get(i).getName());
                int profilePicutre = getResources().getIdentifier(ArrayListProfiles.get(i).getImage(),null,getPackageName());
                Drawable res = getResources().getDrawable(profilePicutre);
                familyProfileArrayList.get(i).setImageDrawable(res);
//                FamilyPersons.get(i).getImage().setImageURI(ArrayListProfiles.get(i).getImage());  --- set profile image to image
            }
        }


//        Intent toProfile = new Intent(this, ProfileCreation.class);
//        toProfile.putExtra("ArrayListProfiles", ArrayListProfiles);




//        if(person1.profilePresent == true){
//            person1.getImage().setOnClickListener(v -> openIV1());
//        }
//        else {
//            person1.getImage().setOnClickListener((v -> CreateProfile()));
//        }
//        if(person2.profilePresent == true){
//            person2.getImage().setOnClickListener(v -> openIV2());
//        }
//        else {
//            person2.getImage().setOnClickListener((v -> CreateProfile()));
//        }
//        if(person3.profilePresent == true){
//            person3.getImage().setOnClickListener(v -> openIV3());
//        }
//        else {
//            person3.getImage().setOnClickListener((v -> CreateProfile()));
//        }
//        if(person4.profilePresent == true){
//            person4.getImage().setOnClickListener(v -> openIV4());
//        }
//        else {
//            person4.getImage().setOnClickListener((v -> CreateProfile()));
//        }
//        if(person5.profilePresent == true){
//            person5.getImage().setOnClickListener(v -> openIV5());
//        }
//        else {
//            person5.getImage().setOnClickListener((v -> CreateProfile()));
//        }
//        if(person6.profilePresent == true){
//            person6.getImage().setOnClickListener(v -> openIV6());
//        }
//        else {
//            person6.getImage().setOnClickListener((v -> CreateProfile()));
//        }

        familyprofile1.setOnClickListener(v -> openProfileCreation(ArrayListProfiles.get(0)));
        familyprofile2.setOnClickListener(v -> openProfileCreation(ArrayListProfiles.get(1)));
        familyprofile3.setOnClickListener(v -> openProfileCreation(ArrayListProfiles.get(2)));
        familyprofile4.setOnClickListener(v -> openProfileCreation(ArrayListProfiles.get(3)));
        familyprofile5.setOnClickListener(v -> openProfileCreation(ArrayListProfiles.get(4)));
        familyprofile6.setOnClickListener(v -> openProfileCreation(ArrayListProfiles.get(5)));

}
//    public void CreateProfile(){
//
//        Intent intent = new Intent(this, profiles.class);
//        startActivity(intent);
//    }
    public void openProfileCreation(Persons person){

        Intent intent = new Intent(this, ProfileCreation.class);
        intent.putExtra("person", person );

        startActivity(intent);
    }
//    public void openIV2(Persons person){
//
//
//        Intent intent = new Intent(this, profiles.class);
//        intent.putExtra("person", person );
//        startActivity(intent);
//    }
//    public void openIV3(Persons person){
//
//        Intent intent = new Intent(this, profiles.class);
//        intent.putExtra("person", person );
//        startActivity(intent);
//    }
//    public void openIV4(Persons person){
//
//
//        Intent intent = new Intent(this, profiles.class);
//        intent.putExtra("person", person );
//        startActivity(intent);
//    }
//    public void openIV5(Person person){
//        TextView textView1 = findViewById(R.id.personName5);
//        String text = textView1.getText().toString();
//
//        Intent intent = new Intent(this, profiles.class);
//        intent.putExtra(EXTRA_TEXT,text);
//        startActivity(intent);
//    }
//    public void openIV6() {
//        TextView textView1 = findViewById(R.id.personName6);
//        String text = textView1.getText().toString();
//
//        Intent intent = new Intent(this, profiles.class);
//        intent.putExtra(EXTRA_TEXT, text);
//        startActivity(intent);


    }
