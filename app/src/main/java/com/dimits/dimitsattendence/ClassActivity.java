package com.dimits.dimitsattendence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dimits.dimitsattendence.common.Common;
import com.dimits.dimitsattendence.model.ClassModel;

public class ClassActivity extends AppCompatActivity {

    ClassModel classModel;
    TextView class_name;
    Button students_btn;
    Button attendance_btn;
    Button add_students_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_class_activity);
        class_name = (TextView) findViewById(R.id.class_name);
        students_btn = (Button) findViewById(R.id.students_btn);
        classModel = Common.currentClass;
        class_name.setText(classModel.getName());
        students_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassActivity.this,StudentsActivity.class);
                startActivity(intent);
            }
        });
    }
}