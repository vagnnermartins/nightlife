package br.com.metasix.olhos_do_rio.componentebox.lib.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.util.List;

public class CropImage {
	
	public static final String PATH_IMAGE_CROP = "path_image_crop";
	
	public static class CropOption {
		public CharSequence title;
		public Drawable icon;
		public Intent appIntent;
	}

	public static final int CROP_IMAGE = 0;

	public static void doCrop(final Activity activity, final Uri imageCaptureUri, int sizeX, int sizeY) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		/**
		 * Check if there is image cropper app installed.
		 */
		List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(
				intent, 0);

		int size = list.size();

		/**
		 * If there is no image cropper app, display warning message
		 */
		if (size == 0) {
			return;
		} else {
			/**
			 * Specify the image path, crop dimension and scale
			 */
			intent.setData(imageCaptureUri);

			intent.putExtra("outputX", sizeX);
			intent.putExtra("outputY", sizeY);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			File f = Environment.getExternalStorageDirectory();
			String pathFile = f.getAbsolutePath() + "/crop_image.jpg";
			SessaoUtil.adicionarValores(activity, PATH_IMAGE_CROP, pathFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(pathFile)));
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			/**
			 * There is posibility when more than one image cropper app exist,
			 * so we have to check for it first. If there is only one app, open
			 * then app.
			 */

			if (size == 1) {
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(1);

				i.setComponent(new ComponentName(res.activityInfo.packageName,
						res.activityInfo.name));
				activity.startActivityForResult(i, CROP_IMAGE);
			} else {
				/**
				 * If there are several app exist, create a custom chooser to
				 * let user selects the app.
				 */
				for (ResolveInfo res : list) {
					Intent i = new Intent(intent);
					i.setComponent(new ComponentName(
							res.activityInfo.packageName,
							res.activityInfo.name));
					activity.startActivityForResult(
							i,
							CROP_IMAGE);
					break;
				}
			}
		}
	}
}
