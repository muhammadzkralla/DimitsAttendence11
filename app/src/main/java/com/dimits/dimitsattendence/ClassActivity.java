package com.dimits.dimitsattendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
        Window window = ClassActivity.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.medium_blue));
        class_name = (TextView) findViewById(R.id.class_name);
        students_btn = (Button) findViewById(R.id.students_btn);
        attendance_btn = (Button)findViewById(R.id.attendance_btn);
        add_students_btn = (Button)findViewById(R.id.add_students_btn);
        reports_btn = (Button)findViewById(R.id.reports_btn);
        classModel = Common.currentClass;

        class_name.setText(classModel.getName());

        reports_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reportsIntent = new Intent(ClassActivity.this,Reports.class);
                startActivity(reportsIntent);
            }
        });

        attendance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent attendanceIntent = new Intent(ClassActivity.this,AttendanceActivity.class);
                startActivity(attendanceIntent);
            }
        });

        add_students_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddStudentDialog();
            }
        });

        students_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassActivity.this,StudentsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Student");
        builder.setMessage("Please, fill in the information");
        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_add_student, null);
        EditText etName =  itemView.findViewById(R.id.edt_student_name);
        EditText etId =  itemView.findViewById(R.id.edt_student_id);
        builder.setNegativeButton("CANCEL", (dialogInterface, i) ->
                dialogInterface.dismiss());
        builder.setPositiveButton("ADD", (dialogInterface, i) -> {
            StudentModel studentModel = new StudentModel();
            if (TextUtils.isEmpty(etName.getText().toString())){
                Toast.makeText(ClassActivity.this, "Please Enter Student Name", Toast.LENGTH_SHORT).show();
                return;
            }
            studentModel.setName(etName.getText().toString());
            studentModel.setId(etId.getText().toString());
            FirebaseDatabase.getInstance().getReference("Classes").child(classModel.getName())
                    .child("students").child(etId.getText().toString())
                    .setValue(studentModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    dialogInterface.dismiss();
                    Toast.makeText(ClassActivity.this, "Student Created Successfully!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}