package oceannet.servizoone.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import oceannet.servizoone.Adapter.Adapter_notifications;
import oceannet.servizoone.Constant.Sabitler;
import oceannet.servizoone.Model.Notification;
import oceannet.servizoone.R;

public class Avt_Notification extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avt__notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    final ActionBar ab = getSupportActionBar();
    ab.setHomeAsUpIndicator(R.drawable.geri);
    ab.setDisplayHomeAsUpEnabled(true);

        rcl_talep = (RecyclerView) findViewById(R.id.rcl_talep);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcl_talep.setLayoutManager(layoutManager);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        progressDialog=new ProgressDialog(this);
        get_complaints();




}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_taleplerim, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //buraya tıklandıkça yaoılacak aktivite yazılacak ******************************************************
                /*if (newText.equals("")){
                    //Burada önceki aramalar listeleniyor
                    AramaGecmisiVeritabani db = new AramaGecmisiVeritabani(getApplicationContext());
                    db.baglantiyiAc();
                    search_adapter = new KisilerAdapter(db.arama_gecmisi_getir());
                    db.baglantiyiKapat();
                    recycler_view.setHasFixedSize(true);
                    recycler_view.setAdapter(search_adapter);
                    recycler_view.setItemAnimator(new DefaultItemAnimator());
                }
                else {
                    kisi_listesi.clear();
                    aranan_kelime=newText;
                    new KisiGetir().execute();
                }*/
                if (newText.length() >2){
                    notificationsSearch.clear();
                    for (int i = 0; i <notifications.size() ; i++) {
                        Notification com = notifications.get(i);

                        if (com.getTitle().contains(newText) || com.getMessage().contains(newText)) {
                            notificationsSearch.add(com);
                        }

                    }
                    adapter_no =  new Adapter_notifications(notificationsSearch,rcl_talep);

                    rcl_talep.setAdapter(adapter_no);
                    adapter_no.notifyDataSetChanged();
                }else
                {
                    adapter_no =  new Adapter_notifications(notifications,rcl_talep);

                    rcl_talep.setAdapter(adapter_no);
                    adapter_no.notifyDataSetChanged();

                }
                return true;
            }
        });
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

    Adapter_notifications adapter_no;

    RecyclerView rcl_talep;
    ArrayList<Notification> notifications = new ArrayList<Notification>();
    ArrayList<Notification> notificationsSearch = new ArrayList<Notification>();

    public void get_complaints(){

        progressDialog.setMessage("Yükleniyor. Lütfen Bekleyiniz");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AndroidNetworking.get(Sabitler.GETNOTIFICATION)
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

                                JSONArray data  = response.getJSONArray("data");


                                for (int i = 0; i <data.length() ; i++) {

                                    JSONObject Com = data.getJSONObject(i);
                                    Notification comlaint = new Notification(Com.getInt("ID"),Com.getString("Title"),Com.getString("Message"),Com.getString("DateTime")) ;
                                    notifications.add(comlaint);


                                }
                                adapter_no =  new Adapter_notifications(notifications,rcl_talep);

                                rcl_talep.setAdapter(adapter_no);

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
                        progressDialog.dismiss();
                    }
                });
        progressDialog.dismiss();
    }
}
