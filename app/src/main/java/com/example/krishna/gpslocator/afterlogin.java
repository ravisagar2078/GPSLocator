package com.example.krishna.gpslocator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ravi Sagar on 2/13/2017.
 */
public class afterlogin extends AppCompatActivity {
    private String jsonResult;
    private String url = "https://freakish-assault.000webhostapp.com/rough.php";
    private Spinner listView;
    private  String username;
 //   Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.afterlogin);
        Intent intent=getIntent();
        username=intent.getExtras().getString("uname");
        listView =(Spinner) findViewById(R.id.spinner1);



        accessWebService();
    //    Spinner spinner = (Spinner)findViewById(R.id.spinner1);
//        String text = spinner.getSelectedItem().toString();
  //      System.out.print(text);


    }
    public void onselect(View view) {

        String q=listView.getSelectedItem().toString();
        String[]  p=q.split("=");
        p=p[1].split(" ");

        String col=username+"/"+p[0];

         //Toast.makeText(getApplicationContext(),col,Toast.LENGTH_LONG).show();
        //Intent intent1=new Intent(getApplicationContext(), MainActivity.class);
 //       intent.putExtra("oo", col);
  //      intent1.putExtra("uname", username);

       // getApplicationContext().startActivity(intent1);
        //startActivity(new Intent(this,MainActivity.class) );
        Intent intent = new Intent(afterlogin.this, MainActivity.class);
        intent.putExtra("oo", col);

        startActivity(intent);


    //    String username = UsernameEt.getText().toString();
      //  String password = PasswordEt.getText().toString();
       // String type = "login";

  //      BackgroundWorker backgroundWorker = new BackgroundWorker(this);
//        backgroundWorker.execute(type,username,password);
//        progress = new ProgressDialog(login.this);

//

    }
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            }

            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);

                }
            }

            catch (IOException e) {
                // e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("BEFORE POST EXECUTE");
            ListDrawer();
        }
    }// end async task
    public void accessWebService()
    {
        JsonReadTask task = new JsonReadTask();
        // passes values for the urls string array
        task.execute(new String[] { url });
    }

    // build hash set for list view
    public void ListDrawer()
    {

        List<Map<String, String>> trainList = new ArrayList<>();

        try
        {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("Trains");

            for (int i = 0; i < jsonMainNode.length(); i++)
            {
                JSONObject row = jsonMainNode.getJSONObject(i);
                String id = row.optString("trainID");
                String id_name = id+"       "+row.optString("train_name");
                //String name = row.optString("train_name");
                trainList.add(createTrain("trains", id_name));
                //trainList.add(createTrain(id, name));
            }
        }
        catch (JSONException e)
        {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, trainList,
                android.R.layout.simple_list_item_1,
                new String[] {"trains"}, new int[] { android.R.id.text1 });
        simpleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listView.setAdapter(simpleAdapter);
    }

    private HashMap<String, String> createTrain(String list, String id_name)
    {
        HashMap<String, String> train = new HashMap<String, String>();
        train.put(list, id_name);
        return train;
    }




}
