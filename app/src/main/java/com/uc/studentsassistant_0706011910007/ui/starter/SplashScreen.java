package com.uc.studentsassistant_0706011910007.ui.starter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uc.studentsassistant_0706011910007.R;
import com.uc.studentsassistant_0706011910007.fragment.MainActivity;

public class SplashScreen extends AppCompatActivity {
    private static int splashtime = 2000;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mUser = mAuth.getCurrentUser();
        if (mUser!=null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, splashtime);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, StarterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, splashtime);
        }
    }
}
