package com.dimits.dimitsattendence.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dimits.dimitsattendence.R;
import com.dimits.dimitsattendence.SpecificAttendanceReport;
import com.dimits.dimitsattendence.common.Common;
import com.dimits.dimitsattendence.model.StudentAttendanceModel;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {
    //initialize the variables
    Context context;
    List<String> dates;

    // A public constructor to get the data from the Reports Class
    public ReportAdapter(Context context, List<String> dates) {
        this.context = context;
        this.dates = dates;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating the layout
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.report_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // here we inflate the data we got from the activity into items also one by one
        String date_txt = dates.get(position);
        holder.report_date.setText(date_txt);
        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open the selected report and store it to reuse later
                Common.selectedReport = date_txt;
                Intent intent = new Intent(context, SpecificAttendanceReport.class);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //initializing views of the layout
        TextView report_date;
        Button btn_view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            report_date = itemView.findViewById(R.id.report_date);
            btn_view = itemView.findViewById(R.id.btn_view);
        }
    }
}
