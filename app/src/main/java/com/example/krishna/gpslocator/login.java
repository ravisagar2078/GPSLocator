package com.example.krishna.gpslocator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Krishna on 4/12/2016.
 */
public class login extends AppCompatActivity {
    EditText UsernameEt, PasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylogin);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        UsernameEt = (EditText) findViewById(R.id.etUserName);
        PasswordEt = (EditText) findViewById(R.id.etPassword);

    }
    ProgressDialog progress;

    public void onlogin(View view) {

        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String type = "login";
        progress = new ProgressDialog(login.this);

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,username,password);
//        progress = new ProgressDialog(login.this);
        progress.setTitle("Authentication");
        progress.setMessage("Please wait...");
        progress.show();

        progress.dismiss();



//

    }
    public void OpenReg(View view)
    {
        startActivity(new Intent(this,register.class) );

    }

}
