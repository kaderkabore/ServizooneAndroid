package oceannet.servizoone.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import oceannet.servizoone.Model.Route;
import oceannet.servizoone.R;

/**
 * Created by oceannet on 25/05/17.
 */

public class Adapter_Routes extends RecyclerView.Adapter {



    ArrayList<Route> notifications ;

    public Adapter_Routes(  ArrayList<Route> notifications, RecyclerView mRecyclerView) {
        this.notifications = notifications;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_routes, parent, false);


        vh = new ViewHolder(v);

        return vh;
    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ViewHolder view  = (ViewHolder) holder;
        Route noti = notifications.get(position);
        view.txt_tittle.setText(noti.getRouteName());
        view.txt_message.setText(noti.getRoadInfo());
        view.txt_time.setText("Kapasite: "+noti.getCapacity());

        view.txt_average.setText("Kayıtlı: "+noti.getRegistered());
        view.txt_register.setText("Ortalama: "+noti.getAverage());

    }

    @Override
    public int getItemCount() {
        return notifications.size();

    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_tittle;
        public TextView txt_message;
        public TextView txt_time;
        public TextView txt_register;
        public TextView txt_average;




        public ViewHolder(View v) {
            super(v);
            txt_tittle = (TextView) v.findViewById(R.id.txt_Tittle);
            txt_message = (TextView) v.findViewById(R.id.txt_message);
            txt_time = (TextView) v.findViewById(R.id.txt_time);
            txt_register = (TextView) v.findViewById(R.id.txt_register);
            txt_average = (TextView) v.findViewById(R.id.txt_average);


        }
    }


}
