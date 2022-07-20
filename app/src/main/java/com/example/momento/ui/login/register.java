package com.example.momento.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.momento.R;
import com.example.momento.databinding.ActivityLoginBinding;
import com.example.momento.databinding.ActivityRegisterBinding;

public class register extends AppCompatActivity {
    private Button next;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button next = binding.proceedToAdmin;
        final EditText first = binding.editTextFirstName;
        final EditText last = binding.editTextLastName;
        final EditText address = binding.editTextAddress;
        final EditText ID = binding.editTextID;

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(empty(first) || empty(last) || empty(address) || empty(ID)){
                    finish();
                    startActivity(getIntent());
                }else{
                    // create admin account here, and go to admin home after successfully created
                    openAdminHome();
                }
            }
        });
    }

    public void openAdminHome(){
        Intent intent = new Intent(this, adminHome.class);
        startActivity(intent);
    }

    public boolean empty(EditText a){
        if(a.getText().toString().trim().length() == 0)
            return true;
        else
            return false;
    }
}