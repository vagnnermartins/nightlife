package br.com.nightlife.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

import br.com.metasix.olhos_do_rio.componentebox.lib.util.NavegacaoUtil;
import br.com.nightlife.R;
import br.com.nightlife.adapter.EventoAdapter;
import br.com.nightlife.app.App;
import br.com.nightlife.enums.StatusEnum;
import br.com.nightlife.parse.EventoParse;
import br.com.nightlife.parse.UserParse;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class EventoBaladaActivity extends Activity implements PullToRefreshAttacher.OnRefreshListener {

    private App app;
    private PullToRefreshAttacher attacher;
    private EventoBaladaUHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_meus_eventos);
        init();
        verificarAtualizar();
    }

    private void verificarAtualizar() {
        List<ParseObject> eventos = app.mapEventoBalada.get(app.baladaSelecionada.getObjectId());
        if(eventos == null){
            verificarStatus(StatusEnum.INICIO);
        }else{
            setList(eventos);
        }
    }

    private void init() {
        app = (App) getApplication();
        getActionBar().setTitle(app.baladaSelecionada.getNome());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        attacher = PullToRefreshAttacher.get(this);
        uiHelper = new EventoBaladaUHelper();
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
        if(app.isInternetConnection(this, R.string.button_tentar_novamente, onTentarNovamenteClickListener(),
                R.string.button_sair, onSairClickListener())){
            EventoParse.buscarEventosPorBalada(app.baladaSelecionada, configBuscarEventosPorBaladaCallback());
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
        uiHelper.listView.setAdapter(new EventoAdapter(this, R.layout.item_evento, result));
    }

    private AdapterView.OnItemClickListener configOnItemClickListener() {
        return (adapterView, view1, position, l) -> {
            app.eventoSelecionado = (EventoParse) adapterView.getAdapter().getItem(position);
            NavegacaoUtil.navegar(this, DetalheEventoActivity.class);
        };
    }

    private FindCallback<ParseObject> configBuscarEventosPorBaladaCallback() {
        return new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> result, ParseException error) {
                if(error == null){
                    app.mapEventoBalada.put(app.baladaSelecionada.getObjectId(), result);
                    setList(result);
                    if(result.isEmpty()){
                        uiHelper.message.setVisibility(View.VISIBLE);
                    }else{
                        uiHelper.message.setVisibility(View.GONE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.out_cima_para_baixo, R.anim.in_cima_para_baixo);
    }

    class EventoBaladaUHelper{
        public ListView listView;
        public View message;

        public EventoBaladaUHelper(){
            listView = (ListView) findViewById(R.id.evento_listview);
            message = findViewById(R.id.eventos_message_main);
        }
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
