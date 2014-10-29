package br.com.metasix.olhos_do_rio.componentebox.lib.sliding;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import br.com.metasix.olhos_do_rio.componentebox.lib.R;

public class SecaoLayout {
	
	private static Context context;
	private static Display display;
	private static LinearLayout lnVertical;
	private static LinearLayout lnHorizontal;
	
	
	public static void montaFrameSecao(ViewGroup pai, ArrayList<SecaoView> secoes){
		context = pai.getContext();
		getDisplay();
		lnVertical = montarLinha(LinearLayout.VERTICAL);
		
		ScrollView scrool = new ScrollView(context);
		scrool.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		scrool.addView(lnVertical);
		pai.addView(scrool);
		
		for (SecaoView secaoView : secoes) {
			if (secaoView.isPrincipal()){
				lnVertical.addView(montarViewGroup(secaoView, false));
			}else if(secaoView.getSecoes() != null){
				lnVertical.addView(addSecaoScroll(secaoView.getSecoes()));
			} else {
				if (lnHorizontal == null || lnHorizontal.getChildCount() >= 2){
					lnHorizontal = montarLinha(LinearLayout.HORIZONTAL);
					lnVertical.addView(lnHorizontal);
				}
				lnHorizontal.addView(montarViewGroup(secaoView, false));
			}
		}
	}

	private static View addSecaoScroll(List<SecaoView> secoes){
		ViewFlipper flipper = new ViewFlipper(context);
		flipper.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		flipper.setOnTouchListener(configurarOnFlipperClickListener());
		
//		HorizontalScrollView scroll = new HorizontalScrollView(context);
//		scroll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		
//		LinearLayout linear = new LinearLayout(context);
//		linear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		LinearLayout linear;
		for (SecaoView secaoView2 : secoes) {
			linear = new LinearLayout(context);
			linear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
			linear.addView(montarViewGroup(secaoView2, true));
			flipper.addView(linear);
		}
		return flipper;
	}
	
	private static OnTouchListener configurarOnFlipperClickListener(){
		return new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ViewFlipper flipper = (ViewFlipper) v;
				flipper.showNext();
				Toast.makeText(context, "Fliper", Toast.LENGTH_LONG).show();
				return false;
			}

		};
	}
	
	@SuppressWarnings("deprecation")
	private static View montarViewGroup(SecaoView secaoView, boolean scroll) {
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout relative = (RelativeLayout) inflater.inflate(R.layout.secao, null);
		TextView titulo = (TextView) relative.findViewById(R.id.secao_titulo);
		TextView descricao = (TextView) relative.findViewById(R.id.secao_descricao);
		if (secaoView.isPrincipal()){
			relative.setLayoutParams(new LinearLayout.LayoutParams(display.getWidth(),	getDisplay().getHeight() / 3));
		}else{
			relative.setLayoutParams(new LinearLayout.LayoutParams(display.getWidth() / 2, (int) ((getDisplay().getHeight() / 3)* 0.7)));
		}
		if(scroll){
			relative.getLayoutParams().width = relative.getLayoutParams().width - 20;
		}
		if (secaoView.getBitmap() != null){
			relative.setBackgroundDrawable(new BitmapDrawable(secaoView.getBitmap()));
		} else {
			relative.setBackgroundResource(secaoView.getImg());
			
		}
		if(secaoView.getTitulo() == null || secaoView.getTitulo().equals("")){
			titulo.setVisibility(View.GONE);
		}else{
			titulo.setText(secaoView.getTitulo());
		}
		if(secaoView.getSubTitulo() == null || secaoView.getSubTitulo().equals("")){
			descricao.setVisibility(View.GONE);
		}else{
			descricao.setText(secaoView.getSubTitulo());
		}
		relative.setOnClickListener(secaoView.getOnClickListener());
		relative.setTag(secaoView.getTag());
		return relative;
	}

	@SuppressWarnings("static-access")
	private static Display getDisplay(){
		if(display == null)
			display = ((WindowManager) context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();
	return display;
	}
	
	private static LinearLayout montarLinha(int orietation){
		LinearLayout linhaHorizontal = new LinearLayout(context);
		linhaHorizontal.setOrientation(orietation);
		linhaHorizontal.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT));
		
		return linhaHorizontal;
	}
}
