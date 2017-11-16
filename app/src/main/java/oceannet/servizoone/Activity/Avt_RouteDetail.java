package oceannet.servizoone.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import oceannet.servizoone.AppController;
import oceannet.servizoone.Constant.Sabitler;
import oceannet.servizoone.Model.RouteItem;
import oceannet.servizoone.R;

public class Avt_RouteDetail extends AppCompatActivity  implements  OnMapReadyCallback {


    TextView txt_distance_Time;
    GoogleMap map;
    private Polyline mMutablePolyline;
    PolylineOptions options = new PolylineOptions() ;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avt__route_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

              setTitle("Hello");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.geri);
        ab.setDisplayHomeAsUpEnabled(true);

        txt_distance_Time = (TextView) findViewById(R.id.txt_distanceTimeD)  ;
        get_routeDetail();

    }



    @Override
    public void onMapReady(final GoogleMap map) {
        map.setContentDescription("Google Map with polylines.");
        this.map = map;

    }


    public  void setMap(){
        int radius = 5;

        for (int i = 0; i < Routeitems.size(); i++) {

            RouteItem item  = Routeitems.get(i);

            LatLng latlong = new LatLng(item.getLatitude(),item.getLongitude())  ;
//            options.add(latlong) ;
//            builder.include(latlong);

            MarkerOptions markerOptions = new MarkerOptions().position(latlong)
                    .title(item.getName())
                    .snippet("")  ;

            map.addMarker(markerOptions);


        }

        for (int i = 0; i < latlongs.size(); i++) {
            //    RouteItem item  = Routeitems.get(i);

            // LatLng latlong = new LatLng(latlongs.get(i))  ;
            options.add(latlongs.get(i)) ;
            builder.include(latlongs.get(i));

//            MarkerOptions markerOptions = new MarkerOptions().position(latlongs.get(i))
//                    .title("")
//                    .snippet("")  ;
//
//            map.addMarker(markerOptions);



        }

//        for (int i = 0; i < latlongs.size(); i++) {
//            RouteItem item  = Routeitems.get(i);
//
//            LatLng latlong = new LatLng(item.getLatitude(),item.getLongitude())  ;
//            options.add(latlong) ;
//            builder.include(latlong);
//
//            MarkerOptions markerOptions = new MarkerOptions().position(latlongs.get(i))
//                    .title("")
//                    .snippet("")  ;
//
//            map.addMarker(markerOptions);
//
//
//
//        }




        map.addPolyline(options);

        int color = Color.HSVToColor(
                1, new float[]{1, 1, 1});
        mMutablePolyline = map.addPolyline(options
                .color(color)
                .width(8)
                .clickable(true));









//        mColorBar.setOnSeekBarChangeListener(this);
//        mAlphaBar.setOnSeekBarChangeListener(this);
//        mWidthBar.setOnSeekBarChangeListener(this);

        // Move the map so that it is centered on the mutable polyline.
//        map.moveCamera(CameraUpdateFactory.newLatLng(SYDNEY));
//
//        map.animateCamera(CameraUpdateFactory.zoomTo(15), 10000, null);


        //    map.moveCamera(cu);


        // Add a listener for polyline clicks that changes the clicked polyline's color.
        map.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                // Flip the values of the r, g and b components of the polyline's color.
                int strokeColor = polyline.getColor() ^ 0x00ffffff;
                polyline.setColor(strokeColor);
            }
        });


        LatLngBounds bounds = builder.build();


        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding) ;
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40));
        map.moveCamera(CameraUpdateFactory.zoomTo(12));


    }
    ArrayList<RouteItem> Routeitems =  new ArrayList<RouteItem>();

    ArrayList<LatLng> latlongs =  new ArrayList<LatLng>();
    public void get_routeDetail(){

//        progressDialog.setMessage("Yükleniyor. Lütfen Bekleyiniz");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();

        AndroidNetworking.get(Sabitler.GET_ROUTEDETAIL)
                .addHeaders("Authorization", AppController.getInstance().getToken())
                .addQueryParameter("id",""+AppController.getInstance().getRoute().getID())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // pDialog.dismiss();
                        System.out.println("zassGelen Servis Bilgisi Json" + response.toString());
                       // progressDialog.dismiss();
                        try {
                            if (response.getJSONObject("status").getInt("response_code")==200){
                                System.out.println("GİRİŞ BAŞARILI");

                                JSONObject data  = response.getJSONObject("data");

                                 setTitle(data.getString("RouteName"));

                                txt_distance_Time.setText( "KM: "+data.getString("KM") + "km    Süre: " +data.getString("DK") + " dk." );

                                JSONArray jsonArray = data.getJSONArray("RouteItemList");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    RouteItem item = new RouteItem(object.getInt("ID"),object.getBoolean("isCompany"),object.getDouble("Latitude"),object.getDouble("Longitude"),object.getString("Name"),object.getInt("Order"));

                                    Routeitems.add(item);


                                }
                                JSONArray jsonArray2 = data.getJSONArray("PointList");
                                for (int i = 0; i < jsonArray2.length(); i++) {

                                    JSONObject object = jsonArray2.getJSONObject(i);

                               LatLng latlng = new LatLng(object.getDouble("latitude"),object.getDouble("longitude"));

                                    latlongs.add(latlng);


                                }
                                setMap();
                            }
                            else {
                                System.out.println("GİRİŞ BAŞARISIZ!!!");
                            }
                        } catch (JSONException e) {


                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        //    pDialog.dismiss();
                        System.out.println("Servis Bilgisi Hatası" + anError.getErrorBody());
                        //progressDialog.dismiss();
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_servis_bilgilerim, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
