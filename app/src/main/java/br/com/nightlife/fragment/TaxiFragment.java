package br.com.nightlife.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import br.com.metasix.olhos_do_rio.componentebox.lib.tab.AbstractItemView;
import br.com.metasix.olhos_do_rio.componentebox.lib.tab.TabBar;
import br.com.metasix.olhos_do_rio.componentebox.lib.util.DistanciaUtil;
import br.com.metasix.olhos_do_rio.componentebox.lib.util.NavegacaoUtil;
import br.com.nightlife.R;
import br.com.nightlife.activity.DetalheRestauranteActivity;
import br.com.nightlife.activity.DetalheTaxiActivity;
import br.com.nightlife.activity.MainActivity;
import br.com.nightlife.adapter.TaxiAdapter;
import br.com.nightlife.app.App;
import br.com.nightlife.callback.Callback;
import br.com.nightlife.enums.StatusEnum;
import br.com.nightlife.parse.RestauranteParse;
import br.com.nightlife.parse.TaxiParse;
import br.com.nightlife.tabview.ListTabView;
import br.com.nightlife.tabview.MapaTabView;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * Created by vagnnermartins on 27/10/14 .
 */
public class TaxiFragment extends Fragment implements PullToRefreshAttacher.OnRefreshListener {

    private static final double MIN_DISTANCIA = 5000;

    private App app;
    private View view;
    private MapaTabView mapaTabview;
    private ListTabView listTabView;
    private PullToRefreshAttacher attacher;
    private LatLng ultimaPosicao;
    private TaxiParse selected;
    private LinearLayout tabs;
    private boolean posicionarMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab, container, false);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapaTabview.initAfterStart();
        verificarAtualizar();
    }

    private void verificarAtualizar() {
        if(app.listTaxi == null){
            verificarStatus(StatusEnum.INICIO);
        }else{
            update();
        }
    }

    private void init() {
        getActivity().getActionBar().setTitle(R.string.fragment_taxi);
        attacher = ((MainActivity) getActivity()).attacher;
        app = (App) getActivity().getApplication();
        app.callback = configAtualizarLocation();
        mapaTabview = new MapaTabView(this, getActivity().getLayoutInflater().inflate(R.layout.tabview_mapa, null),configOnInfoWindowClickListener());
        listTabView = new ListTabView(this, getActivity().getLayoutInflater().inflate(R.layout.tabview_list, null));
        listTabView.uiHelper.listView.setOnItemLongClickListener(configOnItemLongClickListener());
        listTabView.uiHelper.listView.setOnItemClickListener(configOnItemClickListener());
        attacher.addRefreshableView(listTabView.uiHelper.listView, this);
        registerForContextMenu(listTabView.uiHelper.listView);
        tabs = (LinearLayout) view.findViewById(R.id.main_tabs);
        List<AbstractItemView> listTabs = new ArrayList<AbstractItemView>();
        listTabs.add(mapaTabview);
        listTabs.add(listTabView);
        new TabBar(getActivity(), tabs, listTabs);
    }

    public void verificarStatus(StatusEnum status){
        if(status == StatusEnum.INICIO){
            verificarInicio();
        }else if(status == StatusEnum.EXECUTANDO){
            verificarExecutando();
        }else if (status == StatusEnum.EXECUTADO){
            verificarExecutado();
        }
    }

    private void verificarInicio() {
        if(app.isInternetConnection()){
            ParseGeoPoint point = null;
            if(app.location != null){
                point = new ParseGeoPoint(app.location.latitude, app.location.longitude);
            }
            TaxiParse.getTaxiByLocation(point, configFindTaxiCallback());
            verificarStatus(StatusEnum.EXECUTANDO);
        }
    }

    private void verificarExecutando() {
        attacher.setRefreshing(true);
    }

    private void verificarExecutado() {
        attacher.setRefreshComplete();
    }

    private AdapterView.OnItemLongClickListener configOnItemLongClickListener() {
        return (adapterView, view1, position, l) -> {
            selected = (TaxiParse) adapterView.getItemAtPosition(position);
            return false;
        };
    }

    private AdapterView.OnItemClickListener configOnItemClickListener() {
        return (adapterView, view1, position, l) -> {
            app.taxiSelecionado = (TaxiParse) adapterView.getAdapter().getItem(position);
            NavegacaoUtil.navegar(getActivity(), DetalheTaxiActivity.class);
        };
    }

    /**
     * Quando clicar no pin do mapa inicar uma nova tela
     * @return
     */
    private GoogleMap.OnInfoWindowClickListener configOnInfoWindowClickListener() {
        return marker -> {
            app.taxiSelecionado = (TaxiParse) app.mapMarker.get(marker);
            NavegacaoUtil.navegar(getActivity(), DetalheTaxiActivity.class);
        };
    }

    private FindCallback<ParseObject> configFindTaxiCallback() {
        return new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> result, ParseException error) {
                if(error == null){
                    app.listTaxi = result;
                    if(isAdded()){
                        update();
                    }
                }
                verificarStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    /**
     * Executado todas as vezes que sua posição no mapa for atualizada
     * @return
     */
    private Callback configAtualizarLocation(){
        return (error, args) -> {
            if(ultimaPosicao != null){
                double distancia = DistanciaUtil.calcularDistanciaEntreDoisPontos(app.location.latitude, app.location.longitude,
                        ultimaPosicao.latitude, ultimaPosicao.longitude);
                if(distancia > MIN_DISTANCIA){
                    verificarStatus(StatusEnum.INICIO);
                    mapaTabview.atualizarPosicaoMap(ultimaPosicao, MapaTabView.ZOOM);
                }
                if(!posicionarMap){
                    mapaTabview.atualizarPosicaoMap(ultimaPosicao, MapaTabView.ZOOM);
                    posicionarMap = true;
                }
            }
            ultimaPosicao = app.location;
            atualizarDistancia();
        };
    }

    /**
     * Atualizar a distância de cada item na lista
     */
    private void atualizarDistancia() {
        if(listTabView.uiHelper.listView.getAdapter() != null && ultimaPosicao != null){
            listTabView.uiHelper.listView.invalidateViews();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context_taxi, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_context_taxi_ligar:
                ligar();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void ligar() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + selected.getTelefone()));
        startActivity(callIntent);
    }

    @Override
    public void onRefreshStarted(View view) {
        verificarStatus(StatusEnum.INICIO);
    }

    private void update() {
        listTabView.update(app.listTaxi, app.location);
        mapaTabview.update(app.listTaxi);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        app.callback = null;
    }
}
