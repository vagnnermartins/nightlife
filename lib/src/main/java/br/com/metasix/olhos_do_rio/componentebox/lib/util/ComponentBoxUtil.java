package br.com.metasix.olhos_do_rio.componentebox.lib.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ComponentBoxUtil {
	
	public enum ImagemSize{
		ICON(50),
		THUMBNAILS(70),
		FOTO(250);
		
		private final int valor;
		
		private ImagemSize(int valor){
			this.valor = valor;
		}
		
		public int getValor(){
	        return valor;
	    }
	}
	
	public static void esconderTeclado(FragmentActivity activity, EditText editText) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	public static void esconderTeclado(Activity activity, EditText editText) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	public static Bitmap compressImage(File imagem, ImagemSize tamanho, int qualidade) throws IOException{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(new FileInputStream(imagem), null, options);
		int scale = 1;
		while(options.outWidth / scale /2 >= tamanho.getValor() && options.outHeight / scale / 2 >= tamanho.getValor())
			scale *= 2;
		BitmapFactory.Options options2 = new BitmapFactory.Options();
		options2.inSampleSize=scale;
		Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imagem), null, options2);
		FileOutputStream fos = new FileOutputStream(imagem);
		bitmap.compress(CompressFormat.JPEG, qualidade, fos);
		fos.flush();
		fos.close();
		return bitmap;
	}
	
	public static void verificaConexao(Context context) throws Exception{  
	    ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
	    if (!(conectivtyManager.getActiveNetworkInfo() != null  
	            && conectivtyManager.getActiveNetworkInfo().isAvailable()  
	            && conectivtyManager.getActiveNetworkInfo().isConnected())) {
	    	throw new Exception();
	    }
	} 
	
	public static Bitmap convertByteArrayToBitmap(byte[] bytes){
		Bitmap bitmap = null;
		bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		ByteArrayOutputStream blob = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 0 /*ignored for PNG*/, blob);
		return bitmap;
	}
	
	public static byte[] convertBitmapToBytes(Bitmap bitmap){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}
	
	public static byte[] readBytesFromFile(File file) throws Exception {
	      InputStream is = new FileInputStream(file);
	      long length = file.length();
	      if (length > Integer.MAX_VALUE) {
	    	  is.close();
	        throw new Exception();
	      }
	      byte[] bytes = new byte[(int)length];
	      int offset = 0;
	      int numRead = 0;
	      while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	          offset += numRead;
	      }
	      if (offset < bytes.length) {
	    	  is.close();
	          throw new IOException("Could not completely read file " + file.getName());
	      }
	      is.close();
	      return bytes;
	}
	
}
