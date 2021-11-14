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
    RecyclerView recyclerView;
    AttendanceAdapter adapter;
    List<StudentModel> studentModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.medium_blue)));
        actionBar.setTitle(Html.fromHtml("<font color='#FF000000'>Dimits Attendance </font>"));
        Window window = AttendanceActivity.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.medium_blue));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_attendance);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        downloadStudents();
    }
    private void downloadStudents() {
        studentModels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClass.getName()).child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    studentModels.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
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