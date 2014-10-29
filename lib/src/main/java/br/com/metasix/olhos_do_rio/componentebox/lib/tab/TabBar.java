package br.com.metasix.olhos_do_rio.componentebox.lib.tab;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import br.com.metasix.olhos_do_rio.componentebox.lib.R;

public class TabBar implements OnTouchListener{
	
	private LinearLayout containerConteudo;
	private LinearLayout containerTabs;
	private LinearLayout ultimaTabSelecionada;

	/**
	 * 
	 * @param activity
	 * @param linear Linear aonde será exibido a TabBar e sua respectiva Informações
	 * @param itemsTab Lista de Tabs a ser montada
	 */
	public TabBar(final Activity activity, LinearLayout linear, final List<AbstractItemView> itemsTab){
		init(activity, linear);
		configurarTab(activity, linear, itemsTab);
	}

	private void configurarTab(final Activity activity, final LinearLayout linear,
			final List<AbstractItemView> itemsTab) {
		linear.post(new Runnable() {
			
			@SuppressLint("ResourceAsColor")
			@Override
			public void run() {
				int sizeTab = containerTabs.getWidth() / itemsTab.size();
				for (int i = 0; i < itemsTab.size(); i++) {
					LinearLayout tab = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.tab_item, null);
                    int heightTab = (int) ((linear.getHeight() * 8)) / 100;
					construirItemTab(tab, itemsTab.get(i), sizeTab, heightTab);
					if(i==0){
						ultimaTabSelecionada = tab;
						ultimaTabSelecionada.findViewById(R.id.tab_marcador).setBackgroundColor(R.color.color_current_tab);
					}
					View separator = new View(activity);
					LayoutParams param = new LayoutParams(1, LayoutParams.MATCH_PARENT);
					separator.setLayoutParams(param);
					separator.setBackgroundColor(R.color.color_separator);
					containerTabs.addView(separator);
				}
			}
		});
		containerConteudo.addView(itemsTab.get(0).getView());
	}

	private void init(final Activity activity, LinearLayout linear) {
		LayoutParams paramsConteudo = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LayoutParams paramsScrollView = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LayoutParams paramsTab = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		ScrollView scroll = new ScrollView(activity);
		scroll.setLayoutParams(paramsScrollView);
		
		containerTabs = new LinearLayout(activity);
		containerTabs.setLayoutParams(paramsTab);
		
		containerConteudo = new LinearLayout(activity);
		containerConteudo.setLayoutParams(paramsConteudo);
		
		scroll.addView(containerTabs);
		linear.addView(scroll);
		linear.addView(containerConteudo);
	}
	
	@SuppressLint("ResourceAsColor")
	private void construirItemTab(LinearLayout tab, AbstractItemView item, int sizeTab, int heightTab){
		tab.setBackgroundColor(android.R.color.transparent);
		LayoutParams paramsItemTab = new LayoutParams(sizeTab, heightTab);
		paramsItemTab.gravity = Gravity.CENTER;
		tab.setLayoutParams(paramsItemTab);
		if(item.getIcon() != null){
			((ImageView) tab.findViewById(R.id.tab_icon)).setImageDrawable(item.getIcon());
		}else{
			((ImageView) tab.findViewById(R.id.tab_icon)).setVisibility(ImageView.GONE);
		}
		if(!item.getTabName().equals("")){
			((TextView) tab.findViewById(R.id.tab_nome)).setText(item.getTabName());
		}else{
			((TextView) tab.findViewById(R.id.tab_nome)).setVisibility(TextView.GONE);
		}
		tab.setOnClickListener(configurarOnItemTabClickListener());
		tab.setTag(item.getView());
		containerTabs.addView(tab);
	}

	private OnClickListener configurarOnItemTabClickListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View selectedTab) {
				atualizarTabSelecionada(selectedTab);
			}
		};
	}
	
	public void trocarTab(int positionSelectedTab){
		View selectedTab = containerTabs.getChildAt(positionSelectedTab);
		atualizarTabSelecionada(selectedTab);
	}
	
	@SuppressLint("ResourceAsColor")
	private void atualizarTabSelecionada(View selectedTab) {
		ultimaTabSelecionada.findViewById(R.id.tab_marcador).setBackgroundColor(android.R.color.transparent);
		ultimaTabSelecionada = (LinearLayout) selectedTab;
		ultimaTabSelecionada.findViewById(R.id.tab_marcador).setBackgroundColor(R.color.color_current_tab);
		View currentView = (View) ultimaTabSelecionada.getTag();
		containerConteudo.removeAllViews();
		containerConteudo.addView(currentView);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	public LinearLayout getContainerConteudo() {
		return containerConteudo;
	}

	public LinearLayout getContainerTabs() {
		return containerTabs;
	}
	
}