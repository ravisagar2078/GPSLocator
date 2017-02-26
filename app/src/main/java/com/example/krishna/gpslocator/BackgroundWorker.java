
package com.example.krishna.gpslocator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Krishna on 4/12/2016.
 */
public class BackgroundWorker extends AsyncTask<String,Void,String> {
    private String latitude ;
    private String longitude;
    private String userID ;
    private String trainID ;


    private String username;
    private String password;
    private String email;
    private String phnumber;

    Context context;
    HttpURLConnection httpURLConnection;

    BackgroundWorker(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String response=null;
        String type=params[0];
        //String cods_url = "http://traintrackagent.byethost16.com/insertCods.php";
       // String register_url="http://traintrackagent.byethost16.com/regis.php";
      //   String login_url="http://traintrackagent.byethost16.com/login.php";
        String cods_url = "http://freakish-assault.000webhostapp.com/insertCods.php";
          String register_url="http://freakish-assault.000webhostapp.com/regis.php";
         String login_url="http://freakish-assault.000webhostapp.com/login.php";
        //String cods_url = "http://traintrackagent.5gbfree.com/insertCods.php";
         //String register_url="http://traintrackagent.5gbfree.com/regis.php";
        //String login_url="http://traintrackagent.5gbfree.com/login.php";



    //    https://freakish-assault.000webhostapp.com/
        if(type.equals("cods"))
        {
            try
            {
                latitude = params[1];
                longitude = params[2];
                userID=params[3];
                trainID=params[4];
                URL url=new URL(cods_url);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                String post_data = URLEncoder.encode("latitude","UTF-8")+"="+URLEncoder.encode(latitude,"UTF-8")+"&"
                        +URLEncoder.encode("longitude","UTF-8")+"="+URLEncoder.encode(longitude,"UTF-8")+"&"
                        +URLEncoder.encode("userID","UTF-8")+"="+URLEncoder.encode(userID,"UTF-8")+"&"
                        +URLEncoder.encode("trainID","UTF-8")+"="+URLEncoder.encode(trainID,"UTF-8")
                        ;

                OutputStreamWriter ow = new OutputStreamWriter(httpURLConnection.getOutputStream());
                ow.write(post_data);
                ow.flush();
                ow.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader
                        (inputStream,"iso-8859-1"));

                String output;
                output = br.readLine();
                System.out.println("Output: "+output);
//                System.out.println("Output from Server .... \n");
                br.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return output;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("login"))
        {
            try
            {
                username=params[1];
                password=params[2];

                URL url=new URL(login_url);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                String post_data= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");


                OutputStreamWriter ow = new OutputStreamWriter(httpURLConnection.getOutputStream());
                ow.write(post_data);
                ow.flush();
                ow.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader
                        (inputStream,"iso-8859-1"));

                String output;
                output = br.readLine();
                System.out.println("Output: "+output);
//                System.out.println("Output from Server .... \n");
                br.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return output;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(type.equals("register"))
        {
            try
            {
                email=params[1];
                username=params[2];
                password=params[3];
                phnumber=params[4];

                URL url=new URL(register_url);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                String post_data= URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        +URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                        +URLEncoder.encode("phnumber","UTF-8")+"="+URLEncoder.encode(phnumber,"UTF-8");

                OutputStreamWriter ow = new OutputStreamWriter(httpURLConnection.getOutputStream());
                ow.write(post_data);
                ow.flush();
                ow.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader
                        (inputStream,"iso-8859-1"));

                String output;
                output = br.readLine();
                System.out.println("Output: "+output);
//                System.out.println("Output from Server .... \n");
                br.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return output;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
    @Override
    protected void onPostExecute(String result)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (result.equals("success"))
        {
         //   Intent intent=new Intent(context, MainActivity.class);
           // intent.putExtra("uname", username);
            //context.startActivity(intent);
            Intent intent=new Intent(context, afterlogin.class);
            intent.putExtra("uname", username);
            context.startActivity(intent);

        }
        else if(result.equals("codssuccess"))
        {}


        else if(result.equals("Welcome"))
        {

            Intent intent1=new Intent(context, admin.class);
            intent1.putExtra("uname", username);
            context.startActivity(intent1);
            Toast.makeText(context,"Welcome admin sahab.",Toast.LENGTH_LONG).show();
        }
        else
        {
            //Toast.makeText(context,result,Toast.LENGTH_LONG).show();
            builder.setTitle(result);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            //Toast.makeText(context,"ahl ojao.",Toast.LENGTH_LONG).show();

        //    builder.show();
        }

    }

    @Override
    protected void onProgressUpdate(Void... values)
    {
        super.onProgressUpdate(values);
    }
}