package com.dimits.dimitsattendence;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.Toast;

import com.dimits.dimitsattendence.adapter.ClassesAdapter;
import com.dimits.dimitsattendence.model.ClassModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<ClassModel> classModelList;
    ClassesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.medium_blue)));
        actionBar.setTitle(Html.fromHtml("<font color='#FF000000'>Dimits Attendance </font>"));
        Window window = HomeActivity.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.medium_blue));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_classes);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(HomeActivity.this,2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(adapter != null) {
                    switch (adapter.getItemViewType(position)) {
                        case 0: return 1;
                        case 1: return 2;
                        default: return -1;
                    }
                }
                return -1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        downloadClasses();
    }

    private void downloadClasses() {
        classModelList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Classes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    classModelList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ClassModel classModel = dataSnapshot.getValue(ClassModel.class);
                        classModelList.add(classModel);
                        adapter = new ClassesAdapter(HomeActivity.this,classModelList);
                        recyclerView.setAdapter(adapter);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}