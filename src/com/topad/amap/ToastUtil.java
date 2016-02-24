/**
 * 
 */
package com.topad.amap;

import android.content.Context;
import android.widget.Toast;

import com.topad.util.Utils;

public class ToastUtil {

	public static void show(Context context, String info) {
		if(Utils.isEmpty(info)||context==null){
			return;
		}
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}

	public static void show(Context context, int info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}
}
