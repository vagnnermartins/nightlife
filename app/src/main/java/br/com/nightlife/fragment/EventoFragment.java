package br.com.nightlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import br.com.nightlife.R;
import br.com.nightlife.activity.DetalheEventoActivity;
import br.com.nightlife.activity.MainActivity;
import br.com.nightlife.adapter.EventoAdapter;
import br.com.nightlife.app.App;
import br.com.nightlife.enums.StatusEnum;
import br.com.nightlife.parse.EventoParse;
import br.com.nightlife.util.NavegacaoUtil;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * Created by vagnnermartins on 25/10/14.
 */
public class EventoFragment extends Fragment implements PullToRefreshAttacher.OnRefreshListener {

    private View view;
    private PullToRefreshAttacher attacher;
    private App app;
    private EventoUiHelper uiHelper;
    private EventoParse selected;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_evento, container, false);
        init();
        verificarAtualizar();
        return view;
    }

    private void verificarAtualizar() {
        if(app.listEvento == null){
            verificarStatus(StatusEnum.INICIO);
        }else{
            setList(app.listEvento);
        }
    }

    private void init() {
        getActivity().getActionBar().setTitle(R.string.fragment_evento);
        attacher = ((MainActivity) getActivity()).attacher;
        app = (App) getActivity().getApplication();
        uiHelper = new EventoUiHelper();
        uiHelper.listView.setOnItemLongClickListener(configOnItemLongClickListener());
        uiHelper.listView.setOnItemClickListener(configOnItemClickListener());
        attacher.addRefreshableView(uiHelper.listView, this);
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

    private void setList(List<EventoParse> result) {
        uiHelper.listView.setAdapter(new EventoAdapter(getActivity(), R.layout.item_evento, result));
    }

    private FindCallback<EventoParse> configBuscarProximosEventos() {
        return new FindCallback<EventoParse>() {
            @Override
            public void done(List<EventoParse> result, ParseException error) {
                if(error == null){
                    app.listEvento = result;
                    setList(result);
                }
                verificarStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    private AdapterView.OnItemLongClickListener configOnItemLongClickListener() {
        return (adapterView, view1, position, l) -> {
            selected = (EventoParse) adapterView.getItemAtPosition(position);
            return false;
        };
    }

    private AdapterView.OnItemClickListener configOnItemClickListener() {
        return (adapterView, view1, position, l) -> {
            app.eventoSelecionado = (EventoParse) adapterView.getAdapter().getItem(position);
            NavegacaoUtil.navegar(getActivity(), DetalheEventoActivity.class);
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefreshStarted(View view) {
        verificarStatus(StatusEnum.INICIO);
    }

    class EventoUiHelper{

        ListView listView;

        public EventoUiHelper(){
            listView = (ListView) view.findViewById(R.id.evento_listview);
        }
    }
}
