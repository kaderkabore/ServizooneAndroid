package oceannet.servizoone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import oceannet.servizoone.Activity.Avt_MyRoute;
import oceannet.servizoone.Activity.Avt_Notification;
import oceannet.servizoone.Activity.Avt_Routes;
import oceannet.servizoone.Activity.ProfileActivity;
import oceannet.servizoone.Activity.ServisBilgilerimActivity;
import oceannet.servizoone.Activity.ServisimNeredeActivity;
import oceannet.servizoone.Activity.TaleplerimActivity;
import oceannet.servizoone.R;

/**
 * Created by Erdinc on 3.5.2017.
 */

public class MainGridAdapter extends BaseAdapter {

    LayoutInflater inflater;

    public MainGridAdapter(Context context, String[] isimler) {
        this.inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.isimler = isimler;
    }

    private Context context;
    private final String[] isimler;

    @Override
    public int getCount() {
        return isimler.length;
    }

    @Override
    public Object getItem(int position) {
        return isimler[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View gridView;
        gridView = inflater.inflate(R.layout.gridview_item, null);

        final TextView isim = (TextView) gridView.findViewById(R.id.gridview_item_name);
        ImageView image = (ImageView) gridView.findViewById(R.id.gridview_item_image);

        String ad = isimler[position];

        if (ad.equals("Servis Bilgilerim")){
            isim.setText("Servis Bilgilerim");
            image.setImageResource(R.drawable.icon11);
        }else if (ad.equals("Profilim")){
            isim.setText("Profilim");
            image.setImageResource(R.drawable.icon5);
        }else if (ad.equals("Servisim Nerede")){
            isim.setText("Servisim Nerede");
            image.setImageResource(R.drawable.icon10);
        }else if (ad.equals("Taleplerim")){
            isim.setText("Taleplerim");
            image.setImageResource(R.drawable.icon8);
        }else if (ad.equals("Güzergahım")){
            isim.setText("Güzergahım");
            image.setImageResource(R.drawable.icon6);


        }else if (ad.equals("Diğer Güzergahlar")){
            isim.setText("Diğer Güzergahlar");
            image.setImageResource(R.drawable.icon9 );
        }else {
            isim.setText("Bildirimler");
            image.setImageResource(R.drawable.icon10);
        }

        gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isimler[position].equals("Servis Bilgilerim")){
                    Intent servisBilgilerimIntent = new Intent(context,ServisBilgilerimActivity.class);
                    context.startActivity(servisBilgilerimIntent);
                }else if (isimler[position].equals("Profilim")){
                    Intent profilimIntent = new Intent(context, ProfileActivity.class);
                    context.startActivity(profilimIntent);
                }else if (isimler[position].equals("Servisim Nerede")){
                    Intent servisimNeredeIntent = new Intent(context,ServisimNeredeActivity.class);
                    context.startActivity(servisimNeredeIntent);
                }else if (isimler[position].equals("Taleplerim")){
                    Intent taleplerimIntent = new Intent(context, TaleplerimActivity.class);
                    context.startActivity(taleplerimIntent);
                }else if (isimler[position].equals("Güzergahım")){
                    Intent taleplerimIntent = new Intent(context, Avt_MyRoute.class);
                    context.startActivity(taleplerimIntent);

                }else if (isimler[position].equals("Diğer Güzergahlar")){

                    Intent taleplerimIntent = new Intent(context,Avt_Routes.class);
                    context.startActivity(taleplerimIntent);
                }else if (isimler[position].equals("Bildirimler")){
                    Intent taleplerimIntent = new Intent(context, Avt_Notification.class);
                    context.startActivity(taleplerimIntent);

                }
            }
        });

        return gridView;
    }
}
