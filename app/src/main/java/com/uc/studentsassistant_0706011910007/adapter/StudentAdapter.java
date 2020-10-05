package com.uc.studentsassistant_0706011910007.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uc.studentsassistant_0706011910007.R;
import com.uc.studentsassistant_0706011910007.RegisterActivity;
import com.uc.studentsassistant_0706011910007.StarterActivity;
import com.uc.studentsassistant_0706011910007.model.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.CardViewViewHolder> {
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    private Context context;
    private ArrayList<Student> listStudent;

    public StudentAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Student> getListStudent() {
        return listStudent;
    }

    public void setListStudent(ArrayList<Student> listStudent) {
        this.listStudent = listStudent;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_adapter, parent, false);
        return new StudentAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder holder, int position) {
        final Student student = getListStudent().get(position);
        holder.name.setText(student.getName());
        holder.nim.setText(student.getNim());
        holder.email.setText(student.getEmail());
        holder.gender.setText(student.getGender());
        holder.age.setText(" / "+student.getAge()+" yo");
        holder.address.setText(student.getAddress());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(klik);
                Intent intent = new Intent(context, RegisterActivity.class);
                intent.putExtra("action", "edit");
                intent.putExtra("edit_student_data", student);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return getListStudent().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        TextView name, nim, email, gender, age, address;
        ImageButton edit, delete;
        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.student_name);
            nim = itemView.findViewById(R.id.student_nim);
            email = itemView.findViewById(R.id.student_email);
            gender = itemView.findViewById(R.id.student_gender);
            age = itemView.findViewById(R.id.student_age);
            address = itemView.findViewById(R.id.student_address);
            edit = itemView.findViewById(R.id.edit_student);
            delete = itemView.findViewById(R.id.delete_student);
        }
    }
}
