package oceannet.servizoone.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import oceannet.servizoone.Constant.Sabitler;
import oceannet.servizoone.R;
/**
 * Created by Erdinc on 3.5.2017.
 */

public class ProfileActivity  extends AppCompatActivity{
    SharedPreferences sharedPreferences;

       TextView txtname,txt_firmaAdi,txt_lokasyon,txt_email,txt_tel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar)findViewById(R.id.profile_activity_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.geri);
        ab.setDisplayHomeAsUpEnabled(true);




        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        progressDialog=new ProgressDialog(this);


        txtname = (TextView) findViewById(R.id.txt_name);
        txt_email = (TextView) findViewById(R.id.txt_email);

        txt_firmaAdi = (TextView) findViewById(R.id.txt_firmaadi);

        txt_lokasyon = (TextView) findViewById(R.id.txt_lokasyon);

        txt_tel = (TextView) findViewById(R.id.txt_tel);
        txtname.setText(sharedPreferences.getString(Sabitler.NAME,""));

        System.out.println("zassss " +sharedPreferences.getString(Sabitler.NAME,""));

        getInfo();

        new ProfilBilgisiGetir().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_profile, menu);
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

    public void getInfo(){

        progressDialog.setMessage("Yükleniyor. Lütfen Bekleyiniz");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AndroidNetworking.get(Sabitler.GET_MYINFO)
                .addHeaders("Authorization",sharedPreferences.getString("token_type","b")+" "+sharedPreferences.getString("access_token","a"))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // pDialog.dismiss();
                        System.out.println("Gelen Servis Bilgisi Json" + response.toString());
                        progressDialog.dismiss();
                        try {
                            if (response.getJSONObject("status").getInt("response_code")==200){

                                         JSONObject data = response.getJSONObject("data");
                                txt_firmaAdi.setText(data.getString("CompanyName"));
                                txt_lokasyon.setText(data.getString("Lokasyon"));

                                txt_email.setText(data.getString("Email"));

                                txt_tel.setText(data.getString("Phone"));


                                System.out.println("GİRİŞ BAŞARILI");

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




    class ProfilBilgisiGetir extends AsyncTask<Void, Void, Void> {

        JSONArray jsonArray = null;
        JSONObject jsonObject=null;
        String json;

        protected void onPreExecute() {
            //İşlem Başlamadan Önce Yapılacak İşlemler
        }

        protected Void doInBackground(Void... unused) {

            AndroidNetworking.get("http://37.72.57.11:3741/api/Risk?clientRef=15316&userName=mustafa@semra.com.tr")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("RESPONSE_GET: " + response.toString());
                            json=response.toString();
                        }

                        @Override
                        public void onError(ANError anError) {
                            System.out.println("RESPONSE_GET_ERROR: " + anError.toString());
                        }
                    });
            return null;
        }
        protected void onPostExecute(Void unused) {
            //İşlem Bittiğinde Yapılacak İşlemler
        }
    }





}
