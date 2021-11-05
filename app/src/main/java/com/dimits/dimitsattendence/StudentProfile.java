package com.dimits.dimitsattendence;

import android.content.Intent;
import android.os.Bundle;

import com.dimits.dimitsattendence.adapter.SpecificAttendanceAdapter;
import com.dimits.dimitsattendence.common.Common;
import com.dimits.dimitsattendence.model.StudentAttendanceModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StudentProfile extends AppCompatActivity {
    TextView StudentName,txt_id;

    Bundle intent;
    RecyclerView recyclerView;
    SpecificAttendanceAdapter specificAttendanceAdapter;
    List<StudentAttendanceModel> studentAttendanceModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

         intent = getIntent().getExtras();


        if(intent != null){
            StudentName = findViewById(R.id.student_name);
            StudentName.setText(intent.getString("name"));
            txt_id = findViewById(R.id.textView2);
            txt_id.setText(intent.getString("id"));
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
                                if (studentAttendanceModel.getId().equals(intent.getString("id"))) {
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