package oceannet.servizoone.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import oceannet.servizoone.Adapter.ServisBilgilerimAdapter;
import oceannet.servizoone.Constant.Sabitler;
import oceannet.servizoone.Model.Authorized;
import oceannet.servizoone.Model.Header;
import oceannet.servizoone.Model.Service;
import oceannet.servizoone.R;

/**
 * Created by Erdinc on 9.5.2017.
 */

public class ServisBilgilerimActivity extends AppCompatActivity {

    String[] izinler = {
            android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.READ_PHONE_STATE
    };
    int izinKodu = Sabitler.SERVIS_BILGILERIM_PERMISSIONS;

    private List<Object> tumListe = new ArrayList<Object>();
    private RecyclerView recycler_view;
    ServisBilgilerimAdapter adapter;
    private LinearLayoutManager layoutManager;


    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servis_bilgilerim);

        Toolbar toolbar = (Toolbar) findViewById(R.id.servis_bilgilerim_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.geri);
        ab.setDisplayHomeAsUpEnabled(true);


        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        progressDialog=new ProgressDialog(this);
        recycler_view = (RecyclerView) findViewById(R.id.servis_bilgilerim_recyclerview);
        recycler_view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);

        /*tumListe.add(new Header("Servis ve Şoför Bilgileri"));
        tumListe.add(new Service(0, "Erdinç Ayvaz", "05301108138", "021 - Talas", "19 NA 799", "Ford Transit", "Ayvaz Gıda"));
        tumListe.add(new Header("Yetkili Bilgileri"));
        tumListe.add(new Authorized("Erdinç Ayvaz", "05301108138"));
        tumListe.add(new Authorized("Erdinç Ayvaz", "05301108138"));
        tumListe.add(new Authorized("Erdinç Ayvaz", "05301108138"));
        tumListe.add(new Authorized("Erdinç Ayvaz", "05301108138"));
        tumListe.add(new Authorized("Erdinç Ayvaz", "05301108138"));

        adapter = new ServisBilgilerimAdapter(tumListe, recycler_view);
        recycler_view.setAdapter(adapter);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //  && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                  ) {
                //-- Eğer almak istediğimiz izinler daha önceden kullanıcı tarafından onaylanmış ise bu kısımda istediğimiz işlemleri yapabiliriz..
                //-- Mesela uygulama açılışında SD Kart üzerindeki herhangi bir dosyaya bu kısımda erişebiliriz.
            } else {
                //-- Almak istediğimiz izinler daha öncesinde kullanıcı tarafından onaylanmamış ise bu kod bloğu harekete geçecektir.
                //-- Burada requestPermissions() metodu ile kullanıcıdan ilgili Manifest izinlerini onaylamasını istiyoruz.
                requestPermissions(izinler, izinKodu);
            }
        }

        ServisBilgileriniGetir();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 67: {
                //-- Kullanıcı izin isteğini iptal ederse if - else bloğunun içindeki kodlar çalışmayacaktır. Böyle bir durumda yapılacak işlemleri bu kısımda kodlayabilirsiniz.

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //-- Eğer kullanıcı istemiş olduğunuz izni onaylarsa bu kod bloğu çalışacaktır.

                } else {

                    //-- Kullanıcı istemiş olduğunuz izni reddederse bu kod bloğu çalışacaktır.

                }
                return;
            }

            //-- Farklı 'case' blokları ekleyerek diğer izin işlemlerinizin sonuçlarını da kontrol edebilirsiniz.. Biz burada sadece değerini 67 olarak tanımladığımız izin işlemini kontrol ettik.

        }
    }
    ProgressDialog progressDialog;

    public void ServisBilgileriniGetir(){

        progressDialog.setMessage("Yükleniyor. Lütfen Bekleyiniz");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AndroidNetworking.get(Sabitler.URL_GETSERVICEINFO)
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
                                System.out.println("GİRİŞ BAŞARILI");

                                tumListe.add(new Header("Servis ve Şoför Bilgileri"));
                                tumListe.add(new Service(response.getJSONObject("data").getInt("ServiceID"), response.getJSONObject("data").getString("DriverFullName"), response.getJSONObject("data").getString("DriverPhone"), response.getJSONObject("data").getString("ServiceName"), response.getJSONObject("data").getString("Plate"), response.getJSONObject("data").getString("BrandModel"), response.getJSONObject("data").getString("CompanyName")));
                                tumListe.add(new Header("Yetkili Bilgileri"));
                                JSONArray jsonArray = response.getJSONObject("data").getJSONArray("AuthorizedList");
                                for (int i =0; i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    tumListe.add(new Authorized(jsonObject.getString("authorizedFullName"),jsonObject.getString("authorizedPhone")));
                                }
                                adapter = new ServisBilgilerimAdapter(tumListe, recycler_view);
                                recycler_view.setAdapter(adapter);
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
     //   progressDialog.dismiss();
    }
}
