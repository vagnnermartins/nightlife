package br.com.nightlife.parse;

import com.parse.ParseClassName;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/**
 * Created by vagnnermartins on 25/10/14.
 */
@ParseClassName("_User")
public class UserParse extends ParseUser {

    private static final String MEUS_EVENTOS = "meusEventos";

    public ParseRelation<EventoParse> getMeusEventos(){
        return getRelation(MEUS_EVENTOS);
    }
}
