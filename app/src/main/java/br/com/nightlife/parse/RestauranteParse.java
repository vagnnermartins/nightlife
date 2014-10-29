package br.com.nightlife.parse;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by vagnnermartins on 29/10/14 .
 */
@ParseClassName("Restaurante")
public class RestauranteParse extends ParseObject {

    private static final String NOME = "nome";
    private static final String BAIRRO = "bairro";
    private static final String CEP = "cep";
    private static final String CIDADE = "cidade";
    private static final String DESCRICAO = "descricaoPt";
    private static final String EMAIL = "email";
    private static final String ENDERECO = "enderecoOriginal";
    private static final String ESTADO = "estado";
    private static final String HORARIO = "horarioPortugues";
    private static final String LOCALIZACAO = "localizacao";
    private static final String NUMERO = "numero";
    private static final String SITE = "site";
    private static final String TELEFONE = "telefone";
    private static final String TIPO_COMIDA = "tipoComidaPt";
    private static final double MAX_DISTANCE = 100;

    public static void buscarRestaurantes(ParseGeoPoint location, FindCallback<ParseObject> callback){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Restaurante");
        if(location != null){
            query.whereWithinKilometers(LOCALIZACAO, location, MAX_DISTANCE);
            query.whereNear(LOCALIZACAO, location);
        }
        query.findInBackground(callback);
    }

    public String getNome() {
        return getString(NOME);
    }

    public String getBairro(){
        return getString(BAIRRO);
    }

    public String getCep(){
        return getString(CEP);
    }

    public String getCidade(){
        return getString(CIDADE);
    }

    public String getDescricao(){
        return getString(DESCRICAO);
    }

    public String getEmail(){
        return getString(EMAIL);
    }

    public String getEndereco(){
        return getString(ENDERECO);
    }

    public String getEstado(){
        return getString(ESTADO);
    }

    public String getHorario(){
        return getString(HORARIO);
    }

    public String getNumero(){
        return getString(NUMERO);
    }

    public String getSite(){
        return getString(SITE);
    }

    public String getTelefone(){
        return getString(TELEFONE);
    }
    public String getTipoComida(){
        return getString(TIPO_COMIDA);
    }

    public ParseGeoPoint getLocalizacao(){
        return getParseGeoPoint(LOCALIZACAO);
    }
}