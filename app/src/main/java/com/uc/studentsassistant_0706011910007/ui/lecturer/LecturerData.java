package com.uc.studentsassistant_0706011910007.ui.lecturer;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.studentsassistant_0706011910007.ItemClickSupport;
import com.uc.studentsassistant_0706011910007.R;
import com.uc.studentsassistant_0706011910007.adapter.LecturerAdapter;
import com.uc.studentsassistant_0706011910007.model.Lecturer;

import java.util.ArrayList;

public class LecturerData extends AppCompatActivity {
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Toolbar toolbar;
    DatabaseReference dbLecturer;
    ArrayList<Lecturer>listLecturer = new ArrayList<>();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_data);
        toolbar = findViewById(R.id.toolbar_lecturer_data);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LecturerData.this, AddLectureActivity.class);
                intent.putExtra("action", "add");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerData.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        });
        dbLecturer = FirebaseDatabase.getInstance().getReference("lecturer");
        recyclerView = findViewById(R.id.rv_lecturer);
        fetchLecturerData();
    }

    public void fetchLecturerData(){
        dbLecturer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLecturer.clear();
                recyclerView.setAdapter(null);
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    Lecturer lecturer = childSnapshot.getValue(Lecturer.class);
                    listLecturer.add(lecturer);
                }
                showLecturerData(listLecturer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showLecturerData(final ArrayList<Lecturer> list){
        recyclerView.setLayoutManager(new LinearLayoutManager(LecturerData.this));
        LecturerAdapter lecturerAdapter = new LecturerAdapter(LecturerData.this);
        lecturerAdapter.setListLecturer(list);
        recyclerView.setAdapter(lecturerAdapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                v.startAnimation(klik);
                Intent intent = new Intent(LecturerData.this, LecturerDetail.class);
                Lecturer lecturer = new Lecturer(list.get(position).getId(), list.get(position).getName(), list.get(position).getGender(), list.get(position).getExpertise());
                intent.putExtra("data_lecturer", lecturer);
                intent.putExtra("position", position);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerData.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(LecturerData.this, AddLectureActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerData.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(LecturerData.this, AddLectureActivity.class);
        intent.putExtra("action", "add");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerData.this);
        startActivity(intent, options.toBundle());
        finish();
    }

}
