package com.uc.studentsassistant_0706011910007.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.studentsassistant_0706011910007.R;
import com.uc.studentsassistant_0706011910007.adapter.ScheduleAdapter;
import com.uc.studentsassistant_0706011910007.model.Course;

import java.util.ArrayList;

public class FragmentScheduleActivity extends Fragment {

    RecyclerView rv_schedule_list;
    DatabaseReference dbSchedule;
    ArrayList<Course> listCourse = new ArrayList<>();

    public FragmentScheduleActivity(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_schedule, container, false);

        dbSchedule = FirebaseDatabase.getInstance()
                .getReference("student")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("course");

        dbSchedule.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCourse.clear();
                rv_schedule_list.setAdapter(null);
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    Course course = childSnapshot.getValue(Course.class);
                    listCourse.add(course);
                }
                showScheduleList(listCourse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rv_schedule_list = view.findViewById(R.id.rv_schedule);

        return view;
    }

    private void showScheduleList(ArrayList<Course> listCourse) {
        rv_schedule_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(getActivity());
        scheduleAdapter.setListCourse(listCourse);
        rv_schedule_list.setAdapter(scheduleAdapter);
    }
}