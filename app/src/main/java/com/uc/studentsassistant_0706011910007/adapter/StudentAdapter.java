package com.uc.studentsassistant_0706011910007.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.studentsassistant_0706011910007.Glovar;
import com.uc.studentsassistant_0706011910007.R;
import com.uc.studentsassistant_0706011910007.ui.student.RegisterActivity;
import com.uc.studentsassistant_0706011910007.model.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.CardViewViewHolder> {
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Dialog dialog;
    DatabaseReference dbStudent;
    Student student;
    private Context context;
    private ArrayList<Student> listStudent;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

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
        dialog = Glovar.loadingDialog(context);
        dbStudent = FirebaseDatabase.getInstance().getReference("student");
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
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(klik);
                mAuth = FirebaseAuth.getInstance();
                new AlertDialog.Builder(context)
                        .setTitle("Konfirmasi")
                        .setIcon(R.drawable.ic_baseline_android_24)
                        .setMessage("Are you sure to delete "+student.getName()+" data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                        mAuth.signInWithEmailAndPassword(student.getEmail(), student.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                mAuth.getCurrentUser().delete();
                                                dbStudent.child(student.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                        Toast.makeText(context, "Delete success!", Toast.LENGTH_SHORT).show();
                                                        dialogInterface.cancel();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }, 2000);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
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
            gender = itemView.findViewById(R.id.start_course);
            age = itemView.findViewById(R.id.end_course);
            address = itemView.findViewById(R.id.student_address);
            edit = itemView.findViewById(R.id.edit_student);
            delete = itemView.findViewById(R.id.delete_student);
        }
    }
}
