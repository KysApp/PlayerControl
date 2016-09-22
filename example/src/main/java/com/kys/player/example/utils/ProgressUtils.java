package com.kys.player.example.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

import com.kys.player.example.R;
import com.kys.player.example.base.CommonApi;
import com.kys.player.example.base.MyApplication;

public class ProgressUtils {

	private static TransparentProgressDialog DIALOG_TO_SHOW;
	public static Dialog getProgressDialog(Context context){
		DIALOG_TO_SHOW = new TransparentProgressDialog(context);
		DIALOG_TO_SHOW.setMessage("");
		if( context instanceof Activity ){
			if( ((Activity)context).isFinishing() 
					 ){
				context = MyApplication.getAppContext();
			}
		}
		return DIALOG_TO_SHOW;
	}
	
	/**
	 * 获取加载中的等待框
	 * @param context 		上下文
	 * @param title   		标题
	 * @param Message 		提示语
	 * @param couldCancel	能否取消该对话框
	 */
	public static void showProgressDialog(Context context,String title
			,String message,boolean couldCancel){
		DIALOG_TO_SHOW = new TransparentProgressDialog(context);
		if( null != title )
		DIALOG_TO_SHOW.setTitle(title);
		if( null != message )
		DIALOG_TO_SHOW.setMessage(message);
		DIALOG_TO_SHOW.setCancelable(couldCancel);
		if( context instanceof Activity ){
			if( ((Activity)context).isFinishing() 
					 ){
				context = MyApplication.getAppContext();
			}
		}
		try{
			DIALOG_TO_SHOW.show();
		}catch(Exception e){
			
		}
		
	}
	public static void showProgressDialog(Context context, boolean couldCancel){
		if(DIALOG_TO_SHOW!=null && DIALOG_TO_SHOW.isShowing())
			return;
		DIALOG_TO_SHOW = new TransparentProgressDialog(context);
		DIALOG_TO_SHOW.setCancelable(couldCancel);
		if( context instanceof Activity ){
			if( ((Activity)context).isFinishing() 
					 ){
				context = MyApplication.getAppContext();
			}
		}
		try{
			DIALOG_TO_SHOW.show();
		}catch(Exception e){
			
		}
	}
	public static void showProgressDialog(Context context,int title
			,String Message,boolean couldCancel){
		DIALOG_TO_SHOW = new TransparentProgressDialog(context);
		DIALOG_TO_SHOW.setTitle(title);
		DIALOG_TO_SHOW.setMessage("");
		DIALOG_TO_SHOW.setCancelable(couldCancel);
		if( context instanceof Activity ){
			if( ((Activity)context).isFinishing() 
					 ){
				context = MyApplication.getAppContext();
			}
		}
		try{
			DIALOG_TO_SHOW.show();
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 取消等待框
	 */
	public static void hideProgressDialog(){
		if( DIALOG_TO_SHOW!= null && DIALOG_TO_SHOW.isShowing()){
			try{
				DIALOG_TO_SHOW.dismiss();
			}catch (Exception e) {
			}
			DIALOG_TO_SHOW = null;
		}
	}
	
	private static AlertDialog mDialog;
	public static void showDialog(final Context context,String title
			,String message,boolean couldCancel){
		if( mDialog!=null ) mDialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if( null != title )
			builder.setTitle(title);
		if( null != message )
			builder.setMessage(message);
		builder.setPositiveButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if( context instanceof Activity ){
					dialog.dismiss();
					((Activity) context).finish();
				}
			}
		});
		builder.setCancelable(couldCancel);
		mDialog = builder.create();
		try{
			mDialog.show();
		}catch(Exception e){
			
		}
	}
	
	
	static private class TransparentProgressDialog extends Dialog {
		
		 public TransparentProgressDialog(Context context, int resourceIdOfImage) {
			 super(context, R.style.TransparentProgressDialog);
	         init(context);
		 }
		 
		 
		 public void setMessage(String string) {
			// TODO Auto-generated method stub
			
		}


		private void init(Context context) {
			 WindowManager.LayoutParams wlmp = getWindow().getAttributes();
	         wlmp.gravity = Gravity.CENTER_HORIZONTAL;
	         getWindow().setAttributes(wlmp);
			 setTitle(null);
			 setCancelable(false);
			 setOnCancelListener(null);
			  
			 LinearLayout layout = new LinearLayout(context);
			 layout.setOrientation(LinearLayout.VERTICAL);
			 LayoutParams params = new LayoutParams(CommonApi.dip2px(context,20), CommonApi.dip2px(context,20));
			 ProgressBar pb = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.progressbar, null);
			 layout.addView(pb);
			 addContentView(layout, params);
			
		}


		public TransparentProgressDialog(Context context) {
			 super(context, R.style.TransparentProgressDialog);
			 init(context);
		}


	}
}
