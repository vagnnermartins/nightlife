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
import br.com.nightlife.parse.TaxiParse;

public class TaxiAdapter extends ArrayAdapter<ParseObject> {

    private final int resource;
    private final LatLng location;

    public TaxiAdapter(Context context, int resource, List<ParseObject> objects, LatLng location) {
        super(context, resource, objects);
        this.resource = resource;
        this.location = location;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        TaxiViewHolder viewHolder;
        if(convertView == null){
            convertView = parent.inflate(getContext(), resource, null);
            viewHolder = new TaxiViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (TaxiViewHolder) convertView.getTag();
        }
        TaxiParse item = (TaxiParse) getItem(position);
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

    public class TaxiViewHolder{
        TextView nome;
        TextView telefone;
        TextView endereco;
        public TextView distancia;

        public TaxiViewHolder(View view){
            this.nome = (TextView) view.findViewById(R.id.item_taxi_nome);
            this.telefone = (TextView) view.findViewById(R.id.item_taxi_telefone);
            this.endereco = (TextView) view.findViewById(R.id.item_taxi_endereco);
            this.distancia = (TextView) view.findViewById(R.id.item_taxi_distancia);
        }
    }
}
