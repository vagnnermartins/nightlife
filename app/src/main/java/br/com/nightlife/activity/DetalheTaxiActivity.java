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

public class DetalheTaxiActivity extends FragmentActivity {

    private static final float MAP_ZOOM = 16;

    private App app;
    private DetalheTaxiUiHelper uiHelper;
    private GoogleMap map;
    private SupportMapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_taxi);
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
        getActionBar().setTitle(app.taxiSelecionado.getNome());
        uiHelper = new DetalheTaxiUiHelper();
        uiHelper.telefone.setOnClickListener(configOnTelefoneClickListener());
        carregarValores();
        mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.detalhe_taxi_map, mMapFragment).commit();
    }

    private void carregarValores() {
        uiHelper.telefone.setText(app.taxiSelecionado.getTelefone());
        uiHelper.endereco.setText(app.taxiSelecionado.getEndereco());
    }

    private void putPin() {
        LatLng position = new LatLng(app.taxiSelecionado.getLocalizacao().getLatitude(),
                app.taxiSelecionado.getLocalizacao().getLongitude());
        map.addMarker(new MarkerOptions()
                .position(position)
                .title(app.taxiSelecionado.getNome()));
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM);
        map.animateCamera(center);
    }

    private View.OnClickListener configOnTelefoneClickListener() {
        return (v) -> confirmacaoLigar();
    }

    private void ligar() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + app.taxiSelecionado.getTelefone()));
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

    class DetalheTaxiUiHelper{

        TextView telefone;
        TextView endereco;

        public DetalheTaxiUiHelper(){
            this.telefone = (TextView) findViewById(R.id.detalhe_taxi_telefone);
            this.endereco = (TextView) findViewById(R.id.detalhe_taxi_endereco);
        }
    }
}
