package oceannet.servizoone.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import oceannet.servizoone.AppController;
import oceannet.servizoone.Constant.Sabitler;
import oceannet.servizoone.R;
/**
 * Created by Erdinc on 4.5.2017.
 */

public class GirisActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor=preferences.edit();
        AppController.getInstance().requestPermissionBluethoothAdmin(this);


        String permission =  Manifest.permission.ACCESS_FINE_LOCATION;
        if(ActivityCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED) {
            // Proceed with your code execution

            System.out.println("local permission");


        } else {

            requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.ACCESS_FINE_LOCATION},
                    5);

        }

        if (GirisKontrol()){
            menuekraninagec();
        }else {
            Intent loginIntent = new Intent(GirisActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    private void requestPermissions(GirisActivity girisActivity, String[] strings, int i) {

    }

    private void menuekraninagec (){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        ImageView giris = (ImageView) findViewById(R.id.giris_foto);
        anim.reset();
        giris.clearAnimation();
        giris.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent loginIntent = new Intent(GirisActivity.this,MainActivity.class);
                startActivity(loginIntent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public Boolean GirisKontrol(){
        return preferences.getBoolean(Sabitler.GIRIS_KONTROL,false);
    }
}
