package com.dimits.dimitsattendence;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.dimits.dimitsattendence.adapter.SpecificAttendanceAdapter;
import com.dimits.dimitsattendence.common.Common;
import com.dimits.dimitsattendence.model.StudentAttendanceModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StudentProfile extends AppCompatActivity {
    // initialize the variables
    TextView StudentName,txt_id,total_days_off;

    // this variable is the initial days off of the student that we'll add their off days on
    private int INITIAL_DAYS_OFF = 0;

    // to get the data we passed here
    Bundle intent;

    RecyclerView recyclerView;
    SpecificAttendanceAdapter specificAttendanceAdapter;
    List<StudentAttendanceModel> studentAttendanceModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        // getSupportActionBar() to change the color and text color of the Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.medium_blue)));
        actionBar.setTitle(Html.fromHtml("<font color='#FF000000'>Dimits Attendance </font>"));
        // change the color of the status bar
        Window window = StudentProfile.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.medium_blue));
        // declaring the variables and setting recycler view's layout manager and adapter
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(specificAttendanceAdapter);
        specificAttendanceAdapter = new SpecificAttendanceAdapter(getApplicationContext(),studentAttendanceModelList);




        // getting the data we passed from the StudentsAdapter
        intent = getIntent().getExtras();


        // always avoid the nullPointerExceptions
        if(intent != null){
            // declaring the views and assigning their values
            StudentName = findViewById(R.id.student_name);
            StudentName.setText(intent.getString("name"));
            txt_id = findViewById(R.id.student_id);
            txt_id.setText(intent.getString("id"));
            total_days_off = findViewById(R.id.total_days_off);
        }else {
            // we hope this code never executes :)
            Toast.makeText(StudentProfile.this, "Something wrong", Toast.LENGTH_SHORT).show();
        }
        // download the attendance reports uploaded before
       downloadReport();

    }


    private void downloadReport() {
        // refer to the Correct Reference
        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClass.getName())
                .child("attendance")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // you already guessed it :)
                        if (snapshot.exists()) {
                            // for each item under the reference "attendance" do the following :
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                // iterator to iterate over each object under the refernce
                                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                while (iterator.hasNext()) {
                                    // declaring the data and storing them in a StudentAttendanceModel object
                                    DataSnapshot dataSnapshot1 = iterator.next();
                                    StudentAttendanceModel studentAttendanceModel = dataSnapshot1.getValue(StudentAttendanceModel.class);

                                    // spend all your life avoiding nullPointerExceptions
                                    assert studentAttendanceModel != null;
                                    // download only the days off for this student
                                    if (studentAttendanceModel.getId().equals(intent.getString("id"))
                                            && studentAttendanceModel.getAttendance().equals("false")) {

                                        // add their value to 0 and setting the TextView of the days off
                                        INITIAL_DAYS_OFF += 1;
                                        total_days_off.setText(" " + INITIAL_DAYS_OFF);

                                        // add this object to the list
                                        studentAttendanceModelList.add(studentAttendanceModel);
                                    }

                                }
                                specificAttendanceAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }



}