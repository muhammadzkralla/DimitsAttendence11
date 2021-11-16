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
    // initialize the variables
    RecyclerView recyclerView;
    StudentsAdapter adapter;
    List<StudentModel> studentModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        ActionBar actionBar = getSupportActionBar();
        //getSupportActionBar() to change the color and text color of the Action bar
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.medium_blue)));
        actionBar.setTitle(Html.fromHtml("<font color='#FF000000'>Dimits Attendance </font>"));
        //change the color of the status bar
        Window window = StudentsActivity.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.medium_blue));

        //Declare the RecyclerView Variable give it a layout manager
        recyclerView = (RecyclerView) findViewById(R.id.recycler_students);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // download the students in this class and pass them to the adapter
        downloadStudents();
    }

    private void downloadStudents() {
        //prepare the List of studentModel to be filled
        studentModelList = new ArrayList<>();
        //refer to the location of the data required to be downloaded
        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClass.getName()).child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Avoiding nullPointerExceptions
                if (snapshot.exists()){
                    //clear the list to download the data and avoid replication
                    studentModelList.clear();

                    // For each Data snapshot under the reference "students" in the Database
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        // Download them one by one
                        StudentModel studentModel = dataSnapshot.getValue(StudentModel.class);

                        // add them to the list and pass the context,data to the adapter
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