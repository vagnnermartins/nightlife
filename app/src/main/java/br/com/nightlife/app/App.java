package br.com.nightlife.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import br.com.nightlife.R;
import br.com.nightlife.callback.Callback;
import br.com.nightlife.constants.Keys;
import br.com.nightlife.parse.BaladaParse;
import br.com.nightlife.parse.EventoParse;
import br.com.nightlife.parse.GeneroParse;
import br.com.nightlife.parse.TaxiParse;
import br.com.nightlife.parse.UserParse;
import br.com.nightlife.util.ConnectionDetectorUtils;
import br.com.nightlife.util.GPSTrackerUtils;

/**
 * Classe responsável pela Aplicação
 */
public class App extends Application {

    private static final long TEMPO_ATUALIZAR = 60000;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public Callback callback;
    public LatLng location;

    public List<TaxiParse> listTaxi;
    public List<EventoParse> listEvento;
    public List<EventoParse> meusEventos;
    public EventoParse eventoSelecionado;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        initParse();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = configLocationListener();
        updateLocation();
    }

    private void initParse() {
        ParseObject.registerSubclass(TaxiParse.class);
        ParseObject.registerSubclass(EventoParse.class);
        ParseObject.registerSubclass(BaladaParse.class);
        ParseObject.registerSubclass(GeneroParse.class);
        ParseObject.registerSubclass(UserParse.class);
        Parse.initialize(this, Keys.PARSE_APP_ID, Keys.PARSE_CLIENT_KEY);
        ParseFacebookUtils.initialize(Keys.FACEBOOK_APP_ID);
    }

    public boolean isInternetConnection(){
        ConnectionDetectorUtils cd = new ConnectionDetectorUtils(this);
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(this, R.string.exception_erro_err_internet_disconnected, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean isGPSEnable(Activity activity){
        GPSTrackerUtils gps = new GPSTrackerUtils(activity);

        if (gps.canGetLocation()) {
            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
        } else {
            Toast.makeText(this, R.string.exception_erro_err_gps, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    /**
     * Responsável por atualizar a localização do usuário
     */
    private void updateLocation(){
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TEMPO_ATUALIZAR, 0, locationListener);
    }

    private LocationListener configLocationListener() {
        return new LocationListener() {
            public void onLocationChanged(Location l) {
                location = new LatLng(l.getLatitude(), l.getLongitude());
                if(callback != null){
                    callback.onReturn(null, location);
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        locationManager.removeUpdates(locationListener);
    }
}
