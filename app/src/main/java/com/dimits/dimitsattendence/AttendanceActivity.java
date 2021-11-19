package com.dimits.dimitsattendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;

import com.dimits.dimitsattendence.adapter.AttendanceAdapter;
import com.dimits.dimitsattendence.adapter.StudentsAdapter;
import com.dimits.dimitsattendence.common.Common;
import com.dimits.dimitsattendence.model.StudentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {
    // initialize the variables
    RecyclerView recyclerView;
    AttendanceAdapter adapter;
    List<StudentModel> studentModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        //getSupportActionBar() to change the color and text color of the Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.medium_blue)));
        actionBar.setTitle(Html.fromHtml("<font color='#FF000000'>Dimits Attendance </font>"));
        //change the color of the status bar
        Window window = AttendanceActivity.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.medium_blue));
        // declare the variables and set the layout manager
        recyclerView = (RecyclerView) findViewById(R.id.recycler_attendance);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        // download the students will show in the activity
        downloadStudents();
    }
    private void downloadStudents() {
        // prepare the List of StudentModel to be filled
        studentModels = new ArrayList<>();

        // refer to the date in firebase
        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClass.getName()).child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Avoid the null pointer exception
                if (snapshot.exists()){

                    // clear the list to avoid duplication
                    studentModels.clear();
                    // for each student under the reference
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        // store the data one by one and pass the data to the adapter
                        StudentModel studentModel = dataSnapshot.getValue(StudentModel.class);
                        studentModels.add(studentModel);
                        adapter = new AttendanceAdapter(AttendanceActivity.this,studentModels);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}