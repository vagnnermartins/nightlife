package br.com.nightlife.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import br.com.nightlife.activity.MainActivity;
import br.com.nightlife.adapter.BaladaAdapter;
import br.com.nightlife.app.App;
import br.com.nightlife.callback.Callback;
import br.com.nightlife.enums.StatusEnum;
import br.com.nightlife.parse.BaladaParse;
import br.com.nightlife.tabview.ListTabView;
import br.com.nightlife.tabview.MapaTabView;
import br.com.nightlife.util.DialogUtil;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class BaladaFragment extends Fragment implements PullToRefreshAttacher.OnRefreshListener {

    private static final double MIN_DISTANCIA = 5000;
    private App app;
    private View view;
    private MapaTabView mapaTabview;
    private ListTabView listTabView;
    private LinearLayout tabs;
    private PullToRefreshAttacher attacher;
    private LatLng ultimaPosicao;
    private boolean posicionarMap;

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
        mapaTabview.map.setOnMyLocationChangeListener(configOnMyLocationChangeListener());
        verificarAtualizar();
    }

    private void verificarAtualizar() {
        if(app.listBalada == null){
            verificarStatus(StatusEnum.INICIO);
        }else{
            update();
        }
    }

    private void init() {
        getActivity().getActionBar().setTitle(R.string.fragment_baladas);
        mapaTabview = new MapaTabView(this, getActivity().getLayoutInflater().inflate(R.layout.tabview_mapa, null), configOnInfoWindowClickListener());
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
        if(app.isInternetConnection(getActivity(), R.string.button_tentar_novamente, onTentarNovamenteClickListener(), R.string.button_sair, onSairClickListener()) &&
                app.isGPSEnable(getActivity(), R.string.button_tentar_novamente, onTentarNovamenteClickListener(), R.string.button_sair, onSairClickListener())){
            ParseGeoPoint point = null;
            if(app.location != null){
                point = new ParseGeoPoint(app.location.latitude, app.location.longitude);
            }
            BaladaParse.buscarBaladas(point, configFindBaladasCallback(), BaladaParse.STATUS_ATIVO);
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
            app.baladaSelecionada = (BaladaParse) adapterView.getAdapter().getItem(position);
            NavegacaoUtil.navegar(getActivity(), DetalheBaladaActivity.class);
        };
    }

    /**
     * Quando clicar no pin do mapa inicar uma nova tela
     * @return
     */
    private GoogleMap.OnInfoWindowClickListener configOnInfoWindowClickListener() {
        return marker -> {
            app.baladaSelecionada = (BaladaParse) app.mapMarker.get(marker);
            NavegacaoUtil.navegar(getActivity(), DetalheBaladaActivity.class);
        };
    }

    private FindCallback<ParseObject> configFindBaladasCallback() {
        return new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> result, ParseException error) {
                if(error == null){
                    app.listBalada = result;
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
                listTabView.uiHelper.listView.invalidateViews();
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

    private void update() {
        listTabView.update(app.listBalada, app.location);
        mapaTabview.update(app.listBalada);
    }

    @Override
    public void onRefreshStarted(View view) {
        verificarStatus(StatusEnum.INICIO);
    }

    private GoogleMap.OnMyLocationChangeListener configOnMyLocationChangeListener() {
        return location -> {
            if(location != null && !posicionarMap){
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mapaTabview.atualizarPosicaoMap(latLng, MapaTabView.ZOOM);
                posicionarMap = true;
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        app.callback = null;
    }

    private DialogInterface.OnClickListener onTentarNovamenteClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                verificarStatus(StatusEnum.INICIO);
            }
        };
    }

    private DialogInterface.OnClickListener onSairClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        };
    }
}
