package com.dimits.dimitsattendence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.dimits.dimitsattendence.common.Common;
import com.dimits.dimitsattendence.model.UserModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    //initialize the variables
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onStart() {
        super.onStart();
        //Check for connection
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //set a handler to delay the splash screen for 3 seconds.
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToHomeActivity();
                }
            },3000);
        }
        else{
            //user is offline, enquire him.
            Toast.makeText(this, "Your are offline, please connect to the Internet", Toast.LENGTH_SHORT).show();
            showConnectionDialog();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set the status bar color to white
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.white));
    }




    private void goToHomeActivity() {
        //starting the app after the splash screen
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();
    }



    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    private void showConnectionDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Internet Connection error");
        alertDialog.setMessage("choose what to do please :");
        alertDialog.setPositiveButton("Reconnect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Retry connection with calling the onStart() method
                onStart();
            }
        });
        alertDialog.setNegativeButton("Close the app", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //User can't get online, close the app by calling the finish() method
                finish();
            }
        });
        //show the connection Dialog
        alertDialog.show();
    }

    
}