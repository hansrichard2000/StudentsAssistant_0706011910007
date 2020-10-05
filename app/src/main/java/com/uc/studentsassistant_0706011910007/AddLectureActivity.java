package com.uc.studentsassistant_0706011910007;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.studentsassistant_0706011910007.model.Lecturer;

import java.util.HashMap;
import java.util.Map;

public class AddLectureActivity extends AppCompatActivity implements TextWatcher {
    Toolbar toolbar;
    TextInputLayout lecturer_name, lecturer_expert;
    TextView textView;
    RadioGroup rg_gender;
    RadioButton radioButton;
    Button button;
    Lecturer lecturer;
    String name, expertise, gender, action;
    Dialog dialog;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecturer);
        dialog = Glovar.loadingDialog(AddLectureActivity.this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        toolbar = findViewById(R.id.toolbar_lecturer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddLectureActivity.this, StarterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        lecturer_name = findViewById(R.id.lecturer_name);
        lecturer_expert = findViewById(R.id.lecturer_expert);
        lecturer_name.getEditText().addTextChangedListener(this);
        lecturer_expert.getEditText().addTextChangedListener(this);
        button = findViewById(R.id.button_add_lecturer);
        rg_gender = findViewById(R.id.rg_gender);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = findViewById(i);
                gender = radioButton.getText().toString();
//                Toast.makeText(AddLectureActivity.this, "Add"+gender, Toast.LENGTH_SHORT).show();
            }
        });
        textView = findViewById(R.id.add_string);
        final Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if (action.equals("add")){
            textView.setText("Add");
            button.setText("Add Lecturer");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    name = lecturer_name.getEditText().getText().toString().trim();
                    expertise = lecturer_expert.getEditText().getText().toString().trim();
                    addLecturer(name, gender, expertise);
                }
            });
        }else {
            textView.setText("Edit");
            button.setText("Edit Lecturer");
            lecturer = intent.getParcelableExtra("edit_data_lect");
            lecturer_name.getEditText().setText(lecturer.getName());
            lecturer_expert.getEditText().setText(lecturer.getExpertise());
            if (lecturer.getGender().equalsIgnoreCase("male")){
                rg_gender.check(R.id.radioMale);
            }else {
                rg_gender.check(R.id.radioFemale);
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                    name = lecturer_name.getEditText().getText().toString().trim();
                    expertise = lecturer_expert.getEditText().getText().toString().trim();
                    Map<String,Object> params = new HashMap<>();
                    params.put("name", name);
                    params.put("expertise", expertise);
                    params.put("gender", gender);
                    mDatabase.child("lecturer").child(lecturer.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.cancel();
                            Intent intent1;
                            intent1 = new Intent(AddLectureActivity.this, LecturerData.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLectureActivity.this);
                            startActivity(intent1, options.toBundle());
                            finish();
                        }
                    });
                }
            });
        }
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addLecturer(name, gender, expertise);
//            }
//        });
    }

    private void addLecturer(String name, String gender, String expertise) {
        dialog.show();
        String mid = mDatabase.child("lecturer").push().getKey();
        Lecturer lecturer = new Lecturer(mid, name, gender, expertise);
        mDatabase.child("lecturer").child(mid).setValue(lecturer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.cancel();
                Toast.makeText(AddLectureActivity.this, "Add Lecturer Success", Toast.LENGTH_SHORT).show();
                lecturer_name.getEditText().setText("");
                lecturer_expert.getEditText().setText("");
                rg_gender.check(R.id.radioMale);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.cancel();
                Toast.makeText(AddLectureActivity.this, "Add Lecturer Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        name = lecturer_name.getEditText().getText().toString().trim();
        expertise = lecturer_expert.getEditText().getText().toString().trim();
        if (!name.isEmpty() && !expertise.isEmpty()){
            button.setEnabled(true);
        }
        else {
            button.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddLectureActivity.this, StarterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lecturer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.lecturer_list){
            Intent intent;
            intent = new Intent(AddLectureActivity.this, LecturerData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLectureActivity.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
