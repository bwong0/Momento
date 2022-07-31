package com.example.momento.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import com.example.momento.R;
import com.example.momento.databinding.ActivityAdminHomeBinding;
import com.example.momento.databinding.ActivityPatientRegisterBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class adminHome extends AppCompatActivity {
    private ActivityAdminHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button createP = binding.patientRegister;

        createP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPatientRegister();
            }
        });
    }

    private void openPatientRegister(){
        Intent intent = new Intent(this, patientRegister.class);
        startActivity(intent);
    }
}