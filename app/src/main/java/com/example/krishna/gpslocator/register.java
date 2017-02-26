package com.example.krishna.gpslocator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Krishna on 4/12/2016.
 */
public class register extends AppCompatActivity {
    ProgressDialog progress;
    EditText email,username,password, phnumber ,conpass;
    String str_email,str_username,str_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityregister);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email= (EditText)findViewById(R.id.et_email);
        username= (EditText)findViewById(R.id.et_username);
        password= (EditText)findViewById(R.id.et_password);
        phnumber= (EditText)findViewById(R.id.et_number);
        conpass=(EditText)findViewById(R.id.confirmPassword);
    }
    public void onReg(View view) {
        //int chk=checkValidation();
        //if(chk==1) {
            String str_email = email.getText().toString();
            String str_username = username.getText().toString();
            String str_password = password.getText().toString();
            String str_phnumber = phnumber.getText().toString();
            String type = "register";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, str_email, str_username, str_password, str_phnumber);
//        startActivity(new Intent(this,MainActivity.class) );
        //}
        /*else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setTitle("Password Not Matched");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            builder.show();


        }*/

        }
    public void onlogin(View view){
        startActivity(new Intent(this,login.class) );
    }


    public int checkValidation() {

        // Get all edittext texts
        String getPassword = password.getText().toString();
        String getConfirmPassword = conpass.getText().toString();


            // Check if both password should be equal
         if (!getConfirmPassword.equals(getPassword))
        return  0;
        //"Both password doesn't match.");
        return  1;
    }


}




