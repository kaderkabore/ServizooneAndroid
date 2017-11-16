package oceannet.servizoone;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.maps.model.LatLng;



import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.UUID;

import oceannet.servizoone.Adapter.Adapter_ComplaintDetail;
import oceannet.servizoone.Constant.Sabitler;
import oceannet.servizoone.Model.AnswerList;
import oceannet.servizoone.Model.Complaint;
import oceannet.servizoone.Model.Route;

import static oceannet.servizoone.R.id.rcl_talep;
import static oceannet.servizoone.R.id.txt_message;
import static oceannet.servizoone.R.id.txt_time;

/**
 * Created by oceannet on 25/05/17.
 */

public class AppController extends Application implements BeaconConsumer, BootstrapNotifier, RangeNotifier {
    public static final String TAG = AppController.class.getSimpleName();

    private BackgroundPowerSaver backgroundPowerSaver;

    private BeaconManager beaconManager;
    private RegionBootstrap regionBootstrap;
    private static NotificationManager mNotificationManager;

    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private Handler scanHandler = new Handler();
    private int scan_interval_ms = 5000;
    private boolean isScanning = false;


    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;



    String route_id = "";

    Route route ;
       Complaint complaint;


    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ///beaconinside\

       // MultiDex.install(this);


        ///realm
        mInstance = this;

        AndroidNetworking.initialize(getApplicationContext());


         //////    // init BLE
        // init BLE
        btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

        scanHandler.post(scanRunnable);


        backgroundPowerSaver = new BackgroundPowerSaver(this);
        BeaconManager.setDebug(true);

        beaconManager = BeaconManager.getInstanceForApplication(this);

        beaconManager.bind(this);
        Beacon.setHardwareEqualityEnforced(true);


        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("\tm:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));


        beaconManager.setBackgroundScanPeriod(10l);








    }
    private static AppController mInstance;
    public static synchronized AppController getInstance() {


        return mInstance;
    }


    public void toast(String message){

        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();

    }


    public  void requestPermissionBluethoothAdmin(Activity activity){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.ACCESS_FINE_LOCATION},
                        5);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
//        ActivityCompat.requestPermissions(activity,
//                new String[]{Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.ACCESS_FINE_LOCATION},
//                5);

        String permission =  Manifest.permission.ACCESS_FINE_LOCATION;
        if(ActivityCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED) {
            // Proceed with your code execution

            System.out.println("local permission");
        } else {
            // Uhhh I guess we have to ask for permission

            System.out.println("local not permission permission");
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.ACCESS_FINE_LOCATION},
                    5);


        }



    }
  public  String getToken(){
      SharedPreferences sharedPreferences;
      sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


      return  sharedPreferences.getString("token_type","b")+" "+sharedPreferences.getString("access_token","a") ;
  }



         /////

    // ------------------------------------------------------------------------
    // public usage
    // ------------------------------------------------------------------------

    private Runnable scanRunnable = new Runnable()
    {
        @Override
        public void run() {

            if (isScanning)
            {
                if (btAdapter != null)
                {
                    btAdapter.stopLeScan(leScanCallback);
                }
            }
            else
            {
                if (btAdapter != null)
                {
                    btAdapter.startLeScan(leScanCallback);
                }
            }

            isScanning = !isScanning;

            scanHandler.postDelayed(this, scan_interval_ms);
        }
    };

    // ------------------------------------------------------------------------
    // Inner classes
    // ------------------------------------------------------------------------

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback()
    {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord)
        {
            int startByte = 2;
            boolean patternFound = false;
            while (startByte <= 5)
            {
                if (    ((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                        ((int) scanRecord[startByte + 3] & 0xff) == 0x15)
                { //Identifies correct data length
                    patternFound = true;
                    break;
                }
                startByte++;
            }

            if (patternFound)
            {
                //Convert to hex String
                byte[] uuidBytes = new byte[16];
                System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                String hexString = bytesToHex(uuidBytes);

                //UUID detection
                String uuid =  hexString.substring(0,8) + "-" +
                        hexString.substring(8,12) + "-" +
                        hexString.substring(12,16) + "-" +
                        hexString.substring(16,20) + "-" +
                        hexString.substring(20,32);

                // major
                final int major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);

                // minor
                final int minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);

                Log.i("zasss:" , "" +uuid );
                Log.i("zassss","UUID: " +uuid + "\\nmajor: " +major +"\\nminor" +minor);

                setBeacon(uuid,major+"",minor+"");


                if (uuid.equals("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"))
                    //Log.i(LOG_TAG,"UUID: " +uuid + "\\nmajor: " +major +"\\nminor" +minor);
                    Log.i("ugur:" , "" +uuid );


            }

        }
    };

    /**
     * bytesToHex method
     */

    //E2C56DB5-DFFB-48D2-B060-D0F5A71096E0
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

        public  void setBeacon(String uuid,String major,String minor){

            Region region3 = new Region("com.oceannet."+""+minor+major, Identifier.fromUuid(UUID.fromString(uuid)),  Identifier.fromInt(Integer.valueOf(major))
                    ,  Identifier.fromInt(Integer.valueOf(minor)));






            try {
                beaconManager.startMonitoringBeaconsInRegion(region3);
                regionBootstrap = new RegionBootstrap(this, region3);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

    @Override
    public void onBeaconServiceConnect() {

    }

    @Override
    public void didEnterRegion(Region region) {

        Log.d("kaaaaa", "region of a beacon." + region.getUniqueId());
        Log.d("kaaaa", "region of a beacon." + region.getId1());
        Log.d("zass", "region of a beacon." + region.getId3());

        Log.d("zassss", "region of a beacon." + region.getId2()) ;
        logStaff(region.getId1()+"",region.getId2()+"",region.getId3()+"",1);


    }

    @Override
    public void didExitRegion(Region region) {

        Log.d("kaaaaa", "Exit region of a beacon." + region.getUniqueId());
        Log.d("kaaaa", "Exit region of a beacon." + region.getId1());
        Log.d("zass", "Exit region of a beacon." + region.getId3());

        Log.d("zassss", "Exit region of a beacon." + region.getId2()) ;

        logStaff(region.getId1()+"",region.getId2()+"",region.getId3()+"",0);

    }








    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

    }

    private GoogleApiClient mGoogleApiClient;

    Context mContext;

    boolean checkGPS = false;


    boolean checkNetwork = false;

    boolean canGetLocation = false;

    Location loc = null;
    double latitude;
    double longitude;


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;



    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;


    private LatLng latLong;

    private final int INTERVAL = 5000;
    // private final int INTERVAL = 3000;
    private final int FAST_INTERVAL = 1000;


    private Location getLocation() {

        try {
            locationManager = (LocationManager) this
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS && !checkNetwork) {
                Toast.makeText(this, "No Service Provider Available", Toast.LENGTH_SHORT).show();

                Toast.makeText(this, " GPS kapalı", Toast.LENGTH_SHORT).show();


            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (checkNetwork) {
                    //   Toast.makeText(this, "Network", Toast.LENGTH_SHORT).show();

                    try {
//                        locationManager.requestLocationUpdates(
//                                LocationManager.NETWORK_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            loc = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        }

                        if (loc != null) {
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    } catch (SecurityException e) {

                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (checkGPS) {
                // Toast.makeText(this, "GPS", Toast.LENGTH_SHORT).show();
                if (loc == null) {
                    try {
//                        locationManager.requestLocationUpdates(
//                                LocationManager.GPS_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            loc = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (loc != null) {
                                latitude = loc.getLatitude();
                                longitude = loc.getLongitude();
                            }
                        }
                    } catch (SecurityException e) {

                    }
                }
            }     else {
                Toast.makeText(this, " GPS kapalı", Toast.LENGTH_SHORT).show();


//                showSettingsAlert();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        if(loc != null){
//            sendLocationTrack(38.713800,35.532727);

           // sendLocationTrack(loc.getLatitude(),loc.getLongitude());

        //    AppController.getInstance().setLocation(loc);
        }

        return loc;
    }





    public void logStaff(String uuid,String major,String minor,int status){

//        progressDialog.setMessage("Yükleniyor. Lütfen Bekleyiniz");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();

                // aaaa0850-3333-466c-ccc0-031618246195
        //4660
        //8
        Location location = getLocation();
        AndroidNetworking.post(Sabitler.URL_STAFFBYLOG)
                .addHeaders("Authorization", AppController.getInstance().getToken())
                .addQueryParameter("beaconuuid",""+uuid)
                .addQueryParameter("major",""+major)
                .addQueryParameter("minor",""+minor)
                .addQueryParameter("status",""+status)
                .addQueryParameter("latitude",""+location.getLatitude()+"")
                .addQueryParameter("longitude",""+location.getLongitude()+"")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // pDialog.dismiss();
                        System.out.println("zassGelen loggggggg complait detailServis Bilgisi Json" + response.toString());
                        //progressDialog.dismiss();
                        try {
                            if (response.getJSONObject("status").getInt("response_code")==200){

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
                        System.out.println("zassGelen loggggggg Servis Bilgisi Hatası" + anError.getErrorBody());
                        //  progressDialog.dismiss();
                    }
                });
    }
}
