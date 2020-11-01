package com.uc.studentsassistant_0706011910007.ui.student;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.studentsassistant_0706011910007.R;
import com.uc.studentsassistant_0706011910007.adapter.StudentAdapter;
import com.uc.studentsassistant_0706011910007.model.Student;

import java.util.ArrayList;

public class StudentData extends AppCompatActivity {
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Toolbar toolbar;
    ArrayList<Student> listStudent = new ArrayList<>();
    RecyclerView recyclerView;
    private DatabaseReference dbStudent;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_data);
        toolbar = findViewById(R.id.toolbar_student_data);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentData.this, RegisterActivity.class);
                intent.putExtra("action", "add");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentData.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        });
        dbStudent = FirebaseDatabase.getInstance().getReference("student");
        recyclerView = findViewById(R.id.student_list_data);
        fetchStudentData();
    }

    private void fetchStudentData() {
        dbStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listStudent.clear();
                recyclerView.setAdapter(null);
                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    Student student = childSnapshot.getValue(Student.class);
                    listStudent.add(student);
                }
                showStudentData(listStudent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showStudentData(final ArrayList<Student> list) {
        recyclerView.setLayoutManager(new LinearLayoutManager(StudentData.this));
        StudentAdapter studentAdapter = new StudentAdapter(StudentData.this);
        studentAdapter.setListStudent(list);
        recyclerView.setAdapter(studentAdapter);

    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(StudentData.this, RegisterActivity.class);
        intent.putExtra("action", "add");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentData.this);
        startActivity(intent, options.toBundle());
        finish();
    }
}
