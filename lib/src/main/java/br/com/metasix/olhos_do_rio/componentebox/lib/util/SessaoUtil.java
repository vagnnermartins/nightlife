package br.com.metasix.olhos_do_rio.componentebox.lib.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessaoUtil {
	
	private final static String PREFERENCES = "map";
	
	public static void adicionarValores(Context context, String chave, String valor) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCES, 0);
		settings.edit().putString(chave, valor).commit();
	}

	public static String recuperarValores(Context context, String chave) {
		return context.getSharedPreferences(PREFERENCES, 0).getString(chave, null);
	}
}
