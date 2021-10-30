package com.dimits.dimitsattendence.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.dimits.dimitsattendence.R;
import com.dimits.dimitsattendence.common.Common;
import com.dimits.dimitsattendence.model.StudentAttendanceModel;
import com.dimits.dimitsattendence.model.StudentModel;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> {
    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

    Context context;
    List<StudentModel> studentModels = new ArrayList<>();

    public AttendanceAdapter(Context context, List<StudentModel> studentModels) {
        this.context = context;
        this.studentModels = studentModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.attendance_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StudentModel studentModel = studentModels.get(position);
        holder.student_name_att.setText(studentModel.getName());
        holder.txt_id_att.setText(studentModel.getId());

        holder.wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudentAttendanceModel model = new StudentAttendanceModel();
                model.setName(studentModel.getName());
                model.setId(studentModel.getId());
                model.setAttendance("false");
                Toast.makeText(context, "false for : "+studentModel.getName(), Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClass.getName())
                        .child("attendance").child(date)
                        .child(studentModel.getId())
                        .setValue(model);
            }
        });

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudentAttendanceModel model = new StudentAttendanceModel();
                model.setName(studentModel.getName());
                model.setId(studentModel.getId());
                model.setAttendance("true");
                Toast.makeText(context, "true for : "+studentModel.getName(), Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClass.getName())
                        .child("attendance").child(date)
                        .child(studentModel.getId())
                        .setValue(model);
            }
        });


    }

    @Override
    public int getItemCount() {
        return studentModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView student_name_att;
        TextView txt_id_att;
        ImageView wrong;
        ImageView check;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_id_att = itemView.findViewById(R.id.txt_id_att);
            student_name_att = itemView.findViewById(R.id.student_name_att);
            wrong = itemView.findViewById(R.id.wrong_btn);
            check = itemView.findViewById(R.id.true_btn);
        }
    }
}
