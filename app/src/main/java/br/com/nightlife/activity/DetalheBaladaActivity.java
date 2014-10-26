package br.com.nightlife.activity;

import android.app.Activity;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.nightlife.R;
import br.com.nightlife.app.App;

public class DetalheBaladaActivity extends FragmentActivity {

    private static final float MAP_ZOOM = 16;

    private App app;
    private DetalheBaladaUiHelper uiHelper;
    private GoogleMap map;
    private SupportMapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_balada);
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
        getActionBar().setTitle(app.baladaSelecionada.getNome());
        uiHelper = new DetalheBaladaUiHelper();
        uiHelper.telefone.setOnClickListener(configOnTelefoneClickListener());
        uiHelper.website.setOnClickListener(configOnWebsiteClickListener());
        carregarValores();
        mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.detalhe_balada_map, mMapFragment).commit();
    }

    private View.OnClickListener configOnWebsiteClickListener() {
        return view -> {
            Intent viewIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(app.baladaSelecionada.getWebSite()));
            startActivity(viewIntent);
        };
    }

    private void carregarValores() {
        uiHelper.website.setText(app.baladaSelecionada.getWebSite());
        uiHelper.telefone.setText(app.baladaSelecionada.getTelefone());
        uiHelper.endereco.setText(app.baladaSelecionada.getEnderecoFormatado());
        uiHelper.funcionamento.setText(app.baladaSelecionada.getHoraFuncionamento());
        if(app.baladaSelecionada.isPacoteAniversario()){
            uiHelper.mainAniversario.setVisibility(View.VISIBLE);
            uiHelper.aniversario.setText(app.baladaSelecionada.getPacoteAniversario());
        }else{
            uiHelper.mainAniversario.setVisibility(View.GONE);
        }
    }

    private void putPin() {
        LatLng position = new LatLng(app.baladaSelecionada.getLocalizacao().getLatitude(),
                app.baladaSelecionada.getLocalizacao().getLongitude());
        map.addMarker(new MarkerOptions()
                .position(position)
                .title(app.baladaSelecionada.getNome()));
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM);
        map.animateCamera(center);
    }

    private View.OnClickListener configOnTelefoneClickListener() {
        return (v) -> confirmacaoLigar();
    }

    private void ligar() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + app.baladaSelecionada.getTelefone()));
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

    class DetalheBaladaUiHelper{

        TextView funcionamento;
        TextView telefone;
        TextView website;
        TextView aniversario;
        View mainAniversario;
        TextView endereco;

        public DetalheBaladaUiHelper(){
            this.funcionamento = (TextView) findViewById(R.id.detalhe_balada_funcionamento);
            this.telefone = (TextView) findViewById(R.id.detalhe_balada_telefone);
            this.website = (TextView) findViewById(R.id.detalhe_balada_website);
            this.aniversario = (TextView) findViewById(R.id.detalhe_balada_aniversario);
            this.mainAniversario = findViewById(R.id.detalhe_balada_aniversario_main);
            this.endereco = (TextView) findViewById(R.id.detalhe_balada_endereco);
        }
    }
}
