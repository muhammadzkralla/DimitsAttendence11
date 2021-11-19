package com.dimits.dimitsattendence.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dimits.dimitsattendence.ClassActivity;
import com.dimits.dimitsattendence.HomeActivity;
import com.dimits.dimitsattendence.MainActivity;
import com.dimits.dimitsattendence.R;
import com.dimits.dimitsattendence.common.Common;
import com.dimits.dimitsattendence.model.ClassModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.MyViewHolder> {
    //initialize the variables
    Context context;
    private List<ClassModel> classModels = new ArrayList<>();
    // A public constructor to get the data from the HomeActivity Class
    public ClassesAdapter(Context context, List<ClassModel> classModels) {
        this.context = context;
        this.classModels = classModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating the layout
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.class_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // here we inflate the data we got from the activity into items also one by one
        ClassModel classModel = classModels.get(position);
        holder.txt_class_name.setText(classModel.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on clicking on a specific Class store its object in Common Class to reuse later
                Common.currentClass = classModel;
                // open the activity of this clicked class
                Intent intent = new Intent(context, ClassActivity.class);
                context.startActivity(intent);
            }
        });
        // set on long click listener to remove the value of a class
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("DELETE " + classModel.getName() + " ?");
                alertDialog.setMessage("Choose what to do please :");
                alertDialog.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Delete the class by its reference
                        FirebaseDatabase.getInstance().getReference("Classes")
                                .child(classModel.getName()).removeValue();
                        // reload the activity
                        context.startActivity(new Intent(context,HomeActivity.class));
                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //show the connection Dialog
                alertDialog.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return classModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //initializing views of the layout
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

    @Override
    public int getItemViewType(int position) {
        // here we check the size of the items to arrange them correctly in the Activity
        if (classModels.size() == 1){
            return 0;
        }else{
            if (classModels.size() % 2 == 0){
                return 0;
            }
            else {
                if(position > 1 && position == classModels.size()-1)
                    return 1;
                else
                    return 0;
            }
        }
    }
}
