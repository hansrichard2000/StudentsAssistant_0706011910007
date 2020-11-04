package com.uc.studentsassistant_0706011910007.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.studentsassistant_0706011910007.Glovar;
import com.uc.studentsassistant_0706011910007.R;
import com.uc.studentsassistant_0706011910007.model.Course;

import java.util.ArrayList;

public class ListCourseAdapter extends RecyclerView.Adapter<ListCourseAdapter.CardViewViewHolder> {

    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Dialog dialog;
    private DatabaseReference mDatabase, dbCourse;
    FirebaseUser mAuth;
    private Context context;
    private ArrayList<Course> listCourse;
    boolean conflict = false;

    MutableLiveData<Course> cekCourse = new MutableLiveData<>();

    public MutableLiveData<Course> getCekCourse() {
        return cekCourse;
    }

    public ListCourseAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Course> getListCourse() {
        return listCourse;
    }

    public void setListCourse(ArrayList<Course> listCourse) {
        this.listCourse = listCourse;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_course_adapter, parent, false);
        return new ListCourseAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder holder, int position) {
        dialog = Glovar.loadingDialog(context);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Course course = getListCourse().get(position);
        holder.name.setText(course.getSubject());
        holder.lecturer.setText(course.getLecturer());
        holder.day.setText(course.getDay());
        holder.start.setText(course.getStart());
        holder.end.setText(" - " + course.getEnd());
        holder.enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(klik);
                conflictChecker(course);
            }
        });
    }

    private void conflictChecker(final Course course) {
        final int courseStartChoice = Integer.parseInt(course.getStart().replace(":", ""));
        final int courseEndChoice = Integer.parseInt(course.getEnd().replace(":", ""));

        Log.d("chosenDay", course.getDay());
        Log.d("courseStartChoice",courseStartChoice+"");
        Log.d("courseEndChoice",courseEndChoice+"");

        dbCourse = FirebaseDatabase.getInstance().getReference("student");
        dbCourse.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("course")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        conflict = false;
                        for (DataSnapshot childSnapshot : snapshot.getChildren()){
                            Course course1 = childSnapshot.getValue(Course.class);
                            int courseStart = Integer.parseInt(course1.getStart().replace(":", ""));
                            int courseEnd = Integer.parseInt(course1.getEnd().replace(":", ""));

                            Log.d("CourseDay", course1.getDay());
                            Log.d("CourseStart",courseStart+"");
                            Log.d("CourseEnd",courseEnd+"");

                            if (course.getDay().equalsIgnoreCase(course1.getDay())){
                                if (courseStartChoice>=courseStart && courseStartChoice<courseEnd){
                                    conflict = true;
                                    break;
                                }
                                if (courseEndChoice>courseStart && courseEndChoice<=courseEnd){
                                    conflict = true;
                                    break;
                                }
                            }else {
                                conflict = false;
                                Log.d("CheckFalse","False");
                            }
                        }

                        Log.d("Check",conflict+"");

                        if (conflict){
                            new AlertDialog.Builder(context)
                                    .setTitle("Warning")
                                    .setIcon(R.drawable.ic_baseline_android_24)
                                    .setMessage("Course Schedule conflict with others!")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    })
                                    .create()
                                    .show();

                        }else {
                            cekCourse.setValue(course);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return getListCourse().size();
    }


    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        TextView name, lecturer, day, start, end;
        Button enroll;
        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.course_list_name);
            lecturer = itemView.findViewById(R.id.course_list_lecturer);
            day = itemView.findViewById(R.id.course_list_day);
            start = itemView.findViewById(R.id.start_list_course);
            end = itemView.findViewById(R.id.end_list_course);
            enroll = itemView.findViewById(R.id.button_enroll);
        }
    }
}
