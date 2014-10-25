package br.com.nightlife.pojo;

import java.util.ArrayList;
import java.util.List;

import br.com.nightlife.R;
import br.com.nightlife.fragment.BaladaFragment;
import br.com.nightlife.fragment.EventoFragment;
import br.com.nightlife.fragment.TaxiFragment;

/**
 * Created by vagnnermartins on 24/10/14 .
 */
public class MenuPojo {

    private String fragmentName;
    private int idStringResource;
    private int idDrawableResource;

    private MenuPojo(String fragmentName, int idStringResource, int idDrawableResource) {
        this.fragmentName = fragmentName;
        this.idStringResource = idStringResource;
        this.idDrawableResource = idDrawableResource;
    }

    public static List<MenuPojo> getItemsMenu(){
        List<MenuPojo> list = new ArrayList<MenuPojo>();
        list.add(new MenuPojo(BaladaFragment.class.getName(), R.string.fragment_baladas, R.drawable.ic_menu_balada));
        list.add(new MenuPojo(TaxiFragment.class.getName(), R.string.fragment_taxi, R.drawable.ic_menu_taxi));
        list.add(new MenuPojo(EventoFragment.class.getName(), R.string.fragment_evento, R.drawable.ic_menu_evento));
        return list;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public int getIdStringResource() {
        return idStringResource;
    }

    public int getIdDrawableResource() {
        return idDrawableResource;
    }
}
