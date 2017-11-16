package oceannet.servizoone.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import oceannet.servizoone.Constant.Sabitler;
import oceannet.servizoone.R;
/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    RelativeLayout relative;
    LinearLayout linear;
    EditText eMail,password;
    Button loginButton;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String mEmail,mPassword;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor=preferences.edit();

        progressDialog = new ProgressDialog(LoginActivity.this);
        eMail= (EditText) findViewById(R.id.email);
      eMail.setText("erdinc@gmail.com");
        password= (EditText) findViewById(R.id.password);
        password.setText("X1MGAC");
        loginButton= (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail=eMail.getText().toString();
                mPassword=password.getText().toString();
                GirisYap();
            }
        });

        relative = (RelativeLayout)findViewById(R.id.login_relative);
        relative.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(relative.getApplicationWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        linear= (LinearLayout) findViewById(R.id.login_linear);
        linear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(linear.getApplicationWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            }
        });


    }

    public void GirisYap(){


        if ( mEmail.length() < 3 ) {

            return;
        }
        if (  mPassword.length() < 3) {

            return;
        }
        progressDialog.setMessage("Giris Yapılıyor. Lütfen Bekleyiniz");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String httpPostBody = "username=" + mEmail + "" + "&password=" + mPassword + "&deviceToken=" + "aasdsadas" + "&deviceType=" + "1" + "&grant_type=password";

        AndroidNetworking.post(Sabitler.URL_TOKEN)
                .addByteBody(httpPostBody.getBytes())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // pDialog.dismiss();
                        System.out.println("Gelen Login Json" + response.toString());
                        progressDialog.dismiss();

                        try {
                            String jsonn = response.getString("status");
                            JSONObject girisJsonObject = new JSONObject(jsonn);
                            if (girisJsonObject.getInt("response_code")==200){
                                //Giriş Bilgileri Doğru
                                editor.putBoolean(Sabitler.GIRIS_KONTROL,true);
                                editor.putString(Sabitler.ACCESS_TOKEN,response.getString("access_token"));
                                editor.putString(Sabitler.TOKEN_TYPE,response.getString("token_type"));
                                editor.putString(Sabitler.EXPIRES_IN,response.getString("expires_in"));
                                editor.putString(Sabitler.USER_NAME,response.getString("userName"));

                                editor.putString(Sabitler.NAME,response.getString("FullName"));
                                editor.commit();
                                Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                                //System.out.println("Response_İçeriği"+response.getString("access_token").toString());
                            }
                            else {
                                //Giriş Bilgileri yanlış
                                editor.putBoolean("GirisKontrol",false);
                                editor.commit();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();

                        Toast.makeText(LoginActivity.this,"Invalid password or username!",Toast.LENGTH_LONG).show();
                        System.out.println("Login Hatası" + anError.getErrorBody());

                    }
                });
    }
}

