package com.dimits.dimitsattendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dimits.dimitsattendence.adapter.SpecificAttendanceAdapter;
import com.dimits.dimitsattendence.common.Common;
import com.dimits.dimitsattendence.model.StudentAttendanceModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SpecificAttendanceReport extends AppCompatActivity {
    RecyclerView recyclerView;
    List<StudentAttendanceModel> studentAttendanceModels;
    SpecificAttendanceAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_attendance_report);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_specific);
        recyclerView.setHasFixedSize(true);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        downloadReport();
    }

    private void downloadReport() {
        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClass.getName())
                .child("attendance").child(Common.selectedReport)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        studentAttendanceModels = new ArrayList<>();
                        if (snapshot.exists()){
                            studentAttendanceModels.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                StudentAttendanceModel model = dataSnapshot.getValue(StudentAttendanceModel.class);
                                studentAttendanceModels.add(model);
                                adapter = new SpecificAttendanceAdapter(SpecificAttendanceReport.this,studentAttendanceModels);
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