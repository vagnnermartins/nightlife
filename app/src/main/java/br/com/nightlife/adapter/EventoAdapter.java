package br.com.nightlife.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseObject;

import java.text.ParseException;
import java.util.List;

import br.com.metasix.olhos_do_rio.componentebox.lib.util.DataUtil;
import br.com.nightlife.R;
import br.com.nightlife.parse.EventoParse;

/**
 * Created by vagnnermartins on 25/10/14.
 */
public class EventoAdapter extends ArrayAdapter<ParseObject> {

    private final int resource;

    public EventoAdapter(Context context, int resource, List<ParseObject> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        EventoViewHolder viewHolder;
        if(convertView == null){
            convertView = parent.inflate(getContext(), resource, null);
            viewHolder = new EventoViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (EventoViewHolder) convertView.getTag();
        }
        EventoParse item = (EventoParse) getItem(position);
        viewHolder.nome.setText(item.getNome());
        viewHolder.descricao.setText(item.getDescricao());
        try {
            viewHolder.data.setText(DataUtil.obterDataPorExetenso(item.getData()));
            viewHolder.hora.setText(DataUtil.transformDateToSting(item.getData(), "HH:mm"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public class EventoViewHolder{
        TextView nome;
        TextView descricao;
        TextView data;
        TextView hora;

        public EventoViewHolder(View view){
            this.nome = (TextView) view.findViewById(R.id.item_evento_nome);
            this.descricao = (TextView) view.findViewById(R.id.item_evento_descricao);
            this.data = (TextView) view.findViewById(R.id.item_evento_data);
            this.hora = (TextView) view.findViewById(R.id.item_evento_hora);
        }
    }
}
