package oceannet.servizoone.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.view.View;
import android.widget.SearchView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import oceannet.servizoone.Adapter.Adapter_Routes;
import oceannet.servizoone.AppController;
import oceannet.servizoone.Constant.Sabitler;
import oceannet.servizoone.Model.Route;
import oceannet.servizoone.R;
import oceannet.servizoone.Utils.RecyclerItemClickListener;

public class Avt_Routes extends AppCompatActivity {


    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avt__routes);
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
        get_routes();
        rcl_talep.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                AppController.getInstance().setRoute(routes.get(position));


                Intent i = new Intent(Avt_Routes.this,Avt_RouteDetail.class);
                startActivity(i);

            }
        }));

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
                //buraya tıklandıkça yaoılacak aktivite yazılacak


                if (newText.length() >2){
                    routesSearch.clear();
                    for (int i = 0; i <routes.size() ; i++) {
                        Route com = routes.get(i);

                        if (com.getRouteName().contains(newText) || com.getRoadInfo().contains(newText)) {
                            routesSearch.add(com);
                        }

                    }
                    adapter_no =  new Adapter_Routes(routesSearch,rcl_talep);

                    rcl_talep.setAdapter(adapter_no);
                    adapter_no.notifyDataSetChanged();
                }else
                {
                    adapter_no =  new Adapter_Routes(routes,rcl_talep);

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

    Adapter_Routes adapter_no;

    RecyclerView rcl_talep;
    ArrayList<Route> routes = new ArrayList<Route>();
    ArrayList<Route> routesSearch = new ArrayList<Route>();


    public void get_routes(){

        progressDialog.setMessage("Yükleniyor. Lütfen Bekleyiniz");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AndroidNetworking.get(Sabitler.GET_ROUTES)
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
                                    Route comlaint = new Route(Com.getInt("ID"),Com.getString("RouteName"),Com.getString("RoadInfo"),Com.getInt("Capacity"),Com.getInt("Registered"),Com.getDouble("Average")) ;
                                    routes.add(comlaint);


                                }
                                adapter_no =  new Adapter_Routes(routes,rcl_talep);

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
