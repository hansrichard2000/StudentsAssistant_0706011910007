package com.uc.studentsassistant_0706011910007;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uc.studentsassistant_0706011910007.fragment.FragmentAccountActivity;
import com.uc.studentsassistant_0706011910007.model.Student;

public class LoginActivity extends AppCompatActivity implements TextWatcher {

    Button button_login, button_register;
    TextInputLayout email, pass;
    String email_student, pass_student;
    Student student;
    Toolbar toolbar;
    Dialog dialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, StarterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog = Glovar.loadingDialog(LoginActivity.this);
        button_register = findViewById(R.id.button_register_login);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("action", "addfromlogin");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        });
        email = findViewById(R.id.email_login);
        pass = findViewById(R.id.password_login);

        email.getEditText().addTextChangedListener(this);
        pass.getEditText().addTextChangedListener(this);

        button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_student = email.getEditText().getText().toString().trim();
                pass_student = pass.getEditText().getText().toString().trim();
                signinStudent();
            }
        });

    }

    private void signinStudent() {
        dialog.show();
        mAuth.signInWithEmailAndPassword(email_student, pass_student).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "signInWithEmail:success");
                    Toast.makeText(LoginActivity.this, "Sign in Succesfull", Toast.LENGTH_SHORT).show();
                    mUser = mAuth.getCurrentUser();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.putExtra("data_student", mUser);
                    startActivity(intent);
                    finish();

                }else {
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                }
                dialog.cancel();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        email_student = email.getEditText().getText().toString().trim();
        pass_student = pass.getEditText().getText().toString().trim();
        if (!email_student.isEmpty()&&!pass_student.isEmpty()){
            button_login.setEnabled(true);
        }
        else {
            button_login.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
