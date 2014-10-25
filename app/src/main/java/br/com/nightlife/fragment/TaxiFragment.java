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
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import java.util.List;

import br.com.nightlife.R;
import br.com.nightlife.activity.MainActivity;
import br.com.nightlife.adapter.TaxiAdapter;
import br.com.nightlife.app.App;
import br.com.nightlife.callback.Callback;
import br.com.nightlife.enums.StatusEnum;
import br.com.nightlife.parse.TaxiParse;
import br.com.nightlife.util.DistanciaUtil;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class TaxiFragment extends Fragment {

    private static final double MIN_DISTANCIA = 5000;

    private App app;
    private View view;
    private TaxiUiHelper uiHelper;
    private PullToRefreshAttacher attacher;
    private LatLng ultimaPosicao;
    private TaxiParse selected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_taxi, container, false);
        init();
        verificarAtualizar();
        return view;
    }

    private void init() {
        getActivity().getActionBar().setTitle(R.string.fragment_taxi);
        attacher = ((MainActivity) getActivity()).attacher;
        app = (App) getActivity().getApplication();
        app.callback = configAtualizarLocation();
        uiHelper = new TaxiUiHelper();
        uiHelper.listView.setOnItemLongClickListener(configOnItemLongClickListener());
        registerForContextMenu(uiHelper.listView);
    }

    /**
     * Método responsável para verificar se já existe lista de ic_menu_taxi em memória
     */
    private void verificarAtualizar() {
        if(app.listTaxi == null){
            verificarStatus(StatusEnum.INICIO);
        }else{
            setList(app.listTaxi);
        }
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

    private FindCallback<TaxiParse> configFindTaxiCallback() {
        return new FindCallback<TaxiParse>() {
            @Override
            public void done(List<TaxiParse> result, ParseException error) {
                if(error == null){
                    setList(result);
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
                TaxiAdapter.TaxiViewHolder viewHolder = (TaxiAdapter.TaxiViewHolder) current.getTag();
                TaxiParse item = (TaxiParse) uiHelper.listView.getAdapter().getItem(i);
                double distancia = DistanciaUtil.calcularDistanciaEntreDoisPontos(item.getLocalizacao().getLatitude(), item.getLocalizacao().getLongitude(),
                        ultimaPosicao.latitude, ultimaPosicao.longitude);
                viewHolder.distancia.setText(DistanciaUtil.distanciaEmMetrosPorExtenso(distancia));
            }
        }
    }

    /**
     * Respinsável por criar a lista de taxis
     * @param result
     */
    private void setList(List<TaxiParse> result){
        uiHelper.listView.setAdapter(new TaxiAdapter(getActivity(), R.layout.item_taxi, result, app.location));
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
            case R.id.menu_context_taxi_ver_no_mapa:
                break;
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

    private AdapterView.OnItemLongClickListener configOnItemLongClickListener() {
        return (adapterView, view1, position, l) -> {
            selected = (TaxiParse) adapterView.getItemAtPosition(position);
            return false;
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        app.callback = null;
    }

    class TaxiUiHelper{

        ListView listView;

        public TaxiUiHelper(){
            listView = (ListView) view.findViewById(R.id.taxi_listview);
        }
    }
}
