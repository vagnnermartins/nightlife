package br.com.nightlife.tabview;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseObject;

import java.util.List;

import br.com.metasix.olhos_do_rio.componentebox.lib.tab.AbstractItemView;
import br.com.nightlife.R;
import br.com.nightlife.adapter.BaladaAdapter;
import br.com.nightlife.adapter.EventoAdapter;
import br.com.nightlife.adapter.RestauranteAdapter;
import br.com.nightlife.adapter.TaxiAdapter;
import br.com.nightlife.fragment.BaladaFragment;
import br.com.nightlife.fragment.EventoFragment;
import br.com.nightlife.fragment.RestauranteFragment;
import br.com.nightlife.fragment.TaxiFragment;

/**
 * Created by vagnnermartins on 27/10/14 .
 */
public class ListTabView extends AbstractItemView {

    private final Fragment fragment;
    private final View view;
    public ListTabViewUiHelper uiHelper;
    private ArrayAdapter listAdapter;

    public ListTabView(Fragment fragment, View view){
        this.fragment = fragment;
        this.view = view;
        init();
    }

    private void init() {
        uiHelper = new ListTabViewUiHelper();
        uiHelper.searchView.setOnQueryTextListener(configurarOnQueryTextListener());
    }

    private SearchView.OnQueryTextListener configurarOnQueryTextListener() {
        return new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(listAdapter != null){
                    listAdapter.getFilter().filter(newText);
                }
                return false;
            }
        };
    }

    public void update(List<ParseObject> itens){
        if(fragment instanceof EventoFragment ){
            listAdapter = new EventoAdapter(fragment.getActivity(), R.layout.item_evento, itens);
        }
        uiHelper.listView.setAdapter(listAdapter);
    }

    public void update(List<ParseObject> itens, LatLng location){
        if(fragment instanceof TaxiFragment){
            listAdapter = new TaxiAdapter(fragment.getActivity(), R.layout.item_taxi, itens, location);
        }else if(fragment instanceof BaladaFragment){
            listAdapter = new BaladaAdapter(fragment.getActivity(), R.layout.item_balada, itens, location);
        }else if(fragment instanceof RestauranteFragment){
            listAdapter = new RestauranteAdapter(fragment.getActivity(), R.layout.item_restaurante, itens, location);
        }
        uiHelper.listView.setAdapter(listAdapter);
    }

    @Override
    public String getTabName() {
        return "";
    }

    @Override
    public Drawable getIcon() {
        return fragment.getResources().getDrawable(R.drawable.ic_tabbar_list);
    }

    @Override
    public View getView() {
        return view;
    }

    public class ListTabViewUiHelper{

        private final SearchView searchView;
        public final ListView listView;

        public ListTabViewUiHelper(){
            searchView = (SearchView) view.findViewById(R.id.tabview_search);
            listView = (ListView) view.findViewById(R.id.tabview_list);
        }
    }
}
