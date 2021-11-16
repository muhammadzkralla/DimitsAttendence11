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
    List<ClassModel> classModelList;
    //Initialize the variables
    RecyclerView recyclerView;
    ClassesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //getSupportActionBar() to change the color and text color of the Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.medium_blue)));
        actionBar.setTitle(Html.fromHtml("<font color='#FF000000'>Dimits Attendance </font>"));
        //change the color of the status bar
        Window window = HomeActivity.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.medium_blue));

        //Declare the RecyclerView Variable
        recyclerView = (RecyclerView) findViewById(R.id.recycler_classes);
        recyclerView.setHasFixedSize(true);
        //The GridLayoutManager object allow us to put the items in a grid and arrange them with two items in each row
        GridLayoutManager layoutManager = new GridLayoutManager(HomeActivity.this,2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //always check for the adapter to avoid nullPointerExceptions()
                if(adapter != null) {
                    //getting the size of each row
                    switch (adapter.getItemViewType(position)) {
                        case 0: return 1;
                        case 1: return 2;
                        default: return -1;
                    }
                }
                return -1;
            }
        });
        //setting the layout manager to the recycler view
        recyclerView.setLayoutManager(layoutManager);
        //calling the downloadClasses() method to get the Classes on the Firebase and send them to the adapter
        downloadClasses();
    }

    private void downloadClasses() {
        //prepare the List of classModel to be filled
        classModelList = new ArrayList<>();
        //refer to the location of the data required to be downloaded
        FirebaseDatabase.getInstance().getReference("Classes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Avoiding nullPointerExceptions
                if (snapshot.exists()) {
                    //clear the list to download the data and avoid replication
                    classModelList.clear();

                    // For each Data snapshot under the reference "Classes" in the Database
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        // Download them one by one
                        ClassModel classModel = dataSnapshot.getValue(ClassModel.class);
                        // add them to the list one by one
                        classModelList.add(classModel);
                        // pass the context and the List of classModel to the adapter
                        adapter = new ClassesAdapter(HomeActivity.this,classModelList);
                        // finally assign the adapter to the recycler view
                        recyclerView.setAdapter(adapter);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handling exceptions
                Toast.makeText(HomeActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}