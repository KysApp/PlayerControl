package com.kys.player.example.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kys.player.example.R;

/**
 * Created by 幻云紫日 on 2016/4/13.
 */
public abstract class BaseFragment extends Fragment {
	private final String TAG = BaseFragment.class.getSimpleName();
	private View mView = null;
	private View mContainer;
	private FrameLayout mContent;
	private MyOnClickListener myListener;
	private Fragment search,record;
	private boolean isAdded;
	protected Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	/**
	 * @param id layout的id
	 */
	public void setContentView(int id) {
		// TODO Auto-generated method stub
		mView = LayoutInflater.from(getActivity()).inflate(id, null);
	}

	/**
	 * @param view
	 */
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		mView = view;
	}


	/**Look for a child view with the given id.
	 *  If this view has the given id, return this view.
	 * @param id The id to search for.
	 * @return Look for a child view with the given id. 
	 * If this view has the given id, return this view.
	 */
	public View findViewById(int id) {
		// TODO Auto-generated method stub
		View view = null;
		if(mContainer != null)
		{
			view = mContainer.findViewById(id);
		}
		return view;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}
		if (mContainer == null) {
			View view = inflater.inflate(R.layout.ui_base, null);
			mContainer = view;
			mContent = (FrameLayout) mContainer.findViewById(R.id.base_content);

			myListener = new MyOnClickListener();
		} else {

		}

		return mContainer;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(mView != null && !isAdded)
		{
			if(mContent!=null) {
				mContent.removeAllViews();
				mContent.addView(mView);
				isAdded = true;
			}
		}
		logics();

	}

	/**
	 * findView，listener等逻辑代码
	 */
	public abstract void logics();

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void startActivity(Intent intent) {
		// TODO Auto-generated method stub
		super.startActivity(intent);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
	}


	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		super.onAttach(activity);
	}

	public Context getMyActivity(){
		if( mContext instanceof Activity ){
			return mContext;
		}

		mContext = getActivity();

		if( mContext == null ){
			mContext = MyApplication.getAppContext();
		}
		return mContext;
	}

	class MyOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			}
		}

	}
}
