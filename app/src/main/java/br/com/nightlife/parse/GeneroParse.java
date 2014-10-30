package br.com.nightlife.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Genero")
public class GeneroParse extends ParseObject {

    private static final String GENERO = "genero";

    public String getGenero(){
        return getString(GENERO);
    }

    @Override
    public String toString() {
        return getGenero();
    }
}
