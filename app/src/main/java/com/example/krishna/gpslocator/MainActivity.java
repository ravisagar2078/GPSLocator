
package com.example.krishna.gpslocator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;


public class MainActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener,
        GooglePlayServicesClient.OnConnectionFailedListener
{



    private GoogleMap myMap;            // map reference
    private LocationClient myLocationClient;
    double plat=0.0,plng=0.0;
    private  String username,trainID;
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
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        String un=intent.getExtras().getString("oo");

        //

        String[]  p=un.split("/");
        username=p[0];
        trainID=p[1];
        Toast.makeText(getApplicationContext(),username+"--"+trainID,Toast.LENGTH_LONG).show();


//       Intent intent=getIntent();
  //      username=intent.getExtras().getString("uname");
    //    System.out.print(username);

  //      Bundle extras = getIntent().getExtras();
     //   username= extras.getString("uname");
//        trainID=extras.getString("tid");

        //      Intent intent1=getIntent();
//        trainID=intent1.getExtras().getString("tid");
    //    System.out.print(trainID);

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
        String str=null;
        String cityName = null;
        String countryName =null;
        double clat, clng;
        clat = location.getLatitude();
        clng = location.getLongitude();
        if(plng == 0.0 && plat == 0.0) {
            plat = clat;
            plng = clng;
            String str_clat = Double.toString(clat);
            String str_clng = Double.toString(clng);
           // String str = int+.toString(trainID);
            BackgroundWorker backgroundWorker =new BackgroundWorker(getApplicationContext());
            backgroundWorker.execute("cods",str_clat,str_clng,username,trainID);
            //backgroundWorker.execute("cods",str_clat,str_clng,username);

            serMarker(clat,clng);
            gotoMyLocation(clat,clng);
        }
        else
        {
            drawLine(clat,clng);
        }
        //--------------- getting address---------------
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(clat, clng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses != null) {
            String streetName = addresses.get(0).getAddressLine(0);
            cityName = addresses.get(0).getLocality();
            countryName = addresses.get(0).getCountryName();
            //---------------------------------------------------------
            //str = clat + "\n" + clng + "\n" + cityName + "-" + countryName;
            str = clat + ", " + clng;
            System.out.println("\nlocality: "+cityName);
        }
        else
            str = clat + "\n" + clng;
        //Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();
        TextView cordinates = (TextView) findViewById(R.id.coords);
        TextView cityname = (TextView) findViewById(R.id.city);
        cordinates.setText(str);
        cityname.setText(cityName+", "+countryName);

//        String total2 = Double.toString(clat);
//        String total21 = Double.toString(clng);
//            String type ="cods";
        //System.out.println(total2+"\n"+total21);
//        BackgroundWorker backgroundWorker =new BackgroundWorker(getApplicationContext());
//        backgroundWorker.execute(type,total2,total21);

    }
    public void drawLine(double lat, double lng)
    {
        LatLng prev = new LatLng(plat, plng);
        LatLng curr = new LatLng(lat, lng);
        String type ="cods";
        BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());
        float dist = distCalculator(prev,curr);
        if(dist > 2.0 && dist < 17.0) {
            Polyline line = myMap.addPolyline(new PolylineOptions().add(prev, curr)
                    .width(10).color(Color.GREEN));
            System.out.println("DISTANCE : "+dist);
            Toast.makeText(getApplicationContext(),"DISTANCE : "+dist,Toast.LENGTH_LONG).show();
            serMarker(lat,lng);
            plat = lat;
            plng = lng;
            String str_lat = Double.toString(lat);
            String str_lng = Double.toString(lng);
            backgroundWorker.execute(type,str_lat,str_lng,username,trainID);
        }
    }
    public void serMarker(double lat, double lng)
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
    }
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

}
