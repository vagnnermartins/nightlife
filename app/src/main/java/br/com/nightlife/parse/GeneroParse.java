package br.com.nightlife.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by vagnnermartins on 25/10/14.
 */
@ParseClassName("Genero")
public class GeneroParse extends ParseObject {

    private static final String GENERO = "genero";

    public String getGenero(){
        return getString(GENERO);
    }
}
