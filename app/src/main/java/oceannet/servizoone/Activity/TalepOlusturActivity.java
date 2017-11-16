package oceannet.servizoone.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import oceannet.servizoone.Constant.Sabitler;
import oceannet.servizoone.R;
/**
 * Created by Erdinc on 3.5.2017.
 */

public class TalepOlusturActivity extends AppCompatActivity {

    LinearLayout linear;
    MaterialBetterSpinner materialDesignSpinner;
    String[] SPINNERLIST = {"Şikayet", "İstek", "Öneri"};

    EditText edt_title,edt_message;

    String category = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talep_olustur);

        Toolbar toolbar = (Toolbar)findViewById(R.id.talep_olustur_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.geri);
        ab.setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
         materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.talep_olustur_kategori_spinner);
        materialDesignSpinner.setAdapter(arrayAdapter);

        edt_title = (EditText) findViewById(R.id.talep_olustur_baslik_edit);
        edt_message = (EditText) findViewById(R.id.talep_olustur_aciklama_edit);


        linear= (LinearLayout) findViewById(R.id.talep_olustur_linear);
        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Klavye kapatma kodu
                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(linear.getApplicationWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });


        materialDesignSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                category = materialDesignSpinner.getText().toString();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_talep_olustur, menu);
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
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;

    public void click_kaydet(View view) {

        Map<String,String > param = new HashMap<String,String >();



        param.put("Category",category);
        param.put("Title",edt_title.getText().toString());

        param.put("Message",edt_message.getText().toString());

        if (category.length() < 2){
                     return;
        }
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        progressDialog=new ProgressDialog(this);

        progressDialog.setMessage("Yükleniyor. Lütfen Bekleyiniz");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AndroidNetworking.post(Sabitler.URL_ADDCOMPLAINT)
                .addJSONObjectBody(new JSONObject(param))
                . addHeaders("Authorization",sharedPreferences.getString("token_type","b")+" "+sharedPreferences.getString("access_token","a"))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();
                        System.out.println("zassss " +response.toString());
                        try {
                           if (response.getInt("response_code") == 200)
                            {
                        Toast.makeText(TalepOlusturActivity.this,"Öneriniz alınmıştır.",Toast.LENGTH_SHORT).show();

                                TalepOlusturActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        progressDialog.dismiss();

                    }
                });


    }
}
