package br.com.nightlife.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.nightlife.R;
import br.com.nightlife.app.App;

public class DetalheRestauranteActivity extends FragmentActivity {

    private static final float MAP_ZOOM = 16;

    private App app;
    private DetalheRestauranteUiHelper uiHelper;
    private GoogleMap map;
    private SupportMapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_restaurante);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initAfterStart();
    }

    private void initAfterStart() {
        map = mMapFragment.getMap();
        map.setMyLocationEnabled(true);
        putPin();
    }

    private void init() {
        app = (App) getApplication();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle(app.restauranteSelecionado.getNome());
        uiHelper = new DetalheRestauranteUiHelper();
        uiHelper.telefone.setOnClickListener(configOnTelefoneClickListener());
        uiHelper.website.setOnClickListener(configOnWebsiteClickListener());
        carregarValores();
        mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.detalhe_restaurante_map, mMapFragment).commit();
    }

    private void carregarValores() {
        checkValue(uiHelper.horarioMain, uiHelper.horario, app.restauranteSelecionado.getHorario());
        checkValue(uiHelper.telefoneMain, uiHelper.telefone, app.restauranteSelecionado.getTelefone());
        checkValue(uiHelper.websiteMain, uiHelper.website, app.restauranteSelecionado.getSite());
        checkValue(uiHelper.tipoComidaMain, uiHelper.tipoComida, app.restauranteSelecionado.getTipoComida());
        checkValue(uiHelper.enderecoMain, uiHelper.endereco, app.restauranteSelecionado.getEndereco());
        checkValue(uiHelper.descricaoMain, uiHelper.descricao, app.restauranteSelecionado.getDescricao());
    }

    private void checkValue(View main, TextView textView, String text){
        if(text != null && !text.equals("")){
            textView.setText(text);
        }else{
            main.setVisibility(View.GONE);
        }
    }

    private void putPin() {
        LatLng position = new LatLng(app.restauranteSelecionado.getLocalizacao().getLatitude(),
                app.restauranteSelecionado.getLocalizacao().getLongitude());
        map.addMarker(new MarkerOptions()
                .position(position)
                .title(app.restauranteSelecionado.getNome())).showInfoWindow();;
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM);
        map.animateCamera(center);
    }

    private View.OnClickListener configOnTelefoneClickListener() {
        return (v) -> confirmacaoLigar();
    }

    private View.OnClickListener configOnWebsiteClickListener() {
        return view -> {
            Intent viewIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(app.restauranteSelecionado.getSite()));
            startActivity(viewIntent);
        };
    }

    private void ligar() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + app.restauranteSelecionado.getTelefone()));
        startActivity(callIntent);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class DetalheRestauranteUiHelper{

        View horarioMain;
        TextView horario;
        View telefoneMain;
        TextView telefone;
        View websiteMain;
        TextView website;
        View tipoComidaMain;
        TextView tipoComida;
        View descricaoMain;
        TextView descricao;
        View enderecoMain;
        TextView endereco;

        public DetalheRestauranteUiHelper(){
            horario = (TextView) findViewById(R.id.detalhe_restaurante_funcionamento);
            horarioMain = findViewById(R.id.detalhe_restaurante_funcionamento_main);
            telefone = (TextView) findViewById(R.id.detalhe_restaurante_telefone);
            telefoneMain = findViewById(R.id.detalhe_restaurante_telefone_main);
            website = (TextView) findViewById(R.id.detalhe_restaurante_website);
            websiteMain = findViewById(R.id.detalhe_restaurante_website_main);
            tipoComida = (TextView) findViewById(R.id.detalhe_restaurante_tipo);
            tipoComidaMain = findViewById(R.id.detalhe_restaurante_tipo_main);
            descricaoMain = findViewById(R.id.detalhe_restaurante_descricao_main);
            descricao = (TextView) findViewById(R.id.detalhe_restaurante_descricao);
            endereco = (TextView) findViewById(R.id.detalhe_restaurante_endereco);
            enderecoMain = findViewById(R.id.detalhe_restaurante_endereco_main);
        }
    }

}
