//package com.experia.experia.activities;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import com.experia.experia.R;
//
//import java.util.ArrayList;
//
//import adapters.ExperienceAdapter;
//import models.ExperienceTestArray;
//
///**
// * Created by doc_dungeon on 8/20/16.
// */
//public class ExperienceCardActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_experience);
//
//        final RecyclerView rvUsers = (RecyclerView) findViewById(R.id.rvUsers);
//
//        ArrayList<ExperienceTestArray> users = ExperienceTestArray.getUserList();
//
//        ExperienceAdapter adapter = new ExperienceAdapter(users);
//
//        // Set adapter to RecyclerView
//        rvUsers.setAdapter(adapter);
//
//        rvUsers.setLayoutManager(new LinearLayoutManager(this));
//    }
//
//}
