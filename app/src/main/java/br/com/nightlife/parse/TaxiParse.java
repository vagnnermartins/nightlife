package br.com.nightlife.parse;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Taxi")
public class TaxiParse extends ParseObject {

    private static final String NOME = "nome";
    private static final String ENDERECO = "endereco";
    private static final String TELEFONE = "telefone";
    private static final String LOCALIZACAO = "localizacao";
    private static final String STATUS = "status";
    private static final double MAX_DISTANCE = 10;

    public static void getTaxiByLocation(ParseGeoPoint location, FindCallback<TaxiParse> callback){
        ParseQuery<TaxiParse> query = ParseQuery.getQuery(TaxiParse.class);
        if(location != null){
            query.whereWithinKilometers(LOCALIZACAO, location, MAX_DISTANCE);
        }
        query.whereEqualTo(STATUS, true);
        query.findInBackground(callback);
    }

    public String getNome(){
        return getString(NOME);
    }

    public String getEndereco(){
        return getString(ENDERECO);
    }

    public String getTelefone(){
        return getString(TELEFONE);
    }

    public ParseGeoPoint getLocalizacao(){
        return getParseGeoPoint(LOCALIZACAO);
    }
}
