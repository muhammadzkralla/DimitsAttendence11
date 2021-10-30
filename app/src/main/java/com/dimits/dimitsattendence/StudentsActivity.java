package com.dimits.dimitsattendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.dimits.dimitsattendence.adapter.StudentsAdapter;
import com.dimits.dimitsattendence.common.Common;
import com.dimits.dimitsattendence.model.StudentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    StudentsAdapter adapter;
    List<StudentModel> studentModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_students);
        recyclerView.setHasFixedSize(true);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        downloadStudents();
    }

    private void downloadStudents() {
        studentModelList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClass.getName()).child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    studentModelList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        StudentModel studentModel = dataSnapshot.getValue(StudentModel.class);
                        studentModelList.add(studentModel);
                        adapter = new StudentsAdapter(StudentsActivity.this,studentModelList);
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