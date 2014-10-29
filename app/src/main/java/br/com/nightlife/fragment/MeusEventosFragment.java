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
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

import br.com.metasix.olhos_do_rio.componentebox.lib.util.NavegacaoUtil;
import br.com.nightlife.R;
import br.com.nightlife.activity.DetalheEventoActivity;
import br.com.nightlife.activity.MainActivity;
import br.com.nightlife.adapter.EventoAdapter;
import br.com.nightlife.app.App;
import br.com.nightlife.enums.StatusEnum;
import br.com.nightlife.parse.EventoParse;
import br.com.nightlife.parse.UserParse;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class MeusEventosFragment extends Fragment implements PullToRefreshAttacher.OnRefreshListener {

    private App app;
    private View view;
    private PullToRefreshAttacher attacher;
    private MeusEventosUHelper uiHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_meus_eventos, container, false);
        init();
        verificarAtualizar();
        return view;
    }

    private void verificarAtualizar() {
        if(app.meusEventos == null){
            verificarStatus(StatusEnum.INICIO);
        }else{
            setList(app.meusEventos);
        }
    }

    private void init() {
        getActivity().getActionBar().setTitle(R.string.fragment_evento);
        attacher = ((MainActivity) getActivity()).attacher;
        app = (App) getActivity().getApplication();
        uiHelper = new MeusEventosUHelper();
        uiHelper.listView.setOnItemClickListener(configOnItemClickListener());
        attacher.addRefreshableView(uiHelper.listView, this);
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
            UserParse user = (UserParse) ParseUser.getCurrentUser();
            user.buscarMeusEventos(configFindMeusEventosCallback());
            verificarStatus(StatusEnum.EXECUTANDO);
        }
    }

    private void verificarExecutando() {
        attacher.setRefreshing(true);
    }

    private void verificarExecutado() {
        attacher.setRefreshComplete();
    }

    private void setList(List<ParseObject> result) {
        uiHelper.listView.setAdapter(new EventoAdapter(getActivity(), R.layout.item_evento, result));
    }

    private AdapterView.OnItemClickListener configOnItemClickListener() {
        return (adapterView, view1, position, l) -> {
            app.eventoSelecionado = (EventoParse) adapterView.getAdapter().getItem(position);
            NavegacaoUtil.navegar(getActivity(), DetalheEventoActivity.class);
        };
    }

    private FindCallback<ParseObject> configFindMeusEventosCallback() {
        return new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> result, ParseException error) {
                if(error == null){
                    app.meusEventos = result;
                    if(isAdded()){
                        setList(result);
                    }
                }
                verificarStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    @Override
    public void onRefreshStarted(View view) {
        verificarStatus(StatusEnum.INICIO);
    }

    class MeusEventosUHelper{

        public ListView listView;

        public MeusEventosUHelper(){
            listView = (ListView) view.findViewById(R.id.evento_listview);
        }
    }
}
