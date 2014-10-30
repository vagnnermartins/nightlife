package br.com.nightlife.pojo;

import java.util.ArrayList;
import java.util.List;

import br.com.nightlife.R;
import br.com.nightlife.fragment.BaladaFragment;
import br.com.nightlife.fragment.EstacionamentoFragment;
import br.com.nightlife.fragment.EventoFragment;
import br.com.nightlife.fragment.MeusEventosFragment;
import br.com.nightlife.fragment.RestauranteFragment;
import br.com.nightlife.fragment.SobreFragment;
import br.com.nightlife.fragment.TaxiFragment;
import br.com.nightlife.parse.RestauranteParse;

public class MenuPojo {

    private String fragmentName;
    private int idStringResource;
    private int idDrawableResource;

    private MenuPojo(String fragmentName, int idStringResource, int idDrawableResource) {
        this.fragmentName = fragmentName;
        this.idStringResource = idStringResource;
        this.idDrawableResource = idDrawableResource;
    }

    /**
     * Cada item representa um item do menu da aplicação
     * @return
     */
    public static List<MenuPojo> getItemsMenu(){
        List<MenuPojo> list = new ArrayList<MenuPojo>();
        list.add(new MenuPojo(BaladaFragment.class.getName(), R.string.fragment_baladas, R.drawable.ic_menu_balada));
        list.add(new MenuPojo(EventoFragment.class.getName(), R.string.fragment_evento, R.drawable.ic_menu_evento));
        list.add(new MenuPojo(MeusEventosFragment.class.getName(), R.string.fragment_meus_eventos, R.drawable.ic_menu_meus_eventos));
        list.add(new MenuPojo(RestauranteFragment.class.getName(), R.string.fragment_restaurante, R.drawable.ic_menu_restaurantes));
        list.add(new MenuPojo(TaxiFragment.class.getName(), R.string.fragment_taxi, R.drawable.ic_menu_taxi));
        list.add(new MenuPojo(EstacionamentoFragment.class.getName(), R.string.fragment_estacionamento, R.drawable.ic_menu_estacionamento));
        list.add(new MenuPojo(SobreFragment.class.getName(), R.string.fragment_sobre, R.drawable.ic_menu_sobre));
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
