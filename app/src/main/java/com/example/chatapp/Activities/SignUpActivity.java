package com.example.chatapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity {

    TextView login;
    TextInputEditText mname, memail, mpassword, mconfirmPassword, mphone;
    RadioGroup gender;
    Button register;
    private FirebaseAuth fAuth;
    DatabaseReference reference;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        fAuth = FirebaseAuth.getInstance();

        confirmInput();

    }

    private void initViews() {
        login = findViewById(R.id.signup_login);
        mname = findViewById(R.id.full_name);
        memail = findViewById(R.id.signup_email);
        mpassword = findViewById(R.id.signup_password);
        mconfirmPassword = findViewById(R.id.confirm_password);
        mphone = findViewById(R.id.signup_phone);
        gender = findViewById(R.id.mfo);
        register = findViewById(R.id.signup_account);
    }

    private boolean validateName(){
        String nameInput = mname.getText().toString().trim();
        if(nameInput.isEmpty()){
            mname.setError("Name can't be empty");
            return false;
        }
        else{
            return true;
        }
    }

    private boolean validateEmail(){
        String emailInput = memail.getText().toString().trim();
        if(emailInput.isEmpty()){
            memail.setError("Email required");
            return false;
        }
        else{
            return true;
        }
    }

    private boolean validatePassword(){
        String passwordInput = mpassword.getText().toString().trim();
        if(passwordInput.isEmpty()){
            mpassword.setError("Password can't be empty");
            return false;
        }
        else if(passwordInput.length()<6){
            mpassword.setError("Password length must be between 6 and 15");
            return false;
        }
        else if(passwordInput.length()>15){
            mpassword.setError("Password length must be between 6 and 15");
            return false;
        }
        else{
            return true;
        }
    }

    private boolean confirmPassword(){
        String passwordInput = mpassword.getText().toString().trim();
        String confirmpass = mconfirmPassword.getText().toString().trim();
        if(confirmpass.isEmpty()){
            mconfirmPassword.setError("Password can't be empty");
            return false;
        }
        else if(confirmpass.equals(passwordInput)){
            return true;
        }
        else{
            mconfirmPassword.setError("Password not matched");
            return false;
        }
    }

    private boolean validatePhone(){
        String phoneInput = mphone.getText().toString().trim();
        if(phoneInput.isEmpty()){
            mphone.setError("Phone number required");
            return false;
        }
        else{
            return true;
        }
    }

    private boolean validateGender(){
        if (gender.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getApplicationContext(), "Please select your Gender",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }

    public void confirmInput(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateName() | !validateEmail() | !validatePassword() |
                        !validatePhone() | !confirmPassword() | !validateGender()){
                    return;
                }
                else {
                    String mail = memail.getText().toString().trim();
                    String pass = mpassword.getText().toString();
                    final String username = mname.getText().toString().trim();
                    fAuth.createUserWithEmailAndPassword(mail, pass)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser firebaseUser = fAuth.getCurrentUser();
                                        reference = FirebaseDatabase.getInstance().getReference("Users");
                                        assert firebaseUser != null;
                                        String userid = firebaseUser.getUid();
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("id", userid);
                                        hashMap.put("username", username);

                                        reference.child(userid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });

                                    }
                                    else{
                                        String s = "Sign up Failed" + task.getException();
                                        Toast.makeText(SignUpActivity.this, s,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }

            }
        });


    }
}
