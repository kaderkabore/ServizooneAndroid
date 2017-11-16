package oceannet.servizoone.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import oceannet.servizoone.Adapter.Adapter_ComplaintDetail;
import oceannet.servizoone.AppController;
import oceannet.servizoone.Constant.Sabitler;
import oceannet.servizoone.Model.AnswerList;
import oceannet.servizoone.R;


public class Avt_ComplaintDetail extends AppCompatActivity {


    public TextView txt_tittle,txt_tittleM;
    public TextView txt_message;
    public TextView txt_time;

   // ProgressDialog progressDialog;


    RecyclerView rcl_talep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avt__complaint_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            txt_tittle = (TextView) findViewById(R.id.txt_Tittle);
        txt_tittleM = (TextView) findViewById(R.id.txt_TittleM);
            txt_message = (TextView) findViewById(R.id.txt_message);
            txt_time = (TextView) findViewById(R.id.txt_time);
        rcl_talep = (RecyclerView) findViewById(R.id.rcl_talep);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.geri);
        ab.setDisplayHomeAsUpEnabled(true);
        get_Answer();
    }


    ArrayList<AnswerList> answers =  new ArrayList<AnswerList>();
    Adapter_ComplaintDetail  adapter_no;

    public void get_Answer(){

//        progressDialog.setMessage("Yükleniyor. Lütfen Bekleyiniz");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();

        AndroidNetworking.get(Sabitler.GETCOMPLAINTDETAIL)
                .addHeaders("Authorization", AppController.getInstance().getToken())
                .addQueryParameter("id",""+AppController.getInstance().getComplaint().getID())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // pDialog.dismiss();
                        System.out.println("zassGelen complait detailServis Bilgisi Json" + response.toString());
                        //progressDialog.dismiss();
                        try {
                            if (response.getJSONObject("status").getInt("response_code")==200){
                                System.out.println("GİRİŞ BAŞARILI");

                                JSONObject dataw  = response.getJSONObject("data");

                                txt_tittleM.setText("Kategori:  "+dataw.getString("Category"));
                                txt_tittle.setText("  "+dataw.getString("Title"));
                                txt_message.setText(" "+dataw.getString("Message"));
                                txt_time.setText("  "+dataw.getString("ComplaintDateTime"));


                                String dateString = dataw.getString("ComplaintDateTime")+"";
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS");
                                System.out.println("zassss date  bildirim"  +dateString);

                                Date convertedDate = new Date();
                                try {
                                    convertedDate = dateFormat.parse(dateString);

                                    System.out.println("zassss date converted  bildirim"  +convertedDate.toString());

                                    SimpleDateFormat  formatter = new SimpleDateFormat(" yyyy MMM dd hh:mm ");

                                    System.out.println(convertedDate);
                                    txt_time.setText(formatter.format(convertedDate).toString());


                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                JSONArray data  = response.getJSONObject("data").getJSONArray("AnswerList");

                                for (int i = 0; i <data.length() ; i++) {

                                    JSONObject Com = data.getJSONObject(i);
                                    AnswerList comlaint = new AnswerList(Com.getString("Message"),Com.getString("DateTime")) ;



                                    answers.add(comlaint);


                                }
                                adapter_no =  new Adapter_ComplaintDetail(answers,rcl_talep);

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
                        //  progressDialog.dismiss();
                    }
                });
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
}
