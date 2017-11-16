package oceannet.servizoone.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import oceannet.servizoone.Constant.Sabitler;
import oceannet.servizoone.R;

import static oceannet.servizoone.R.id.map;

public class ServisimNeredeActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SharedPreferences sharedPreferences;


    String  Plate,Address;

    Double KM,DK,ServiceLocation_Lat,ServiceLocation_Lng,HouseLocation_Lat,HouseLocation_Lng,Companylocation_Lat,Companylocation_Lng;

    TextView txt_distanceTime,txt_plaka,txt_placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servisim_nerede);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        progressDialog=new ProgressDialog(this);



        txt_distanceTime = (TextView) findViewById(R.id.txt_distanceTime);
        txt_plaka = (TextView) findViewById(R.id.txt_plaka);
        txt_placeName = (TextView) findViewById(R.id.txt_placeName);

        txt_distanceTime.setText(" ");
        GetServiceLocation();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



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
    ProgressDialog progressDialog;
    public void GetServiceLocation(){

        progressDialog.setMessage("Yükleniyor. Lütfen Bekleyiniz");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AndroidNetworking.get(Sabitler.URL_GETSERVICELOCATION)
                .addHeaders("Authorization",sharedPreferences.getString("token_type","b")+" "+sharedPreferences.getString("access_token","a"))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // pDialog.dismiss();
                        System.out.println("zassGelen Servis Bilgisi Json" + response.toString());
                        progressDialog.dismiss();
                        try {
                            if (response.getJSONObject("status").getInt("response_code")==200){
                                System.out.println("GİRİŞ BAŞARILI");

                                JSONObject data  = response.getJSONObject("data");

                                KM = data.getDouble("KM");
                                DK = data.getDouble("DK");

                                Plate =  data.getString("Plate");
                                Address =  data.getString("Address");
                                JSONObject servicelocation  = response.getJSONObject("data").getJSONObject("ServiceLocation");
                                JSONObject HouseLocation  = response.getJSONObject("data").getJSONObject("HouseLocation");
                                JSONObject Companylocation  = response.getJSONObject("data").getJSONObject("Companylocation");


                                ServiceLocation_Lat = servicelocation.getDouble("latitude");
                                ServiceLocation_Lng = servicelocation.getDouble("longitude");

                               HouseLocation_Lat =  HouseLocation.getDouble("latitude");

                                HouseLocation_Lng =  HouseLocation.getDouble("longitude");

                                Companylocation_Lat =  Companylocation.getDouble("latitude");

                                Companylocation_Lat =  Companylocation.getDouble("longitude");


                                LatLng sydney = new LatLng(ServiceLocation_Lat, ServiceLocation_Lng);
                                mMap.addMarker(new MarkerOptions().position(sydney).title("" +Address));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                mMap.moveCamera(CameraUpdateFactory.zoomTo(16));

                                KM = data.getDouble("KM");KM = data.getDouble("KM");
                                KM = data.getDouble("KM");KM = data.getDouble("KM");KM = data.getDouble("KM");
                                txt_distanceTime.setText(KM + " km,  Varış Süresi " + DK + "dk.");
                                txt_plaka.setText("" +Plate);
                                txt_placeName.setText("" +Address);

                            }
                            else {
                                System.out.println("GİRİŞ BAŞARISIZ!!!");
                                txt_distanceTime.setText(response.getJSONObject("status").getString("response_desc"));
                            }
                        } catch (JSONException e) {


                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        //    pDialog.dismiss();
                        System.out.println("Servis Bilgisi Hatası" + anError.getErrorBody());
                        progressDialog.dismiss();
                    }
                });
       // progressDialog.dismiss();
    }

    public void click_back(View view) {

        this.finish();
    }
}
