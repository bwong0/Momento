package com.example.momento.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import com.example.momento.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class profiles extends AppCompatActivity {

    TextView prompt1;
    TextView prompt2;
    TextView prompt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        Intent intent = getIntent();
        String text = intent.getStringExtra(patientHome.EXTRA_TEXT);

        TextView profileName = (TextView) findViewById(R.id.profileName);

        profileName.setText(text);

        // Initialize TextView objects
        prompt1 = findViewById((R.id.prompt_1));
        prompt2 = findViewById((R.id.prompt_2));
        prompt3 = findViewById((R.id.prompt_3));

        // Apply OnClickListener to TextView
        // navigates to another activity

        prompt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openVidPlayer();}
        });

        prompt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openVidPlayer();}
        });

        prompt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openVidPlayer();}
        });
    }

    private void openVidPlayer(){
        // TODO: add code to launch video player activity
    }
}