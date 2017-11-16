package oceannet.servizoone.Adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import oceannet.servizoone.Model.Authorized;
import oceannet.servizoone.Model.Header;
import oceannet.servizoone.Model.Service;
import oceannet.servizoone.R;

/**
 * Created by Erdinc on 9.5.2017.
 */

public class ServisBilgilerimAdapter extends RecyclerView.Adapter {

    private final int VIEW_HEADER = 0;
    private final int VIEW_AUTHORIZED = 1;
    private final int VIEW_SERVICE = 2;

    List<Object> tumListeler;

    public ServisBilgilerimAdapter(List<Object> tumListeler, RecyclerView mRecyclerView) {
        this.tumListeler = tumListeler;
        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView
                    .getLayoutManager();

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_servis_bilgilerim_header, parent, false);

            vh = new HeaderViewHolder(v);
        } else if (viewType == VIEW_SERVICE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_servis_bilgilerim_service, parent, false);

            vh = new ServiceViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_servis_bilgilerim_authorized, parent, false);

            vh = new AuthorizedViewHolder(v);
        }

        return vh;
    }

    @Override
    public int getItemViewType(int position) {

        if (tumListeler.get(position) instanceof Header) {
            return VIEW_HEADER;
        } else if (tumListeler.get(position) instanceof Authorized) {
            return VIEW_AUTHORIZED;
        } else {
            return VIEW_SERVICE;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {

            //final Header header = new Header("Hareketler",0);
            final Header header = (Header) tumListeler.get(position);
            ((HeaderViewHolder) holder).headerText.setText(header.getBaslik().toString());

        } else if (holder instanceof ServiceViewHolder) {

            //final Transactions transactions = new Transactions("asd","asd","asd","asd","asd","asd","asd",1);
            final Service service = (Service) tumListeler.get(position);
            ((ServiceViewHolder) holder).adSoyadText.setText(service.getDriverFullName().replace(" ",""));
            ((ServiceViewHolder) holder).telefonText.setText(service.getDriverPhone().replace(" ",""));
            ((ServiceViewHolder) holder).servisAdıText.setText(service.getServiceName());
            ((ServiceViewHolder) holder).plakaText.setText(service.getPlate());
            ((ServiceViewHolder) holder).markaText.setText(service.getBrandModel());
            ((ServiceViewHolder) holder).firmaText.setText(service.getCompanyName());

            ((ServiceViewHolder) holder).telefonText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(((ServiceViewHolder) holder).telefonText.getContext());
                    builder.setTitle(((ServiceViewHolder) holder).adSoyadText.getText());
                    builder.setMessage(((ServiceViewHolder) holder).telefonText.getText()+" aranacak...");
                    builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id) {
                            //İptal butonuna basılınca yapılacaklar.Sadece kapanması isteniyorsa boş bırakılacak
                        }
                    });


                    builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Tamam butonuna basılınca yapılacaklar
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + service.getDriverPhone()));
                            if (ActivityCompat.checkSelfPermission(((ServiceViewHolder) holder).telefonText.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            ((ServiceViewHolder) holder).telefonText.getContext().startActivity(intent);
                        }
                    });
                    builder.show();
                }
            });
        }else {
            //final AggTrans aggTrans = new AggTrans("qwe","qwe","qwe","qwe",2);
            final Authorized authorized = (Authorized) tumListeler.get(position);
            ((AuthorizedViewHolder)holder).adSoyadText.setText(authorized.getAuthorizedFullName());
            ((AuthorizedViewHolder)holder).telefonText.setText(authorized.getAuthorizedPhone());

            ((AuthorizedViewHolder)holder).telefonText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(((AuthorizedViewHolder) holder).telefonText.getContext());
                    builder.setTitle(((AuthorizedViewHolder) holder).adSoyadText.getText());
                    builder.setMessage(((AuthorizedViewHolder) holder).telefonText.getText()+" aranacak...");
                    builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id) {
                            //İptal butonuna basılınca yapılacaklar.Sadece kapanması isteniyorsa boş bırakılacak
                        }
                    });


                    builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Tamam butonuna basılınca yapılacaklar
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + authorized.getAuthorizedPhone()));
                            if (ActivityCompat.checkSelfPermission(((AuthorizedViewHolder) holder).telefonText.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            ((AuthorizedViewHolder) holder).telefonText.getContext().startActivity(intent);
                        }
                    });
                    builder.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tumListeler.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView headerText;

        public HeaderViewHolder(View v) {
            super(v);
            headerText = (TextView) v.findViewById(R.id.list_servis_bilgilerim_header_baslik);
        }
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {

        public TextView adSoyadText;
        public TextView telefonText;
        public TextView servisAdıText;
        public TextView plakaText;
        public TextView markaText;
        public TextView firmaText;


        public ServiceViewHolder(View v) {
            super(v);
            adSoyadText = (TextView) v.findViewById(R.id.service_isim2);
            telefonText = (TextView) v.findViewById(R.id.service_telefon2);
            servisAdıText = (TextView) v.findViewById(R.id.service_servisadı2);
            plakaText = (TextView) v.findViewById(R.id.service_plaka2);
            markaText = (TextView) v.findViewById(R.id.service_marka2);
            firmaText = (TextView) v.findViewById(R.id.service_firma2);
        }
    }

    public static class AuthorizedViewHolder extends RecyclerView.ViewHolder {

        public TextView adSoyadText;
        public TextView telefonText;


        public AuthorizedViewHolder(View v) {
            super(v);
            adSoyadText = (TextView) v.findViewById(R.id.authorized_isim2);
            telefonText = (TextView) v.findViewById(R.id.authorized_telefon2);
        }
    }
}
