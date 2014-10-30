package br.com.nightlife.parse;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.Date;

@ParseClassName("Evento")
public class EventoParse extends ParseObject {

    private static final String NOME = "nome";
    private static final String DESCRICAO = "descricao";
    private static final String DATA_EVENTO = "dataEvento";
    private static final String BALADA = "balada";
    private static final String GENERO = "genero";

    public static void buscarProximosEventos(FindCallback<ParseObject> callback){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Evento");
        query.orderByAscending(DATA_EVENTO);
        query.include(BALADA);
        query.findInBackground(callback);
    }

    public String getNome(){
        return getString(NOME);
    }

    public String getDescricao(){
        return getString(DESCRICAO);
    }

    public Date getData(){
        return getDate(DATA_EVENTO);
    }

    public BaladaParse getBalada(){
        return (BaladaParse) getParseObject(BALADA);
    }

    public ParseRelation<GeneroParse> getGenero(){
        return getRelation(GENERO);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventoParse that = (EventoParse) o;

        if (getObjectId() != null ? !getObjectId().equals(that.getObjectId()) : that.getObjectId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getObjectId() != null ? getObjectId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
