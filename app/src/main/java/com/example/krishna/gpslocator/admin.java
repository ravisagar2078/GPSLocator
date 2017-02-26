package com.example.krishna.gpslocator;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ravi Sagar on 12/26/2016.
 */
public class admin extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener,
        GooglePlayServicesClient.OnConnectionFailedListener
{



    private GoogleMap myMap;            // map reference
    private LocationClient myLocationClient;
    double plat=0.0,plng=0.0;
    private  String username;
    Marker myMarker=null;
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(2000)         // 2 seconds
            .setFastestInterval(16)    // 16ms = 60fp1s
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



    /**
     *      Activity's lifecycle event.
     *      onCreate will be Called when the activity is starting.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Intent intent=getIntent();
        username=intent.getExtras().getString("uname");
        System.out.print(username);
        //Toast.makeText(this,username,Toast.LENGTH_LONG).show();
        getMapReference();

    }

    /**
     *     Activity's lifecycle event.
     *     onResume will be called when the Activity receives focus and is visible
     */
    @Override
    protected  void onResume()
    {
        super.onResume();
        getMapReference();
        wakeUpLocationClient();
        myLocationClient.connect();
    }

    /**
     *  Get a map object reference if none exists and enable blue arrow icon on map
     */
    private void getMapReference()
    {
        if(myMap == null)
        {
            myMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
        }
        if(myMap != null)
        {
            myMap.setMyLocationEnabled(true);   // enable the current location button.
        }
    }

    /**
     *      Activity's lifecycle event.
     *      onPause will be called when activity is going into the background,
     */
    @Override
    public void onPause()
    {
        super.onPause();
        if(myLocationClient != null)
        {
            myLocationClient.disconnect();
        }

    }


    /**
     * @param lat - latitude of the location to move the camera to
     * @param lng - longitude of the location to move the camera to
     * Prepares a CameraUpdate object to be used with  callbacks
     */
    private void gotoMyLocation(double lat, double lng)
    {
        changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(15.5f)
                .bearing(0)
                .tilt(25)
                .build()
        ), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {

                // Your code here to do something after the Map is rendered
            }
            @Override
            public void onCancel() {

                // Your code here to do something after the Map rendering is cancelled
            }
        });
    }

    /**
     *  When we receive focus, we need to get back our LocationClient
     *  Creates a new LocationClient object if there is none
     */
    private void wakeUpLocationClient()
    {
        if(myLocationClient == null)
        {
            myLocationClient = new LocationClient(getApplicationContext(),this,this);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Logout:
                Intent intent=new Intent(getApplicationContext(), login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                getApplicationContext().startActivity(intent);

                Toast.makeText(this, "logout ", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }

        return true;
    }





    /**
     *  @param bundle
     *  onConnected called LocationClient is connected
     */

    @Override
    public void onConnected(Bundle bundle)
    {
        if(!runtime_permissions())
            myLocationClient.requestLocationUpdates(REQUEST,this);
        //System.out.println("Last location"+myLocationClient.getLastLocation());
    }


    /**
     *      LocationClient is disconnected
     */

    @Override
    public void onDisconnected() {
        // myLocationClient.disconnect();
        // myLocationClient = null;
    }

    /**
     *  @param location - Location object with all the information about location
     *  Callback from LocationClient every time our location is changed
     */
    @Override
    public void onLocationChanged(Location location)
    {
    }

 /*   public void serMarker(double lat, double lng)
    {
        if(myMarker != null)
            myMarker.remove();
        MarkerOptions options = new MarkerOptions()
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerpp))
                .position(new LatLng(lat,lng));
        LatLng ltln = new LatLng(lat,lng);
        myMap.animateCamera(CameraUpdateFactory.newLatLng(ltln));
        myMarker = myMap.addMarker(options);
    }
    public float distCalculator(LatLng prev, LatLng curr)
    {
        float[] results = new float[1];
        Location.distanceBetween(prev.latitude, prev.longitude,
                curr.latitude, curr.longitude, results);

        return results[0];
    }*/
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback)
    {
        myMap.moveCamera(update);
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.
                ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.
                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED )
        {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
            },100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == 100)
//        {
//            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
//                enable_buttons();
//            else
//                runtime_permissions();
//        }
    }

    private class LongOperation  extends AsyncTask<String, Void, String> {

     //   private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
       // private ProgressDialog Dialog = new ProgressDialog(AsyncronoustaskAndroidExample.this);
        //TextView uiUpdate = (TextView) findViewById(R.id.output);
        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //UI Element
          //  uiUpdate.setText("Output : ");
           // Dialog.setMessage("Downloading source..");
            //Dialog.show();
        }

        // Call after onPreExecute method
        HttpURLConnection httpURLConnection;

        protected String doInBackground(String... urls) {

           String cods_url = "http://freakish-assault.000webhostapp.com/zx.php";

            try {

                URL url=new URL(cods_url);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);



                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader
                        (inputStream,"iso-8859-1"));

                StringBuilder sb = new StringBuilder();
                 String line = "";
                 while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                     }

                String output;
                output = sb.toString();
                //System.out.println("Output: "+output);
//                System.out.println("Output from Server .... \n");
                br.close();
                inputStream.close();
                httpURLConnection.disconnect();

                try{
                     String n = "";
                     String a="";
                     String j="";
                     JSONArray jArray = new JSONArray(output);

                     for(int i=0; i<jArray.length();i++) {
                         JSONObject json = jArray.getJSONObject(i);
                         n = n + "userid : " + json.getString("userID")  + "\n";
                         a = a + "latitude : " + json.getString("latitude") + "\n";
                         j = j + "longitude : " + json.getString("longitude") + "\n";

                 System.out.println(n);
                         System.out.println(a);
                         System.out.println(j);


                     }

                }
                catch (Exception e) {

                     Log.e("log_tag", "Error Parsing Data "+e.toString());
                     }

                //return output;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
                   return  null;
        }

        protected void onPostExecute(String unused) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    //Toast.makeText(c, "check", Toast.LENGTH_SHORT).show();
                   // handler.postDelayed(this, 2000);
                }
            }, 60000);

        }

    }













    }


