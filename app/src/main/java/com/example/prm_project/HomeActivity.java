package com.example.prm_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;


public class HomeActivity extends AppCompatActivity {
    //ImageView home_button;
    ImageView addFood_button;
    ImageView caculateBmi_button;
    ImageView history_button;
    //ImageView recipe_button;
    ImageView profile_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //home button
        //click button home để load intent home ????
        /*home_button = (ImageView) findViewById(R.id.home_button);
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
        //addFood Button
        addFood_button = (ImageView) findViewById(R.id.addFood_button);
        addFood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,CaloriesAcitivity.class);
                startActivity(intent);
            }
        });
        //caculateBMI
        caculateBmi_button =(ImageView) findViewById(R.id.calculateBmi_button);
        caculateBmi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeActivity.this,BmiActivity.class);
                startActivity(intent);

            }
        });
        //historyButton
            history_button = (ImageView) findViewById(R.id.history_button);
            history_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(HomeActivity.this,HistoryActivity.class);
                    startActivity(intent);
                }
            });
        //RecipeButton(under develop)
            /*recipe_button = (ImageView) findViewById(R.id.recipe_button);
            recipe_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //still under develope
                }
            });*/
        //ProfileButton(under develope)
            profile_button = (ImageView) findViewById(R.id.profile_button);
            profile_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //still under develope
                    viewProfile();
                }
            });
            }

    private void viewProfile() {
        Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }
}
