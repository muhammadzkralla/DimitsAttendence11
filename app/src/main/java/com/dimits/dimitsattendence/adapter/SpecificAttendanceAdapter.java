package com.dimits.dimitsattendence.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dimits.dimitsattendence.R;
import com.dimits.dimitsattendence.model.StudentAttendanceModel;

import java.util.List;

public class SpecificAttendanceAdapter extends RecyclerView.Adapter<SpecificAttendanceAdapter.MyViewHolder> {

    // initialize the variables
    Context context;
    List<StudentAttendanceModel> studentAttendanceModels;

    // A public constructor to get the data from the SpecificAttendanceReport Class
    public SpecificAttendanceAdapter(Context context, List<StudentAttendanceModel> studentAttendanceModels) {
        this.context = context;
        this.studentAttendanceModels = studentAttendanceModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating the layout
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.specific_attendance_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // here we inflate the data we got from the activity into items also one by one
        StudentAttendanceModel model = studentAttendanceModels.get(position);
        holder.student_name.setText(model.getName());
        holder.status.setText(model.getAttendance());
        holder.id.setText(model.getId());

        // handle the nullPointerExceptions like a bro
        if(model.getDate() == null){
            holder.date.setVisibility(View.INVISIBLE);
        }else {
            holder.date.setText(model.getDate());

        }


    }

    @Override
    public int getItemCount() {
        return studentAttendanceModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //initializing views of the layout
        TextView student_name,status,id,date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            student_name = itemView.findViewById(R.id.student_name);
            status = itemView.findViewById(R.id.status);
            id = itemView.findViewById(R.id.txt_id);
            date = itemView.findViewById(R.id.report_date);
        }
    }
}
