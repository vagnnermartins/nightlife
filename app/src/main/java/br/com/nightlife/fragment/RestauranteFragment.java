package br.com.nightlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

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
import br.com.nightlife.activity.DetalheBaladaActivity;
import br.com.nightlife.activity.DetalheRestauranteActivity;
import br.com.nightlife.activity.MainActivity;
import br.com.nightlife.adapter.BaladaAdapter;
import br.com.nightlife.app.App;
import br.com.nightlife.callback.Callback;
import br.com.nightlife.enums.StatusEnum;
import br.com.nightlife.parse.BaladaParse;
import br.com.nightlife.parse.RestauranteParse;
import br.com.nightlife.tabview.ListTabView;
import br.com.nightlife.tabview.MapaTabView;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * Created by vagnnermartins on 29/10/14 .
 */
public class RestauranteFragment extends Fragment implements PullToRefreshAttacher.OnRefreshListener {

    private static final double MIN_DISTANCIA = 5000;
    private App app;
    private View view;
    private MapaTabView mapaTabview;
    private ListTabView listTabView;
    private PullToRefreshAttacher attacher;
    private LinearLayout tabs;
    private LatLng ultimaPosicao;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        if(app.listRestaurantes == null){
            verificarStatus(StatusEnum.INICIO);
        }else{
            update();
        }
    }

    private void init() {
        getActivity().getActionBar().setTitle(R.string.fragment_restaurante);
        mapaTabview = new MapaTabView(this, getActivity().getLayoutInflater().inflate(R.layout.tabview_mapa, null));
        listTabView = new ListTabView(this, getActivity().getLayoutInflater().inflate(R.layout.tabview_list, null));
        app = (App) getActivity().getApplication();
        attacher = ((MainActivity)getActivity()).attacher;
        attacher.addRefreshableView(listTabView.uiHelper.listView, this);
        app.callback = configAtualizarLocation();
        listTabView.uiHelper.listView.setOnItemClickListener(configOnItemClickListener());
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
            RestauranteParse.buscarRestaurantes(point, configFindRestaurantesCallback());
            verificarStatus(StatusEnum.EXECUTANDO);
        }
    }

    private void verificarExecutando() {
        attacher.setRefreshing(true);
    }

    private void verificarExecutado() {
        attacher.setRefreshComplete();
    }

    private AdapterView.OnItemClickListener configOnItemClickListener() {
        return (adapterView, view1, position, l) -> {
            app.restauranteSelecionado = (RestauranteParse) adapterView.getAdapter().getItem(position);
            NavegacaoUtil.navegar(getActivity(), DetalheRestauranteActivity.class);
        };
    }

    private FindCallback<ParseObject> configFindRestaurantesCallback() {
        return new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> result, ParseException error) {
                if(error == null){
                    app.listRestaurantes = result;
                    if(isAdded()){
                        update();
                    }
                }
                verificarStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    private Callback configAtualizarLocation(){
        return (error, args) -> {
            if(ultimaPosicao != null){
                double distancia = DistanciaUtil.calcularDistanciaEntreDoisPontos(app.location.latitude, app.location.longitude,
                        ultimaPosicao.latitude, ultimaPosicao.longitude);
                if(distancia > MIN_DISTANCIA){
                    verificarStatus(StatusEnum.INICIO);
                }
            }
            ultimaPosicao = app.location;
            atualizarDistancia();
        };
    }

    private void atualizarDistancia() {
        if(listTabView.uiHelper.listView.getAdapter() != null && ultimaPosicao != null){
            listTabView.uiHelper.listView.invalidateViews();
        }
    }

    private void update() {
        listTabView.update(app.listRestaurantes, app.location);
        mapaTabview.update(app.listRestaurantes);
    }

    @Override
    public void onRefreshStarted(View view) {
        verificarStatus(StatusEnum.INICIO);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        app.callback = null;
    }
}
