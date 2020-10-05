package com.uc.studentsassistant_0706011910007;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StarterActivity extends AppCompatActivity {
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    private long backPressedTime;
    private Toast backToast;
    CardView cardViewAddStudent, cardViewLecturer, cardViewCourse, cardViewLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        cardViewAddStudent = findViewById(R.id.add_student);
        cardViewLecturer = findViewById(R.id.add_lecturer);
        cardViewCourse = findViewById(R.id.add_course);
        cardViewLogin = findViewById(R.id.login_as_student);
        cardViewAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(klik);
                Intent intent = new Intent(StarterActivity.this, RegisterActivity.class);
                intent.putExtra("action", "add");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        });
        cardViewLecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(klik);
                Intent intent = new Intent(StarterActivity.this, AddLectureActivity.class);
                intent.putExtra("action", "add");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        });
        cardViewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StarterActivity.this, AddCourseActivity.class);
                startActivity(intent);
                finish();
            }
        });
        cardViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StarterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed(){
        if (backPressedTime + 2000 > System.currentTimeMillis() ){
            backToast.cancel();
            super.onBackPressed();
            return;
        }
        else{
            backToast= Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
