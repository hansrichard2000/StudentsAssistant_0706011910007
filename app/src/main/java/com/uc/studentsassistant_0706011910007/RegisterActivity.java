package com.uc.studentsassistant_0706011910007;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.studentsassistant_0706011910007.adapter.StudentAdapter;
import com.uc.studentsassistant_0706011910007.model.Student;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements TextWatcher {
    ImageButton button;
    Dialog dialog;
    RadioGroup rg_student;
    RadioButton radioButton;
    Button btn_register;
    TextView textView;
    TextInputLayout user_email, user_pass, user_name, user_nim, user_age, user_address;
    String uid, email, pass, name, nim, gender, age, address;
    String action;
    Student student;
    Toolbar toolbar;
    DatabaseReference mDatabase, getmDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar = findViewById(R.id.toolbar_register_student);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = Glovar.loadingDialog(RegisterActivity.this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("student");
        getmDatabase = FirebaseDatabase.getInstance().getReference();

        user_email = findViewById(R.id.user_email);
        user_pass = findViewById(R.id.user_pass);
        user_name = findViewById(R.id.user_name);
        user_nim = findViewById(R.id.user_nim);
        user_age = findViewById(R.id.user_age);
        user_address = findViewById(R.id.user_address);

        user_email.getEditText().addTextChangedListener(this);
        user_pass.getEditText().addTextChangedListener(this);
        user_name.getEditText().addTextChangedListener(this);
        user_nim.getEditText().addTextChangedListener(this);
        user_age.getEditText().addTextChangedListener(this);
        user_address.getEditText().addTextChangedListener(this);

        btn_register = findViewById(R.id.button_register_student);
        rg_student = findViewById(R.id.rg_student);
        rg_student.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = findViewById(i);
                gender = radioButton.getText().toString();
            }
        });
        textView = findViewById(R.id.register_string);
        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if (action.equalsIgnoreCase("add")){
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RegisterActivity.this, StarterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            });
            textView.setText("Register");
            btn_register.setText("Register Student");
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    email = user_email.getEditText().getText().toString().trim();
                    pass = user_pass.getEditText().getText().toString().trim();
                    name = user_name.getEditText().getText().toString().trim();
                    nim = user_nim.getEditText().getText().toString().trim();
                    address = user_address.getEditText().getText().toString().trim();
                    age = user_age.getEditText().getText().toString().trim();
                    addStudent();
                }
            });
        }else if(action.equalsIgnoreCase("edit")){
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RegisterActivity.this, StudentAdapter.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            });
            user_age.getEditText().setText("");
            textView.setText("Edit");
            btn_register.setText("Edit Student");
            student = intent.getParcelableExtra("edit_student_data");
            user_name.getEditText().setText(student.getName());
            user_pass.getEditText().setText(student.getPassword());
            user_pass.getEditText().setEnabled(false);
            user_email.getEditText().setText(student.getEmail());
            user_email.getEditText().setEnabled(false);
            user_nim.getEditText().setText(student.getNim());
            user_address.getEditText().setText(student.getAddress());
            user_age.getEditText().setText(student.getAge());
            if (student.getGender().equalsIgnoreCase("male")){
                rg_student.check(R.id.male_gender);
            }else{
                rg_student.check(R.id.female_gender);
            }
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                    name = user_name.getEditText().getText().toString().trim();
                    nim = user_nim.getEditText().getText().toString().trim();
                    address = user_address.getEditText().getText().toString().trim();
                    age = user_age.getEditText().getText().toString().trim();
                    Map<String,Object> params = new HashMap<>();
                    params.put("name", name);
                    params.put("nim", nim);
                    params.put("gender", gender);
                    params.put("address", address);
                    params.put("age", age);
                    getmDatabase.child("student").child(student.getUid()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.cancel();
                            Intent intent1 = new Intent(RegisterActivity.this, StudentData.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this);
                            startActivity(intent1, options.toBundle());
                            finish();
                        }
                    });
                }
            });
        }
    }

    private void addStudent() {
        dialog.show();
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    dialog.cancel();
                    uid = mAuth.getCurrentUser().getUid();
                    student = new Student(uid, email, pass, name, nim, gender, age, address);
                    mDatabase.child(uid).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegisterActivity.this, "Student Register Succesfull", Toast.LENGTH_SHORT).show();
                            user_email.getEditText().setText("");
                            user_pass.getEditText().setText("");
                            user_name.getEditText().setText("");
                            user_nim.getEditText().setText("");
                            user_address.getEditText().setText("");
                            user_age.getEditText().setText("");
                        }
                    });
                    mAuth.signOut();
                }else {
                    try {
                        throw task.getException();
                    }catch(FirebaseAuthInvalidCredentialsException malFormed){
                        Toast.makeText(RegisterActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                    }catch(FirebaseAuthUserCollisionException existEmail){
                        Toast.makeText(RegisterActivity.this, "Email already registered!", Toast.LENGTH_SHORT).show();
                    }catch (Exception e) {
                        Toast.makeText(RegisterActivity.this, "Register failed!", Toast.LENGTH_SHORT).show();
                    }
                    dialog.cancel();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.student_list){
            Intent intent;
            intent = new Intent(RegisterActivity.this, StudentData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        email = user_email.getEditText().getText().toString().trim();
        pass = user_pass.getEditText().getText().toString().trim();
        name = user_name.getEditText().getText().toString().trim();
        nim = user_nim.getEditText().getText().toString().trim();
        address = user_address.getEditText().getText().toString().trim();
        age = user_age.getEditText().getText().toString().trim();
        if (!name.isEmpty() && !email.isEmpty()&& !pass.isEmpty()){
            btn_register.setEnabled(true);
        }else{
            btn_register.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this);
        startActivity(intent, options.toBundle());
        finish();
    }
}
