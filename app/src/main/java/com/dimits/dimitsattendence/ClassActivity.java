package com.dimits.dimitsattendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dimits.dimitsattendence.common.Common;
import com.dimits.dimitsattendence.model.ClassModel;
import com.dimits.dimitsattendence.model.StudentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ClassActivity extends AppCompatActivity {
    // initialize the variables
    ClassModel classModel;
    TextView class_name;
    Button students_btn;
    Button attendance_btn;
    Button add_students_btn;
    Button reports_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_class_activity);
        //getSupportActionBar() to change the color and text color of the Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.medium_blue)));
        actionBar.setTitle(Html.fromHtml("<font color='#FF000000'>Dimits Attendance </font>"));
        //change the color of the status bar
        Window window = ClassActivity.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.medium_blue));

        // declare the variables as we won't use adapters here, so these are the views here
        class_name = (TextView) findViewById(R.id.class_name);
        students_btn = (Button) findViewById(R.id.students_btn);
        attendance_btn = (Button)findViewById(R.id.attendance_btn);
        add_students_btn = (Button)findViewById(R.id.add_students_btn);
        reports_btn = (Button)findViewById(R.id.reports_btn);

        // getting the class that we stored in the Common Class earlier ( you remember? :) )
        classModel = Common.currentClass;

        //we didn't make an adapter for this so setting the views values here
        class_name.setText(classModel.getName());


        // Different Buttons for different actions :

        reports_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open the reports Activity
                Intent reportsIntent = new Intent(ClassActivity.this,Reports.class);
                startActivity(reportsIntent);
            }
        });

        attendance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //opens the Attendance Activity to take today's attendance
                Intent attendanceIntent = new Intent(ClassActivity.this,AttendanceActivity.class);
                startActivity(attendanceIntent);
            }
        });

        add_students_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // adding a new student
                showAddStudentDialog();
            }
        });

        students_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // showing the students in this class
                Intent intent = new Intent(ClassActivity.this,StudentsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showAddStudentDialog() {
        //call the AlertDialog Builder Object to show an alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Student");
        builder.setMessage("Please, fill in the information");

        //inflating the layout of the dialog
        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_add_student, null);
        EditText etName =  itemView.findViewById(R.id.edt_student_name);
        EditText etId =  itemView.findViewById(R.id.edt_student_id);
        builder.setNegativeButton("CANCEL", (dialogInterface, i) ->
                // close the alert dialog
                dialogInterface.dismiss());
        builder.setPositiveButton("ADD", (dialogInterface, i) -> {
            // take the data and upload it by initializing a new Student object
            StudentModel studentModel = new StudentModel();

            // don't make them ignore you :)
            if (TextUtils.isEmpty(etName.getText().toString())){
                Toast.makeText(ClassActivity.this, "Please Enter Student Name", Toast.LENGTH_SHORT).show();
                return;
            }
            // taking the data in the Edit text and storing them in the Student object we declared to upload it
            studentModel.setName(etName.getText().toString());
            studentModel.setId(etId.getText().toString());

            // upload to the correct reference
            FirebaseDatabase.getInstance().getReference("Classes").child(classModel.getName())
                    .child("students").child(etId.getText().toString())
                    .setValue(studentModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // if completed successfully close the dialog and tell the user it's done
                    dialogInterface.dismiss();
                    Toast.makeText(ClassActivity.this, "Student Created Successfully!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        // well you can make a perfect Alert Dialog and forget to even write the code that show it
        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}