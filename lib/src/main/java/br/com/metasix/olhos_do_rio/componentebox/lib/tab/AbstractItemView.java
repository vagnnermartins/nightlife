package br.com.metasix.olhos_do_rio.componentebox.lib.tab;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Todos os itens do TabBar deverão extender a Classe 
 *
 */
public abstract class AbstractItemView {

	/**
	 * 
	 * @return Nome que será exibido na Tab
	 */
	public abstract String getTabName();
	
	/**
	 * 
	 * @return Imagem que será exibido na Tab
	 */
	public abstract Drawable getIcon();
	
	/**
	 * 
	 * @return View que será exibido quando a determinada Tab estiver selecionada
	 */
	public abstract View getView();;
	
}
