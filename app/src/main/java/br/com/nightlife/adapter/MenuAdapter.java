package br.com.nightlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.nightlife.R;
import br.com.nightlife.pojo.MenuPojo;

/**
 * Created by vagnnermartins on 24/10/14 .
 */
public class MenuAdapter extends ArrayAdapter<MenuPojo>{

    private int resource;

    public MenuAdapter(Context context, int resource, List<MenuPojo> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ItemHolder itemHolder;
        MenuPojo itemMenu = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, null);
            itemHolder = new ItemHolder(convertView);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }

        if (itemMenu != null) {
            itemHolder.name.setText(itemMenu.getIdStringResource());
            itemHolder.image.setImageResource(itemMenu.getIdDrawableResource());
        }

        return convertView;
    }

    public class ItemHolder {
        TextView name;
        ImageView image;

        ItemHolder(View view) {
            this.name = (TextView) view.findViewById(R.id.item_menu_name);
            this.image = (ImageView) view.findViewById(R.id.item_menu_image);
        }
    }
}
