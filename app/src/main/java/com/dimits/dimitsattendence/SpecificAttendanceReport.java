package com.dimits.dimitsattendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
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
        //getSupportActionBar() to change the color and text color of the Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.medium_blue)));
        actionBar.setTitle(Html.fromHtml("<font color='#FF000000'>Dimits Attendance </font>"));
        //change the color of the status bar
        Window window = SpecificAttendanceReport.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.medium_blue));

        // declare the variables and set the layout manager
        recyclerView = (RecyclerView) findViewById(R.id.recycler_specific);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        // download the report data will show in the activity
        downloadReport();
    }

    private void downloadReport() {
        // get the reference of the selected report
        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClass.getName())
                .child("attendance").child(Common.selectedReport)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // prepare the list of attendances
                        studentAttendanceModels = new ArrayList<>();

                        // avoid nulls
                        if (snapshot.exists()){

                            // clear the list to avoid duplication
                            studentAttendanceModels.clear();

                            // for each item in the report :
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                // download them one by one and pass the data to the adapter
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