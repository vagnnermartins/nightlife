package br.com.nightlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import br.com.metasix.olhos_do_rio.componentebox.lib.tab.AbstractItemView;
import br.com.metasix.olhos_do_rio.componentebox.lib.tab.TabBar;
import br.com.metasix.olhos_do_rio.componentebox.lib.util.NavegacaoUtil;
import br.com.nightlife.R;
import br.com.nightlife.activity.DetalheBaladaActivity;
import br.com.nightlife.activity.DetalheEventoActivity;
import br.com.nightlife.activity.MainActivity;
import br.com.nightlife.app.App;
import br.com.nightlife.enums.StatusEnum;
import br.com.nightlife.parse.BaladaParse;
import br.com.nightlife.parse.EventoParse;
import br.com.nightlife.tabview.ListTabView;
import br.com.nightlife.tabview.MapaTabView;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class EventoFragment extends Fragment implements PullToRefreshAttacher.OnRefreshListener {

    private App app;
    private PullToRefreshAttacher attacher;
    private View view;
    private MapaTabView mapaTabview;
    private ListTabView listTabView;
    private LinearLayout tabs;

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
        if(app.listEvento == null){
            verificarStatus(StatusEnum.INICIO);
        }else{
            update();
        }
    }

    private void init() {
        getActivity().getActionBar().setTitle(R.string.fragment_evento);
        attacher = ((MainActivity) getActivity()).attacher;
        app = (App) getActivity().getApplication();
        mapaTabview = new MapaTabView(this, getActivity().getLayoutInflater().inflate(R.layout.tabview_mapa, null),configOnInfoWindowClickListener());
        listTabView = new ListTabView(this, getActivity().getLayoutInflater().inflate(R.layout.tabview_list, null));
        listTabView.uiHelper.listView.setOnItemClickListener(configOnItemClickListener());
        attacher.addRefreshableView(listTabView.uiHelper.listView, this);
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
            EventoParse.buscarProximosEventos(configBuscarProximosEventos());
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
            app.eventoSelecionado = (EventoParse) adapterView.getAdapter().getItem(position);
            NavegacaoUtil.navegar(getActivity(), DetalheEventoActivity.class);
        };
    }

    /**
     * Quando clicar no pin do mapa inicar uma nova tela
     * @return
     */
    private GoogleMap.OnInfoWindowClickListener configOnInfoWindowClickListener() {
        return marker -> {
            app.eventoSelecionado = (EventoParse) app.mapMarker.get(marker);
            NavegacaoUtil.navegar(getActivity(), DetalheEventoActivity.class);
        };
    }

    private FindCallback<ParseObject> configBuscarProximosEventos() {
        return new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> result, ParseException error) {
                if(error == null){
                    app.listEvento = result;
                    if(isAdded()){
                        update();
                    }
                }
                verificarStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    private void update() {
        listTabView.update(app.listEvento);
        mapaTabview.update(app.listEvento);
    }

    @Override
    public void onRefreshStarted(View view) {
        verificarStatus(StatusEnum.INICIO);
    }
}
