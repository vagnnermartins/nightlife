package br.com.nightlife.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.parse.ParseUser;

import br.com.metasix.olhos_do_rio.componentebox.lib.util.NavegacaoUtil;
import br.com.nightlife.R;

public class SplashScreenActivity extends Activity {

    private static final long TIMER = 500;

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
            finish();
        }, TIMER);
    }
}
