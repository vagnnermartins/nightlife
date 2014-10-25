package br.com.nightlife.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

import br.com.nightlife.R;
import br.com.nightlife.util.NavegacaoUtil;

public class SplashScreenActivity extends Activity {

    private static final long TIMER = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();
    }

    private void init() {
        new Handler().postDelayed(() -> {
            if(ParseUser.getCurrentUser() == null){
                NavegacaoUtil.navegar(this, LoginActivity.class);
            }else{
                NavegacaoUtil.navegar(this, MainActivity.class);
            }
        }, TIMER);
    }
}
