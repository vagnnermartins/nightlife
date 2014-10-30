package br.com.nightlife.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.ParseException;
import java.util.List;

import br.com.metasix.olhos_do_rio.componentebox.lib.util.DataUtil;
import br.com.nightlife.R;
import br.com.nightlife.app.App;
import br.com.nightlife.enums.StatusEnum;
import br.com.nightlife.parse.GeneroParse;
import br.com.nightlife.parse.UserParse;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class DetalheEventoActivity extends FragmentActivity {

    private static final float MAP_ZOOM = 16;
    private App app;
    private DetalheEventoUiHelper uiHelper;
    private GoogleMap map;
    private SupportMapFragment mMapFragment;
    private PullToRefreshAttacher attacher;
    private boolean adicionou;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_evento);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initAfterStart();
    }

    private void init() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        app = (App) getApplication();
        attacher = PullToRefreshAttacher.get(this);
        getActionBar().setTitle(app.eventoSelecionado.getNome());
        uiHelper = new DetalheEventoUiHelper();
        uiHelper.telefone.setOnClickListener(configOnTelefoneClickListener());
        carregarValores();
        mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.detalhe_evento_map, mMapFragment).commit();
    }

    private void initAfterStart() {
        map = mMapFragment.getMap();
        map.setMyLocationEnabled(true);
        putPin();
    }

    private void verificarMeusEventos(StatusEnum status){
        if(status == StatusEnum.INICIO){
            verificarMeusEventos();
        }else if(status == StatusEnum.EXECUTANDO){
            verificarExecutando();
        }else if (status == StatusEnum.EXECUTADO){
            verificarExecutado();
        }
    }

    private void verificarMeusEventos() {
        if(app.isInternetConnection()){
            UserParse user = (UserParse) ParseUser.getCurrentUser();
            user.getMeusEventos().getQuery().findInBackground(configFindMeusEventosCallback());
            verificarMeusEventos(StatusEnum.EXECUTANDO);
        }
    }

    private void verificarExecutando() {
        attacher.setRefreshing(true);
    }

    private void verificarExecutado() {
        attacher.setRefreshComplete();
    }

    /**
     * Adiciona o pin do local do evento no mapa
     */
    private void putPin() {
        LatLng position = new LatLng(app.eventoSelecionado.getBalada().getLocalizacao().getLatitude(),
                app.eventoSelecionado.getBalada().getLocalizacao().getLongitude());
        map.addMarker(new MarkerOptions()
                .position(position)
                .title(app.eventoSelecionado.getNome())).showInfoWindow();;
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM);
        map.animateCamera(center);
    }

    private void carregarValores() {
        try {
            uiHelper.data.setText(DataUtil.obterDataPorExetenso(app.eventoSelecionado.getData()));
            uiHelper.hora.setText(DataUtil.transformDateToSting(app.eventoSelecionado.getData(), "HH:mm"));
            uiHelper.descricao.setText(app.eventoSelecionado.getDescricao());
            uiHelper.endereco.setText(app.eventoSelecionado.getBalada().getEnderecoFormatado());
            uiHelper.telefone.setText(app.eventoSelecionado.getBalada().getTelefone());
            verificarGeneros();
            verificarMeusEventos(StatusEnum.INICIO);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Buscar os generos deste evento
     */
    private void verificarGeneros() {
        app.eventoSelecionado.getGenero().getQuery().findInBackground(configFindGenerosCallback());
    }

    private FindCallback<GeneroParse> configFindGenerosCallback() {
        return new FindCallback<GeneroParse>() {
            @Override
            public void done(List<GeneroParse> result, com.parse.ParseException error) {
                if(error == null){
                    String genero = "";
                    for(GeneroParse item : result){
                        genero += item.getGenero() + "; ";
                    }
                    uiHelper.genero.setText(genero);
                }
            }
        };
    }

    private View.OnClickListener configOnTelefoneClickListener() {
        return (v) -> confirmacaoLigar();
    }

    /**
     * Confirmação para realizar a ligação
     */
    private void confirmacaoLigar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.detalhe_evento_confirmacao);
        builder.setMessage(R.string.detalhe_evento_confirmacao_mensagem);
        builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> ligar());
        builder.setNegativeButton(android.R.string.no, null);
        builder.create().show();
    }

    private void ligar() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + app.eventoSelecionado.getBalada().getTelefone()));
        startActivity(callIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_detalhe_evento_participar:
                verificarEvento();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Se o evento já está em meus eventos remove senão adiciona
     */
    private void verificarEvento() {
        UserParse user = (UserParse) ParseUser.getCurrentUser();
        if(app.meusEventos.contains(app.eventoSelecionado)){
            app.meusEventos.remove(app.eventoSelecionado);
            user.getMeusEventos().remove(app.eventoSelecionado);
        }else{
            app.meusEventos.add(app.eventoSelecionado);
            user.getMeusEventos().add(app.eventoSelecionado);
            adicionou = true;
        }
        user.saveInBackground(configSaveCallback());
    }

    private SaveCallback configSaveCallback() {
        return new SaveCallback() {
            @Override
            public void done(com.parse.ParseException error) {
                if(error == null){
                    if(adicionou){
                        exibirMensagem(R.string.detalhe_evento_evento_adicionado);
                    }else{
                        exibirMensagem(R.string.detalhe_evento_evento_removido);
                    }
                    verificarMenu();
                }
            }
        };
    }

    private void exibirMensagem(int message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private FindCallback<ParseObject> configFindMeusEventosCallback() {
        return new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> result, com.parse.ParseException error) {
                if(error == null){
                    app.meusEventos = result;
                }
                verificarMeusEventos(StatusEnum.EXECUTADO);
                verificarMenu();
            }

        };
    }

    private void verificarMenu() {
        if(menu != null && app.meusEventos != null ){
            if(app.meusEventos.contains(app.eventoSelecionado)){
                menu.findItem(R.id.menu_detalhe_evento_participar).setTitle(R.string.fragment_evento_desistir);
            }else{
                menu.findItem(R.id.menu_detalhe_evento_participar).setTitle(R.string.fragment_evento_participar);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhe_evento, menu);
        this.menu = menu;
        verificarMenu();
        return super.onCreateOptionsMenu(menu);
    }

    class DetalheEventoUiHelper{
        TextView data;
        TextView hora;
        TextView genero;
        TextView descricao;
        TextView endereco;
        TextView telefone;

        public DetalheEventoUiHelper(){
            this.data = (TextView) findViewById(R.id.detalhe_evento_data);
            this.hora = (TextView) findViewById(R.id.detalhe_evento_hora);
            this.genero = (TextView) findViewById(R.id.detalhe_evento_genero);
            this.descricao = (TextView) findViewById(R.id.detalhe_evento_descricao);
            this.endereco = (TextView) findViewById(R.id.detalhe_evento_endereco);
            this.telefone = (TextView) findViewById(R.id.detalhe_evento_telefone);
        }
    }

}
