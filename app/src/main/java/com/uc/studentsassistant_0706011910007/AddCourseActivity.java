package com.uc.studentsassistant_0706011910007;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.studentsassistant_0706011910007.model.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCourseActivity extends AppCompatActivity implements TextWatcher {
    Toolbar toolbar;
    Dialog dialog;
    TextView judul;
    Spinner spinnerDay, spinnerStart, spinnerEnd, spinnerLecturer;
    TextInputLayout course_subject;
    String subject, day, start, end, lecturer, action;
    Course course;
    private DatabaseReference dbCourse;
    FirebaseDatabase dbLecturer;
    ArrayAdapter<CharSequence> adapterEnd;
    Button button;
    List<String> names = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        dialog = Glovar.loadingDialog(AddCourseActivity.this);
        dbCourse = FirebaseDatabase.getInstance().getReference();
        toolbar = findViewById(R.id.toolbar_course);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbLecturer = FirebaseDatabase.getInstance();
        course_subject = findViewById(R.id.course_subject);
        spinnerDay = findViewById(R.id.spinner_day);
        spinnerStart = findViewById(R.id.spinner_time);
        spinnerEnd = findViewById(R.id.spinner_end);
        spinnerLecturer = findViewById(R.id.spinner_lecturer);
        judul = findViewById(R.id.textView14);
        button = findViewById(R.id.button_add_course);

        course_subject.getEditText().addTextChangedListener(this);

        ArrayAdapter<CharSequence> adapterDay = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.days, android.R.layout.simple_spinner_dropdown_item);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapterDay);

        ArrayAdapter<CharSequence> adapterStart = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_start_array, android.R.layout.simple_spinner_dropdown_item);
        adapterStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStart.setAdapter(adapterStart);

        spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterEnd = null;
                setSpinnerEnd(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if (action.equals("add")){
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AddCourseActivity.this, StarterActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            judul.setText("Add");
            button.setText("Add Course");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subject = course_subject.getEditText().getText().toString().trim();
                    day = spinnerDay.getSelectedItem().toString();
                    start = spinnerStart.getSelectedItem().toString();
                    end = spinnerEnd.getSelectedItem().toString();
                    lecturer = spinnerLecturer.getSelectedItem().toString();
                    addCourse(subject, day, start, end, lecturer);
                }
            });
        }else if (action.equalsIgnoreCase("edit")){
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AddCourseActivity.this, CourseData.class);
                    startActivity(intent);
                    finish();
                }
            });
            judul.setText("Edit");
            course = intent.getParcelableExtra("edit_course_data");
            course_subject.getEditText().setText(course.getSubject());
            final int dayPos = adapterDay.getPosition(course.getDay());
            spinnerDay.setSelection(dayPos);
            final int startPos = adapterStart.getPosition(course.getStart());
            spinnerStart.setSelection(startPos);
            setSpinnerEnd(startPos);
            final int endPos = adapterEnd.getPosition(course.getEnd());
            spinnerEnd.setSelection(endPos);
            Log.d("end", course.getEnd());
            Log.d("ends", String.valueOf(endPos));

            button.setText("Edit Course");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                    subject = course_subject.getEditText().getText().toString().trim();
                    day = spinnerDay.getSelectedItem().toString();
                    start = spinnerStart.getSelectedItem().toString();
                    end = spinnerEnd.getSelectedItem().toString();
                    lecturer = spinnerLecturer.getSelectedItem().toString();
                    Map<String, Object> params = new HashMap<>();
                    params.put("subject", subject);
                    params.put("day", day);
                    params.put("start", start);
                    params.put("end", end);
                    params.put("lecturer", lecturer);
                    dbCourse.child("course").child(course.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.cancel();
                            Intent intent = new Intent(AddCourseActivity.this, CourseData.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourseActivity.this);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    });
                }
            });
        }

        showSpinnerLecturer();

    }

//    insert lecturer to spinner
    private void showSpinnerLecturer() {
        dbCourse.child("lecturer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot:snapshot.getChildren()){
                    String spinnerName = childSnapshot.child("name").getValue(String.class);
                    names.add(spinnerName);
                    Log.d("lecturer", spinnerName);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddCourseActivity.this, android.R.layout.simple_spinner_dropdown_item, names);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerLecturer.setAdapter(arrayAdapter);
                if (action.equalsIgnoreCase("edit")){
                    int lecturerPos = arrayAdapter.getPosition(course.getLecturer());
                    spinnerLecturer.setSelection(lecturerPos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addCourse(String subject, String day, String start, String end, String lecturer) {
        dialog.show();
        String mid = dbCourse.child("course").push().getKey();
        Course course = new Course(mid, subject, day, start, end, lecturer);
        dbCourse.child("course").child(mid).setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.cancel();
                Toast.makeText(AddCourseActivity.this, "Add Course Succesfully", Toast.LENGTH_SHORT).show();
                course_subject.getEditText().setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.cancel();
                Toast.makeText(AddCourseActivity.this, "Add Course Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinnerEnd(int i) {
        if(i==0){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end0730, android.R.layout.simple_spinner_item);
        }else if(i==1){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end0800, android.R.layout.simple_spinner_item);
        }else if(i==2){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end0830, android.R.layout.simple_spinner_item);
        }else if(i==3){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end0900, android.R.layout.simple_spinner_item);
        }else if(i==4){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end0930, android.R.layout.simple_spinner_item);
        }else if(i==5){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1000, android.R.layout.simple_spinner_item);
        }else if(i==6){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1030, android.R.layout.simple_spinner_item);
        }else if(i==7){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1100, android.R.layout.simple_spinner_item);
        }else if(i==8){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1130, android.R.layout.simple_spinner_item);
        }else if(i==9){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1200, android.R.layout.simple_spinner_item);
        }else if(i==10){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1230, android.R.layout.simple_spinner_item);
        }else if(i==11){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1300, android.R.layout.simple_spinner_item);
        }else if(i==12){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1330, android.R.layout.simple_spinner_item);
        }else if(i==13){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1400, android.R.layout.simple_spinner_item);
        }else if(i==14){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1430, android.R.layout.simple_spinner_item);
        }else if(i==15){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1500, android.R.layout.simple_spinner_item);
        }else if(i==16){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1530, android.R.layout.simple_spinner_item);
        }else if(i==17){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1600, android.R.layout.simple_spinner_item);
        }else if(i==18){
            adapterEnd = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1630, android.R.layout.simple_spinner_item);
        }

        adapterEnd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEnd.setAdapter(adapterEnd);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        subject = course_subject.getEditText().getText().toString().trim();
        if (!subject.isEmpty()){
            button.setEnabled(true);
        }else {
            button.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.course_list){
            Intent intent = new Intent(AddCourseActivity.this, CourseData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddCourseActivity.this, StarterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}
