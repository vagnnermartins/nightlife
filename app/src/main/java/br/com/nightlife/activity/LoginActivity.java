package br.com.nightlife.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;

import br.com.metasix.olhos_do_rio.componentebox.lib.util.NavegacaoUtil;
import br.com.nightlife.R;
import br.com.nightlife.app.App;
import br.com.nightlife.enums.StatusEnum;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class LoginActivity extends Activity {

    private App app;
    private LoginUiHelper uiHelper;
    private PullToRefreshAttacher attacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        app = (App) getApplication();
        attacher = PullToRefreshAttacher.get(this);
        uiHelper = new LoginUiHelper();
        uiHelper.facebook.setOnClickListener(configOnFacebookClickListener());
    }

    private void verificarStatus(StatusEnum status){
        if(status == StatusEnum.INICIO){
            statusInicio();
        }else if(status == StatusEnum.EXECUTANDO){
            statusExecutando();
        }else if(status == StatusEnum.EXECUTADO){
            statusExecutado();
        }
    }

    private void statusInicio() {
        if(app.isInternetConnection(this, R.string.button_tentar_novamente, onTentarNovamenteClickListener(),
                R.string.button_sair, onSairClickListener())){
            ParseFacebookUtils.logIn(
                    Arrays.asList(ParseFacebookUtils.Permissions.User.ABOUT_ME, ParseFacebookUtils.Permissions.User.BIRTHDAY),
                    this, callBackLoginFacebook());
            verificarStatus(StatusEnum.EXECUTANDO);
        }
    }

    private void statusExecutando() {
        attacher.setRefreshing(true);
        uiHelper.progress.setVisibility(View.VISIBLE);
        uiHelper.facebook.setEnabled(false);
    }

    private void statusExecutado() {
        attacher.setRefreshComplete();
        uiHelper.progress.setVisibility(View.GONE);
        uiHelper.facebook.setEnabled(true);
    }

    private LogInCallback callBackLoginFacebook() {
        return new LogInCallback() {
            @Override
            public void done(ParseUser result, ParseException error) {
                if(error == null){
                    NavegacaoUtil.navegar(LoginActivity.this, MainActivity.class);
                    finish();
                }
            }
        };
    }

    private View.OnClickListener configOnFacebookClickListener() {
        return (v) -> {
            verificarStatus(StatusEnum.INICIO);
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    class LoginUiHelper{

        public View facebook;
        public View progress;

        public LoginUiHelper(){
            facebook = findViewById(R.id.login_facebook);
            progress = findViewById(R.id.login_facebook_progress);
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
