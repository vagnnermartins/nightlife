package br.com.nightlife.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseObject;

import java.util.List;

import br.com.metasix.olhos_do_rio.componentebox.lib.util.DistanciaUtil;
import br.com.nightlife.R;
import br.com.nightlife.parse.BaladaParse;
import br.com.nightlife.parse.RestauranteParse;

/**
 * Created by vagnnermartins on 25/10/14.
 */
public class RestauranteAdapter extends ArrayAdapter<ParseObject> {

    private final int resource;
    private final LatLng location;

    public RestauranteAdapter(Context context, int resource, List<ParseObject> objects, LatLng location) {
        super(context, resource, objects);
        this.resource = resource;
        this.location = location;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        BaladaViewHolder viewHolder;
        if(convertView == null){
            convertView = parent.inflate(getContext(), resource, null);
            viewHolder = new BaladaViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (BaladaViewHolder) convertView.getTag();
        }
        RestauranteParse item = (RestauranteParse) getItem(position);
        viewHolder.nome.setText(item.getNome());
        viewHolder.telefone.setText(item.getTelefone());
        viewHolder.endereco.setText(item.getEndereco());
        if(location != null){
            double distancia = DistanciaUtil.calcularDistanciaEntreDoisPontos(item.getLocalizacao().getLatitude(), item.getLocalizacao().getLongitude(),
                    location.latitude, location.longitude);
            viewHolder.distancia.setText(DistanciaUtil.distanciaEmMetrosPorExtenso(distancia));
        }
        return convertView;
    }

    public class BaladaViewHolder{
        TextView nome;
        TextView telefone;
        TextView endereco;
        public TextView distancia;

        public BaladaViewHolder(View view){
            this.nome = (TextView) view.findViewById(R.id.item_restaurante_nome);
            this.telefone = (TextView) view.findViewById(R.id.item_restaurante_telefone);
            this.endereco = (TextView) view.findViewById(R.id.item_restaurante_endereco);
            this.distancia = (TextView) view.findViewById(R.id.item_restaurante_distancia);
        }
    }
}
