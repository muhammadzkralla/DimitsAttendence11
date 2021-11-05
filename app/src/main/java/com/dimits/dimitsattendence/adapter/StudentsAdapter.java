package com.dimits.dimitsattendence.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dimits.dimitsattendence.R;
import com.dimits.dimitsattendence.StudentProfile;
import com.dimits.dimitsattendence.model.StudentModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.MyViewHolder> {
    Context context;
    List<StudentModel> studentModels = new ArrayList<>();

    public StudentsAdapter(Context context, List<StudentModel> studentModels) {
        this.context = context;
        this.studentModels = studentModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.student_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StudentModel studentModel = studentModels.get(position);
        holder.student_name.setText(studentModel.getName());
        holder.txt_id.setText(studentModel.getId());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("id", studentModel.getId());
                editor.apply();

                Intent intent = new Intent(context.getApplicationContext(), StudentProfile.class);
                intent.putExtra("id",studentModel.getId());
                intent.putExtra("name",studentModel.getName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.student_name)
        TextView student_name;
        @BindView(R.id.txt_id)
        TextView txt_id;
        @BindView(R.id.student_img)
        ImageView student_img;

        Unbinder unbinder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
