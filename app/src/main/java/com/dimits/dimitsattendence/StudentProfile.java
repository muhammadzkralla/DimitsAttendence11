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
    TextView StudentName,txt_id,total_days_off;
    private int INITIAL_DAYS_OFF = 0;

    Bundle intent;
    RecyclerView recyclerView;
    SpecificAttendanceAdapter specificAttendanceAdapter;
    List<StudentAttendanceModel> studentAttendanceModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.medium_blue)));
        actionBar.setTitle(Html.fromHtml("<font color='#FF000000'>Dimits Attendance </font>"));
        Window window = StudentProfile.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.medium_blue));

         intent = getIntent().getExtras();


        if(intent != null){
            StudentName = findViewById(R.id.student_name);
            StudentName.setText(intent.getString("name"));
            txt_id = findViewById(R.id.student_id);
            txt_id.setText(intent.getString("id"));
            total_days_off = findViewById(R.id.total_days_off);
        }else {
            Toast.makeText(StudentProfile.this, "Something wrong", Toast.LENGTH_SHORT).show();
        }


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
       recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        studentAttendanceModelList = new ArrayList<>();
       specificAttendanceAdapter = new SpecificAttendanceAdapter(getApplicationContext(),studentAttendanceModelList);
       recyclerView.setAdapter(specificAttendanceAdapter);

       downloadReport();





    }


    private void downloadReport() {
        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClass.getName())
                .child("attendance")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                            while (iterator.hasNext()) {
                                DataSnapshot dataSnapshot1 = iterator.next();

                                StudentAttendanceModel studentAttendanceModel = dataSnapshot1.getValue(StudentAttendanceModel.class);
                                assert studentAttendanceModel != null;
                                if (studentAttendanceModel.getId().equals(intent.getString("id"))
                                        && studentAttendanceModel.getAttendance().equals("false")) {
                                    INITIAL_DAYS_OFF += 1;
                                    total_days_off.setText(" " + INITIAL_DAYS_OFF);

                                    studentAttendanceModelList.add(studentAttendanceModel);
                                }

                            }
                            specificAttendanceAdapter.notifyDataSetChanged();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }



}