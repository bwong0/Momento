package com.example.momento.family;


import androidx.appcompat.app.AppCompatActivity;
import com.example.momento.R;

import com.example.momento.ui.login.Persons;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;



public class familyHome extends AppCompatActivity implements Serializable {

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

        familyprofile1.setOnClickListener(v -> openProfileCreation(ArrayListProfiles.get(0)));
        familyprofile2.setOnClickListener(v -> openProfileCreation(ArrayListProfiles.get(1)));
        familyprofile3.setOnClickListener(v -> openProfileCreation(ArrayListProfiles.get(2)));
        familyprofile4.setOnClickListener(v -> openProfileCreation(ArrayListProfiles.get(3)));
        familyprofile5.setOnClickListener(v -> openProfileCreation(ArrayListProfiles.get(4)));
        familyprofile6.setOnClickListener(v -> openProfileCreation(ArrayListProfiles.get(5)));
}
    public void openProfileCreation(Persons person){

        Intent intent = new Intent(this, ProfileCreation.class);
        intent.putExtra("person", person );
        startActivity(intent);
    }
}
