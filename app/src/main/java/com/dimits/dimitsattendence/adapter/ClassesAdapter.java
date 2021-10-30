package com.dimits.dimitsattendence.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dimits.dimitsattendence.ClassActivity;
import com.dimits.dimitsattendence.R;
import com.dimits.dimitsattendence.common.Common;
import com.dimits.dimitsattendence.model.ClassModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.MyViewHolder> {
    Context context;
    private List<ClassModel> classModels = new ArrayList<>();

    public ClassesAdapter(Context context, List<ClassModel> classModels) {
        this.context = context;
        this.classModels = classModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.class_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClassModel classModel = classModels.get(position);
        holder.txt_class_name.setText(classModel.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.currentClass = classModel;
                Intent intent = new Intent(context, ClassActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return classModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_class_name)
        TextView txt_class_name;
        @BindView(R.id.img_class)
        ImageView img_class;

        Unbinder unbinder;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }


    }
}
