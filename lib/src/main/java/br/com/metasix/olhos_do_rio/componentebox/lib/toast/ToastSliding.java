package br.com.metasix.olhos_do_rio.componentebox.lib.toast;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.metasix.olhos_do_rio.componentebox.lib.R;

public class ToastSliding extends RelativeLayout {
	
	private ImageView imagem;
	private TextView texto;
	private ImageView btnFechar;
	private View view;
	private Context context;
	private FrameLayout viewRoot;
	public static final int LONG_MESSAGE = 10000;
	public static final int SLOW_MESSAGE = 4000;
	public static final int FAST_MESSAGE = 2000;

	public static final int INFO_MESSAGE = 1;
	public static final int WARNNING_MESSAGE = 2;
	public static final int ERROR_MESSAGE = 3;
	public static final int SUCCESS_MESSAGE = 4;
	

	public ToastSliding(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	@SuppressLint("NewApi")
	private void init() {
		Activity activity = ((Activity)context);
		viewRoot = (FrameLayout)activity.getWindow().getDecorView().findViewById(android.R.id.content);
		
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); // You might want to tweak these to WRAP_CONTENT
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.view = inflater.inflate(R.layout.toast, null);
		view.setLayoutParams(params);
		
		LayoutTransition transition = new LayoutTransition();
		setLayoutTransition(transition);
		
		carregarComponentes();
	}

	private void carregarComponentes() {
		texto = (TextView)view.findViewById(R.id.toast_texto_msgs);
		btnFechar = (ImageView)view.findViewById(R.id.toast_btn_fechars);
		imagem = (ImageView)view.findViewById(R.id.toast_img_msgs);
		
		btnFechar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				viewRoot.removeView(view);
			}
			
		});
	}
	
	public void show(int tipo, String mensagem,int time){
		this.removeAllViews();
		texto.setText(mensagem);
		configurarTipoMensagem(tipo);
		viewRoot.addView(view);
		
		new RemoverMensagemAsyncTask(time).execute();
	}
	
	private void configurarTipoMensagem(int tipo) {
		int idImagem = 0;
		int idColor = 0;
		
		switch (tipo) {
		case INFO_MESSAGE:
			idImagem = R.drawable.info;
			idColor = R.color.info;
			break;

		case WARNNING_MESSAGE:
			idImagem = R.drawable.warnning;
			idColor = R.color.warnning;
			break;

		case ERROR_MESSAGE:
			idImagem = R.drawable.error;
			idColor = R.color.error;
			break;
			
		case SUCCESS_MESSAGE:
			idImagem = R.drawable.success;
			idColor = R.color.success;
			break;
		default:
			break;
		}
		
		imagem.setImageResource(idImagem);
		view.setBackgroundResource(idColor);
		
	}

	public class RemoverMensagemAsyncTask extends AsyncTask<Void, Void, String>{
		
		int time;
		public RemoverMensagemAsyncTask(int time) {
			this.time = time;
		}
		
		@SuppressWarnings("static-access")
		@Override
		protected String doInBackground(Void... params) {
			try {
				new Thread().sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String retorno) {
			viewRoot.removeView(view);
		}
	}

}
