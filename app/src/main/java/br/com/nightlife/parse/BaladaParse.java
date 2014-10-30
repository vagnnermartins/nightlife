package br.com.nightlife.parse;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Balada")
public class BaladaParse extends ParseObject {

    private static final String NOME = "nome";
    private static final String ENDERECO = "endereco";
    private static final String CEP = "cep";
    private static final String BAIRRO = "bairro";
    private static final String CIDADE = "cidade";
    private static final String ESTADO = "estado";
    private static final String TELEFONE = "telefone";
    private static final String HORA_FUNCIONAMENTO = "horaFuncionamento";
    private static final String WEBSITE = "website";
    private static final String STATUS = "status";
    private static final String LOCALIZACAO = "localizacao";
    private static final String ANIVERSARIO = "aniversario";
    private static final String PACOTE_ANIVERSARIO = "pacoteAniversario";
    private static final double MAX_DISTANCE = 100;

    public static void buscarBaladas(ParseGeoPoint point, FindCallback<ParseObject> callback){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Balada");
        if(point != null){
            query.whereNear(LOCALIZACAO, point);
            query.whereWithinKilometers(LOCALIZACAO, point, MAX_DISTANCE);
        }
        query.findInBackground(callback);
    }

    public String getNome(){
        return getString(NOME);
    }

    public String getEndereco(){
        return getString(ENDERECO);
    }

    public String getCep(){
        return getString(CEP);
    }

    public String getBairro(){
        return getString(BAIRRO);
    }

    public String getCidade(){
        return getString(CIDADE);
    }

    public String getEstado(){
        return getString(ESTADO);
    }

    public String getTelefone(){
        return getString(TELEFONE);
    }

    public String getHoraFuncionamento(){
        return getString(HORA_FUNCIONAMENTO);
    }

    public String getWebSite(){
        return getString(WEBSITE);
    }

    public ParseGeoPoint getLocalizacao(){
        return getParseGeoPoint(LOCALIZACAO);
    }

    public boolean isPacoteAniversario(){
        return getBoolean(ANIVERSARIO);
    }

    public String getPacoteAniversario(){
        return getString(PACOTE_ANIVERSARIO);
    }

    public String getEnderecoFormatado(){
        return getEndereco() + ", " + getBairro() + ", " + getCidade();
    }

    @Override
    public String toString() {
        return getNome();
    }
}
