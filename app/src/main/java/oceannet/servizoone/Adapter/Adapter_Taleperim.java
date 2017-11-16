package oceannet.servizoone.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import oceannet.servizoone.Model.Complaint;
import oceannet.servizoone.R;

/**
 * Created by oceannet on 24/05/17.
 */

public class Adapter_Taleperim extends RecyclerView.Adapter {



    ArrayList<Complaint> notifications ;

    public Adapter_Taleperim(  ArrayList<Complaint> notifications, RecyclerView mRecyclerView) {
        this.notifications = notifications;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_taleplerim, parent, false);


        vh = new ViewHolder(v);

        return vh;
    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ViewHolder view  = (ViewHolder) holder;
          Complaint com = notifications.get(position);
        view.txt_tittle.setText(com.getTitle());
        view.txt_message.setText(com.getMessage());

        view.txt_time.setText(com.getDateTime());

        String dateString = com.getDateTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS");
        System.out.println("zassss date  bildirim"  +dateString);

        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);

            System.out.println("zassss date converted  bildirim"  +convertedDate.toString());

            SimpleDateFormat  formatter = new SimpleDateFormat(" yyyy MMM dd hh:mm ");

            System.out.println(convertedDate);
            view.txt_time.setText(formatter.format(convertedDate).toString());


        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return notifications.size();

    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_tittle;
        public TextView txt_message;
        public TextView txt_time;



        public ViewHolder(View v) {
            super(v);
            txt_tittle = (TextView) v.findViewById(R.id.txt_Tittle);
            txt_message = (TextView) v.findViewById(R.id.txt_message);
            txt_time = (TextView) v.findViewById(R.id.txt_time);



        }
    }


}
