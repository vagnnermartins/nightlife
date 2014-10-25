package br.com.nightlife.callback;

import java.util.Objects;


public interface Callback {

    public void onReturn(Exception error, Object... args);
}
