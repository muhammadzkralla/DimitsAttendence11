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

import com.dimits.dimitsattendence.adapter.ReportAdapter;
import com.dimits.dimitsattendence.common.Common;
import com.dimits.dimitsattendence.model.StudentAttendanceModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Reports extends AppCompatActivity {
    // initialize the variables
    RecyclerView recyclerView;
    ReportAdapter adapter;
    List<String> Dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        //getSupportActionBar() to change the color and text color of the Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.medium_blue)));
        actionBar.setTitle(Html.fromHtml("<font color='#FF000000'>Dimits Attendance </font>"));
        //change the color of the status bar
        Window window = Reports.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.medium_blue));

        // declare the variables and set the layout manager
        recyclerView = (RecyclerView) findViewById(R.id.recycler_reports);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        // download the reports will show in the activity
        downloadReports();
    }

    private void downloadReports() {
        // prepare the List of Dates to be filled
        Dates = new ArrayList<>();

        // refer to the date in firebase
        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClass.getName())
                .child("attendance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Avoid the null pointer exception
                if (snapshot.exists()){
                    // clear the list to avoid duplication
                    Dates.clear();
                    // for each Date under the reference, do the following
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        // store them one by one and pass the data to the adapter
                        String DateReference = dataSnapshot.getKey();
                        Dates.add(DateReference);
                        adapter = new ReportAdapter(Reports.this,Dates);
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