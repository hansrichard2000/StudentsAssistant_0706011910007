package com.uc.studentsassistant_0706011910007.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uc.studentsassistant_0706011910007.R;
import com.uc.studentsassistant_0706011910007.model.Lecturer;

import java.util.ArrayList;

public class LecturerAdapter extends RecyclerView.Adapter<LecturerAdapter.CardViewViewHolder> {
    private Context context;
    private ArrayList<Lecturer> listLecturer;

    public LecturerAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Lecturer> getListLecturer() {
        return listLecturer;
    }

    public void setListLecturer(ArrayList<Lecturer> listLecturer) {
        this.listLecturer = listLecturer;
    }

    @NonNull
    @Override
    public LecturerAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_adapter, parent, false);
        return new LecturerAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LecturerAdapter.CardViewViewHolder holder, int position) {
        final Lecturer lecturer = getListLecturer().get(position);
        holder.name.setText(lecturer.getName());
        holder.gender.setText(lecturer.getGender());
        holder.expert.setText(lecturer.getExpertise());
    }

    @Override
    public int getItemCount() {
        return getListLecturer().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        TextView name, gender, expert;
        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.lbl_name_lecturer);
            gender = itemView.findViewById(R.id.lbl_gender_lecturer);
            expert = itemView.findViewById(R.id.lbl_expert_lecturer);
        }
    }
}