package br.com.nightlife.tabview;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.metasix.olhos_do_rio.componentebox.lib.tab.AbstractItemView;
import br.com.nightlife.R;
import br.com.nightlife.enums.StatusEnum;
import br.com.nightlife.fragment.BaladaFragment;
import br.com.nightlife.fragment.EventoFragment;
import br.com.nightlife.fragment.MeusEventosFragment;
import br.com.nightlife.fragment.RestauranteFragment;
import br.com.nightlife.fragment.TaxiFragment;

/**
 * Created by vagnnermartins on 27/10/14 .
 */
public class MapaTabView extends AbstractItemView {

    public static final float ZOOM_MAP = 16;

    private final Fragment fragment;
    private final View view;
    private SupportMapFragment mMapaFragment;
    private MapaTabViewUiHelper uiHelper;
    private GoogleMap map;
    private Map<Marker, ParseObject> mapMaker;

    public MapaTabView(Fragment fragment, View view) {
        this.fragment = fragment;
        this.view = view;
        init();
    }

    private void init() {
        mMapaFragment = SupportMapFragment.newInstance();
        fragment.getChildFragmentManager().beginTransaction().add(R.id.tabview_map, mMapaFragment).commit();
        uiHelper = new MapaTabViewUiHelper();
        uiHelper.btnUpdate.setOnClickListener(configurarOnUpdateClickListener());
    }

    public void initAfterStart() {
        map = mMapaFragment.getMap();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMyLocationEnabled(true);
    }

    private View.OnClickListener configurarOnUpdateClickListener() {
        return view1 -> {
            if(fragment instanceof BaladaFragment){
                ((BaladaFragment)fragment).verificarStatus(StatusEnum.INICIO);
            }else if(fragment instanceof EventoFragment){
                ((EventoFragment)fragment).verificarStatus(StatusEnum.INICIO);
            }else if(fragment instanceof MeusEventosFragment){
                ((MeusEventosFragment)fragment).verificarStatus(StatusEnum.INICIO);
            }else if(fragment instanceof TaxiFragment){
                ((TaxiFragment)fragment).verificarStatus(StatusEnum.INICIO);
            }else if(fragment instanceof RestauranteFragment){
                ((RestauranteFragment)fragment).verificarStatus(StatusEnum.INICIO);
            }
        };
    }

    public void update(List<ParseObject> itens) {
        map.clear();
        mapMaker = new HashMap<Marker, ParseObject>();
        Map<Integer, BitmapDescriptor> pins = new HashMap<Integer, BitmapDescriptor>();
        for (ParseObject item : itens) {
            adicionarPin(item);
        }
    }

    private void adicionarPin(ParseObject item) {
        ParseGeoPoint point = null;
        if(fragment instanceof EventoFragment){
            point = item.getParseObject("balada").getParseGeoPoint("localizacao");
        }else{
            point = item.getParseGeoPoint("localizacao");
        }
        MarkerOptions options = new MarkerOptions()
                .position(new LatLng(point.getLatitude(), point.getLongitude()))
                .title(item.getString("nome"));
        mapMaker.put(map.addMarker(options), item);
    }

    public void atualizarPosicaoMap(LatLng latLng, float zoom) {
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        map.animateCamera(center);
    }

    @Override
    public String getTabName() {
        return "";
    }

    @Override
    public Drawable getIcon() {
        return fragment.getResources().getDrawable(R.drawable.ic_tab_map);
    }

    @Override
    public View getView() {
        return view;
    }

    class MapaTabViewUiHelper{

        private final ImageButton btnUpdate;

        public MapaTabViewUiHelper(){
            btnUpdate = (ImageButton) view.findViewById(R.id.tabview_update);
        }
    }
}
