package br.com.nightlife.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by vagnnermartins on 15/12/14.
 */
public class DialogUtil {

    public static void showDialog(Context context, int title, int message,
                                  int positiveText, DialogInterface.OnClickListener onPositiveButton,
                                  int negativeText, DialogInterface.OnClickListener onNegativeButton){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButton);
        builder.setNegativeButton(negativeText, onNegativeButton);
        builder.setCancelable(false);
        builder.create().show();
    }
}
