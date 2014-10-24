package br.com.nightlife.app;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.nightlife.R;
import br.com.nightlife.constants.Keys;
import br.com.nightlife.util.ConnectionDetectorUtils;

/**
 * Classe responsável pela Aplicação
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        initParse();
    }

    private void initParse() {
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

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
