package com.uc.studentsassistant_0706011910007.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uc.studentsassistant_0706011910007.LoginActivity;
import com.uc.studentsassistant_0706011910007.R;
import com.uc.studentsassistant_0706011910007.model.Student;

public class FragmentAccountActivity extends Fragment {
    TextView account_name, account_nim, account_email, account_gender, account_age, account_address;
    Button button_logout;
    String name, nim, email, gender, age, address;
    Student student;
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
        mAuth = FirebaseAuth.getInstance();
        account_name = view.findViewById(R.id.account_name);
        account_nim = view.findViewById(R.id.account_nim);
        account_email = view.findViewById(R.id.account_email);
        account_gender = view.findViewById(R.id.account_gender);
        account_age = view.findViewById(R.id.account_age);
        account_address = view.findViewById(R.id.account_address);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        account_name.setText(mUser.getEmail());
//        account_nim.setText(student.getNim());
//        account_email.setText(student.getEmail());
//        account_gender.setText(student.getNim());
//        account_age.setText(student.getNim());
//        account_address.setText(student.getNim());

        button_logout = view.findViewById(R.id.button_logout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                getActivity().finish();
            }
        });
    }
}