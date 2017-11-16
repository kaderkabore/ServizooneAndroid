package oceannet.servizoone.Activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import oceannet.servizoone.Adapter.MainGridAdapter;
import oceannet.servizoone.AppController;
import oceannet.servizoone.Constant.Sabitler;
import oceannet.servizoone.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GridView gridViewMain;
//    static final String[] menu = {"Servis Bilgilerim","Profilim","Servisim Nerede","Taleplerim","Güzergahım","Diğer Güzergahlar","Bildirimler"};
    static final String[] menu = {"Servis Bilgilerim","Profilim","Servisim Nerede","Taleplerim","Güzergahım","Diğer Güzergahlar"};
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
           TextView txt_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppController.getInstance().requestPermissionBluethoothAdmin(this);
        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor=preferences.edit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        gridViewMain= (GridView) findViewById(R.id.gridview_main);
        gridViewMain.setAdapter(new MainGridAdapter(this, menu));

        txt_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_nameN);
        txt_name.setText(preferences.getString(Sabitler.NAME,""));

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)

                displayPromptForEnablingBluttoth(this);
            }
        }

         LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

            displayPromptForEnablingGPS(this);
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bildirim) {


            Intent taleplerimIntent = new Intent(MainActivity.this,Avt_Notification.class);
            startActivity(taleplerimIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_profil) {
            // Handle the camera action
            Intent profileIntent = new Intent(MainActivity.this,ProfileActivity.class);
            startActivity(profileIntent);
        } else if (id == R.id.drawer_servis_bilgileri) {
            Intent servisBilgilerimIntent = new Intent(MainActivity.this,ServisBilgilerimActivity.class);
            startActivity(servisBilgilerimIntent);
        } else if (id == R.id.drawer_servisim_nerede) {
            Intent servisimNeredeIntent = new Intent(MainActivity.this,ServisimNeredeActivity.class);
            startActivity(servisimNeredeIntent);
        } else if (id == R.id.drawer_bildirimlerim) {

            Intent taleplerimIntent = new Intent(MainActivity.this,Avt_Notification.class);
            startActivity(taleplerimIntent);

        } else if (id == R.id.drawer_taleplerim) {
            Intent taleplerimIntent = new Intent(MainActivity.this,TaleplerimActivity.class);
            startActivity(taleplerimIntent);
        } else if (id == R.id.drawer_guzergahlar) {

            Intent taleplerimIntent = new Intent(MainActivity.this,Avt_Routes.class);
            startActivity(taleplerimIntent);
        } else if (id == R.id.drawer_cikis) {

            editor.putBoolean("GirisKontrol",false);
            editor.commit();

            Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else if (id == R.id.drawer_versiyon) {
            Intent taleplerimIntent = new Intent(MainActivity.this,Avt_version.class);
            startActivity(taleplerimIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public  void displayPromptForEnablingGPS(final Activity activity)
    {


        final AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "GPS/konumu açmalısınız";

        builder.setMessage(message)
                .setPositiveButton("Tamam",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                                MainActivity.this.finish();

                            }
                        })
                .setNegativeButton("Hayır",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();


                            }
                        });
        builder.create().show();

    }

    public  void displayPromptForEnablingBluttoth(final Activity activity)
    {
        final AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_BLUETOOTH_SETTINGS;
        final String message = "Beacon noktalarını algılayabılmek için bluetooth un açık olmasi gereklidir";

        builder.setMessage(message)
                .setPositiveButton("Tamam",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                                MainActivity.this.finish();

                            }
                        })
                .setNegativeButton("Hayır",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();


                            }
                        });
        builder.create().show();

    }
}
