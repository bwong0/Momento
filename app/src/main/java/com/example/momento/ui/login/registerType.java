package com.example.momento.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.momento.R;
import com.example.momento.databinding.ActivityLoginBinding;

public class registerType extends AppCompatActivity {

    RadioButton patient, caretaker, admin;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_type);

        patient = (RadioButton) findViewById(R.id.radioPatient);
        caretaker = (RadioButton) findViewById(R.id.radioFamily);
        admin = (RadioButton) findViewById(R.id.radioAdmin);
        next = (Button) findViewById(R.id.registerTypeNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(patient.isChecked())
                    openRegisterPatient();
                else if(caretaker.isChecked())
                    openRegisterFamily();
                else if(admin.isChecked())
                    openRegisterAdmin();
                else{
                    //this error message doesn't show for some reasons
                    Toast error = Toast.makeText(registerType.this, "Need to choose one to proceed.", Toast.LENGTH_SHORT);
                }
            }
        });

    }

    public void openRegisterPatient(){
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

    public void openRegisterFamily(){
        Intent intent = new Intent(this, familyRegister.class);
        startActivity(intent);
    }

    public void openRegisterAdmin(){
        Intent intent = new Intent(this, adminRegister.class);
        startActivity(intent);
    }
}