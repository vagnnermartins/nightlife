package br.com.nightlife.parse;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("_User")
public class UserParse extends ParseUser {

    private static final String MEUS_EVENTOS = "meusEventos";
    private static final String BALADA = "balada";

    public void buscarMeusEventos(FindCallback<ParseObject> callback){
        ParseQuery<ParseObject> query = getMeusEventos().getQuery();
        query.include(BALADA);
        query.whereGreaterThanOrEqualTo("dataEvento", new Date());
        query.findInBackground(callback);
    }

    public ParseRelation<ParseObject> getMeusEventos(){
        return getRelation(MEUS_EVENTOS);
    }
}
