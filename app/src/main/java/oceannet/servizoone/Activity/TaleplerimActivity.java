package oceannet.servizoone.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import oceannet.servizoone.Adapter.Adapter_Taleperim;
import oceannet.servizoone.AppController;
import oceannet.servizoone.Constant.Sabitler;
import oceannet.servizoone.Model.Complaint;
import oceannet.servizoone.R;
import oceannet.servizoone.Utils.RecyclerItemClickListener;

/**
 * Created by Erdinc on 3.5.2017.
 */

public class TaleplerimActivity extends AppCompatActivity {

    FloatingActionButton talep_olustur_fab;
    SharedPreferences sharedPreferences;


    ArrayList<Complaint> complaints = new ArrayList<Complaint>();
    ArrayList<Complaint> complaintsSearch = new ArrayList<Complaint>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taleplerim);

        talep_olustur_fab= (FloatingActionButton) findViewById(R.id.taleplerim_fab);
        talep_olustur_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent talepOlusturIntent = new Intent(TaleplerimActivity.this, TalepOlusturActivity.class);
                startActivity(talepOlusturIntent);
            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.taleplerim_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.geri);
        ab.setDisplayHomeAsUpEnabled(true);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        progressDialog=new ProgressDialog(this);


        get_complaints();
        rcl_talep = (RecyclerView) findViewById(R.id.rcl_talep);

        LinearLayoutManager  layoutManager = new LinearLayoutManager(this);
        rcl_talep.setLayoutManager(layoutManager);

        rcl_talep.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                AppController.getInstance().setComplaint(complaints.get(position));


                Intent i = new Intent(TaleplerimActivity.this,Avt_ComplaintDetail.class);
                startActivity(i);



            }
        }));

    }

    @Override
    protected void onResume() {
        super.onResume();
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



                if (newText.length() >2){
                    complaintsSearch.clear();
                    for (int i = 0; i <complaints.size() ; i++) {
                                 Complaint com = complaints.get(i);

                if (com.getTitle().contains(newText) || com.getMessage().contains(newText)) {
                            complaintsSearch.add(com);
                        }

                    }
                    adapter_talepelrim =  new Adapter_Taleperim(complaintsSearch,rcl_talep);

                    rcl_talep.setAdapter(adapter_talepelrim);
                    adapter_talepelrim.notifyDataSetChanged();
                }else
                {
                    adapter_talepelrim =  new Adapter_Taleperim(complaints,rcl_talep);

                    rcl_talep.setAdapter(adapter_talepelrim);
                    adapter_talepelrim.notifyDataSetChanged();

                }
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
              //  Toast.makeText(getApplicationContext(),newText,Toast.LENGTH_SHORT).show();
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

    Adapter_Taleperim adapter_talepelrim;

    RecyclerView rcl_talep;
    public void get_complaints(){

        progressDialog.setMessage("Yükleniyor. Lütfen Bekleyiniz");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AndroidNetworking.get(Sabitler.GETCOMPLAINTS)
                .addHeaders("Authorization",sharedPreferences.getString("token_type","b")+" "+sharedPreferences.getString("access_token","a"))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // pDialog.dismiss();
                        System.out.println("zassGelen complait Servis Bilgisi Json" + response.toString());
                        progressDialog.dismiss();
                        try {
                            if (response.getJSONObject("status").getInt("response_code")==200){
                                System.out.println("GİRİŞ BAŞARILI");

                                JSONArray data  = response.getJSONArray("data");

                                complaints.clear();
                                for (int i = 0; i <data.length() ; i++) {

                                    JSONObject Com = data.getJSONObject(i);
                                 Complaint comlaint = new Complaint(Com.getInt("ID"),Com.getString("Category"),Com.getString("Title"),Com.getString("Message"),Com.getString("DateTime"),Com.getBoolean("isAnswered")) ;
                                    complaints.add(comlaint);




                                }
                                adapter_talepelrim =  new Adapter_Taleperim(complaints,rcl_talep);

                                rcl_talep.setAdapter(adapter_talepelrim);

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
    //    progressDialog.dismiss();
    }
}
