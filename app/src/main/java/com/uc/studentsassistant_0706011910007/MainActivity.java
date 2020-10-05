package com.uc.studentsassistant_0706011910007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uc.studentsassistant_0706011910007.fragment.FragmentAccountActivity;
import com.uc.studentsassistant_0706011910007.fragment.FragmentCourseActivity;
import com.uc.studentsassistant_0706011910007.fragment.FragmentScheduleActivity;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    Fragment fragment;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main,new FragmentScheduleActivity()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.schedule_page:
                            toolbar.setTitle("Schedule");
                            setSupportActionBar(toolbar);
                            fragment = new FragmentScheduleActivity();
                            loadFragment(fragment);
                            return true;
                        case R.id.course_page:
                            toolbar.setTitle("Courses");
                            setSupportActionBar(toolbar);
                            fragment = new FragmentCourseActivity();
                            loadFragment(fragment);
                            return true;
                        case R.id.account_page:
                            toolbar.setTitle("My Account");
                            setSupportActionBar(toolbar);
                            fragment = new FragmentAccountActivity();
                            loadFragment(fragment);
                            return true;
                    }
                    return false;
                }
            };

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    public void onBackPressed() {
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