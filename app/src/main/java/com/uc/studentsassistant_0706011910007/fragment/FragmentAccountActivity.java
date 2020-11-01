package com.uc.studentsassistant_0706011910007.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.studentsassistant_0706011910007.Glovar;
import com.uc.studentsassistant_0706011910007.ui.login.LoginActivity;
import com.uc.studentsassistant_0706011910007.R;
import com.uc.studentsassistant_0706011910007.model.Student;

public class FragmentAccountActivity extends Fragment {
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    TextView account_name, account_nim, account_email, account_gender, account_age, account_address;
    Button button_logout;
    String name, nim, email, gender, age, address;
    Student student;
    DatabaseReference dbReference;
    Dialog dialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public FragmentAccountActivity(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = Glovar.loadingDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        account_name = view.findViewById(R.id.account_name);
        account_nim = view.findViewById(R.id.account_nim);
        account_email = view.findViewById(R.id.account_email);
        account_gender = view.findViewById(R.id.account_gender);
        account_age = view.findViewById(R.id.account_age);
        account_address = view.findViewById(R.id.account_address);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("student").child(mUser.getUid());

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                student = snapshot.getValue(Student.class);
                if (student!=null){
                    account_name.setText(student.getName());
                    account_nim.setText(student.getNim());
                    account_email.setText(student.getEmail());
                    account_gender.setText(student.getGender());
                    account_age.setText(student.getAge());
                    account_address.setText(student.getAddress());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button_logout = view.findViewById(R.id.button_logout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(klik);
                new AlertDialog.Builder(getActivity())
                        .setTitle("Alert")
                        .setIcon(R.drawable.ic_baseline_android_24)
                        .setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                        mAuth.signOut();
                                        Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent1);
                                        getActivity().finish();
                                        Toast.makeText(getActivity(), "Logout successful", Toast.LENGTH_SHORT).show();
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
}