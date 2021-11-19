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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> {
    // prepare the date format you want to use
    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

    // initialize the variables
    Context context;
    List<StudentModel> studentModels = new ArrayList<>();

    // A public constructor to get the data from the Attendance Class
    public AttendanceAdapter(Context context, List<StudentModel> studentModels) {
        this.context = context;
        this.studentModels = studentModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating the layout
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.attendance_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // here we inflate the data we got from the activity into items also one by one
        StudentModel studentModel = studentModels.get(position);
        holder.student_name_att.setText(studentModel.getName());
        holder.txt_id_att.setText(studentModel.getId());

        // checking if the attendance of a specific user is taken, if so show a sign next to it
        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClass.getName())
                .child("attendance").child(date)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // but always avoid nulls :)
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                StudentAttendanceModel studentAttendanceModel = dataSnapshot.getValue(StudentAttendanceModel.class);
                                if (studentAttendanceModel.getId().equals(studentModel.getId())){
                                    holder.attendance_done.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        // when the user press on the wrong button to make the student absent
        holder.wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // prepare a studentModel object to modify the old one on the firebase
                StudentAttendanceModel model = new StudentAttendanceModel();
                holder.attendance_done.setVisibility(View.VISIBLE);
                model.setName(studentModel.getName());
                model.setId(studentModel.getId());
                model.setAttendance("false");
                model.setDate(date);

                // modify the old student object
                FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClass.getName())
                        .child("attendance").child(date)
                        .child(studentModel.getId())
                        .setValue(model);
            }
        });

        // when the user press on the wrong button to make the student present
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // prepare a studentModel object to modify the old one on the firebase
                StudentAttendanceModel model = new StudentAttendanceModel();
                holder.attendance_done.setVisibility(View.VISIBLE);
                model.setName(studentModel.getName());
                model.setId(studentModel.getId());
                model.setAttendance("true");
                model.setDate(date);

                // modify the old student object
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
        //initializing views of the layout
        TextView student_name_att;
        TextView attendance_done;
        TextView txt_id_att;
        ImageView wrong;
        ImageView check;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_id_att = itemView.findViewById(R.id.txt_id_att);
            student_name_att = itemView.findViewById(R.id.student_name_att);
            wrong = itemView.findViewById(R.id.wrong_btn);
            check = itemView.findViewById(R.id.true_btn);
            attendance_done = itemView.findViewById(R.id.attendance_done);
        }
    }
}
