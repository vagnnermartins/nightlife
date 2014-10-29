package br.com.metasix.olhos_do_rio.componentebox.lib.sliding;

import java.util.List;

import android.graphics.Bitmap;
import android.view.View.OnClickListener;

public class SecaoView {
	
	private int img;
	private Bitmap bitmap;
	private String titulo;
	private String subTitulo;
	private boolean isPrincipal;
	private List<SecaoView> secoes;
	private OnClickListener onClickListener;
	private Object tag;

	/**
	 * 
	 * @param img Referência da imagem para a seção
	 * @param titulo Descrição do Título para a seção
	 * @param subTitulo Descrição do Sub-Titulo para a seção
	 * @param isPrincipal se true a imagem terá largura da tela do dispositivo, senão terá a metade da tela de largura
	 * @param onClickListener listener para ser chamado no evento onClick da Seção configurada
	 */
	public SecaoView(int img, String titulo, String subTitulo, boolean isPrincipal, OnClickListener onClickListener) {
		super();
		this.img = img;
		this.titulo = titulo;
		this.subTitulo = subTitulo;
		this.isPrincipal = isPrincipal;
		this.onClickListener = onClickListener;
	}
	
	/**
	 * 
	 * @param bitmap Imagem Bitmap para a seção
	 * @param titulo Descrição do Título para a seção
	 * @param subTitulo Descrição do Sub-Titulo para a seção
	 * @param isPrincipal se true a imagem terá largura da tela do dispositivo, senão terá a metade da tela de largura
	 * @param onClickListener listener para ser chamado no evento onClick da Seção configurada
	 * @param tag Tag para ser estabelecida na View que será criada pelo componente
	 */
	public SecaoView(int img, String titulo, String subTitulo, boolean isPrincipal, OnClickListener onClickListener, Object tag) {
		super();
		this.img = img;
		this.titulo = titulo;
		this.subTitulo = subTitulo;
		this.isPrincipal = isPrincipal;
		this.onClickListener = onClickListener;
		this.tag = tag;
	}
	
	/**
	 * 
	 * @param bitmap Imagem Bitmap para a seção
	 * @param titulo Descrição do Título para a seção
	 * @param subTitulo Descrição do Sub-Titulo para a seção
	 * @param isPrincipal se true a imagem terá largura da tela do dispositivo, senão terá a metade da tela de largura
	 * @param onClickListener listener para ser chamado no evento onClick da Seção configurada
	 */
	public SecaoView(Bitmap bitmap, String titulo, String subTitulo, boolean isPrincipal, OnClickListener onClickListener) {
		super();
		this.bitmap = bitmap;
		this.titulo = titulo;
		this.subTitulo = subTitulo;
		this.isPrincipal = isPrincipal;
		this.onClickListener = onClickListener;
	}
	
	/**
	 * 
	 * @param bitmap Imagem Bitmap para a seção
	 * @param titulo Descrição do Título para a seção
	 * @param subTitulo Descrição do Sub-Titulo para a seção
	 * @param isPrincipal se true a imagem terá largura da tela do dispositivo, senão terá a metade da tela de largura
	 * @param onClickListener listener para ser chamado no evento onClick da Seção configurada
	 * @param tag Tag para ser estabelecida na View que será criada pelo componente
	 */
	public SecaoView(Bitmap bitmap, String titulo, String subTitulo, boolean isPrincipal, OnClickListener onClickListener, Object tag) {
		super();
		this.bitmap = bitmap;
		this.titulo = titulo;
		this.subTitulo = subTitulo;
		this.isPrincipal = isPrincipal;
		this.onClickListener = onClickListener;
		this.tag = tag;
	}
	
	/**
	 * 
	 * @param secoes Coleções de seções para ser criada uma seção com scrool horizontal
	 */
	public SecaoView(List<SecaoView> secoes){
		super();
		this.secoes = secoes;
	}
	public int getImg() {
		return img;
	}
	public void setImg(int img) {
		this.img = img;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getSubTitulo() {
		return subTitulo;
	}
	public void setSubTitulo(String subTitulo) {
		this.subTitulo = subTitulo;
	}
	public boolean isPrincipal() {
		return isPrincipal;
	}
	public void setPrincipal(boolean isPrincipal) {
		this.isPrincipal = isPrincipal;
	}
	public List<SecaoView> getSecoes() {
		return secoes;
	}
	public void setSecoes(List<SecaoView> secoes) {
		this.secoes = secoes;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public OnClickListener getOnClickListener() {
		return onClickListener;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}
}