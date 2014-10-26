package br.com.nightlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import java.util.List;

import br.com.nightlife.R;
import br.com.nightlife.activity.DetalheBaladaActivity;
import br.com.nightlife.activity.MainActivity;
import br.com.nightlife.adapter.BaladaAdapter;
import br.com.nightlife.app.App;
import br.com.nightlife.callback.Callback;
import br.com.nightlife.enums.StatusEnum;
import br.com.nightlife.parse.BaladaParse;
import br.com.nightlife.parse.TaxiParse;
import br.com.nightlife.util.DistanciaUtil;
import br.com.nightlife.util.NavegacaoUtil;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * Created by vagnnermartins on 24/10/14 .
 */
public class BaladaFragment extends Fragment implements PullToRefreshAttacher.OnRefreshListener {

    private static final double MIN_DISTANCIA = 5000;

    private App app;
    private PullToRefreshAttacher attacher;
    private View view;
    private BaladaUiHelper uiHelper;
    private LatLng ultimaPosicao;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_balada, container, false);
        init();
        verificarAtualizar();
        return view;
    }

    private void verificarAtualizar() {
        if(app.listBalada == null){
            verificarStatus(StatusEnum.INICIO);
        }else{
            setList(app.listBalada);
        }
    }

    private void init() {
        getActivity().getActionBar().setTitle(R.string.fragment_baladas);
        app = (App) getActivity().getApplication();
        attacher = ((MainActivity)getActivity()).attacher;
        uiHelper = new BaladaUiHelper();
        attacher.addRefreshableView(uiHelper.listView, this);
        app.callback = configAtualizarLocation();
        uiHelper.listView.setOnItemClickListener(configOnItemClickListener());
    }

    private AdapterView.OnItemClickListener configOnItemClickListener() {
        return (adapterView, view1, position, l) -> {
            app.baladaSelecionada = (BaladaParse) adapterView.getAdapter().getItem(position);
            NavegacaoUtil.navegar(getActivity(), DetalheBaladaActivity.class);
        };
    }

    private void verificarStatus(StatusEnum status){
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
            BaladaParse.buscarBaladas(point, configFindBaladasCallback());
        }
    }

    private void verificarExecutando() {
        attacher.setRefreshing(true);
    }

    private void verificarExecutado() {
        attacher.setRefreshComplete();
    }

    private void setList(List<BaladaParse> result) {
        uiHelper.listView.setAdapter(new BaladaAdapter(getActivity(), R.layout.item_balada, result, ultimaPosicao));
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
        if(uiHelper.listView.getAdapter() != null && ultimaPosicao != null){
            for(int i = 0; i < uiHelper.listView.getAdapter().getCount(); i++){
                View current = uiHelper.listView.getChildAt(i);
                if(current != null){
                    BaladaAdapter.BaladaViewHolder viewHolder = (BaladaAdapter.BaladaViewHolder) current.getTag();
                    BaladaParse item = (BaladaParse) uiHelper.listView.getAdapter().getItem(i);
                    double distancia = DistanciaUtil.calcularDistanciaEntreDoisPontos(item.getLocalizacao().getLatitude(), item.getLocalizacao().getLongitude(),
                            ultimaPosicao.latitude, ultimaPosicao.longitude);
                    viewHolder.distancia.setText(DistanciaUtil.distanciaEmMetrosPorExtenso(distancia));
                }
            }
        }
    }

    private FindCallback<BaladaParse> configFindBaladasCallback() {
        return new FindCallback<BaladaParse>() {
            @Override
            public void done(List<BaladaParse> result, ParseException error) {
                if(error == null){
                    app.listBalada = result;
                    setList(app.listBalada);
                }
                verificarStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    @Override
    public void onRefreshStarted(View view) {
        verificarStatus(StatusEnum.INICIO);
    }

    class BaladaUiHelper{

        ListView listView;

        public BaladaUiHelper(){
            listView = (ListView) view.findViewById(R.id.balada_listview);
        }
    }
}
