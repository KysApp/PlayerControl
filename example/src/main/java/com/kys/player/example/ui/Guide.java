package com.kys.player.example.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


import com.kys.player.example.R;

import java.util.ArrayList;

/**
 * Created by 幻云紫日 on 2016/4/13.
 */
public class Guide extends Activity implements OnClickListener, OnPageChangeListener {
	private final String TAG = Guide.class.getSimpleName();
	
	private ArrayList<View> views,hints;
	private int[] guides = {R.drawable.guide1};
	private LinearLayout mLayout;
	private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_guide);
		mLayout = (LinearLayout) findViewById(R.id.hint);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		logic();
	}
	private void logic() {
		
		views = new ArrayList<View>();
		hints = new ArrayList<View>();
		for(int i = 0 ;i < guides.length;i++)	//引导界面的图片
		{
			RelativeLayout layout = new RelativeLayout(this);
			ImageView imageView = new ImageView(this);
			LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageResource(guides[i]);
			imageView.setOnClickListener(this);
			layout.addView(imageView);
			
			views.add(layout);
			
			ImageView hint = new ImageView(this);
			hint.setImageResource(R.drawable.hint_unselected);
			mLayout.addView(hint);
			hints.add(hint);
		}
		changeHint(0);
		viewPager.setAdapter(new MPagerAdapter());
		viewPager.setOnPageChangeListener(this);
	}

	class MPagerAdapter extends PagerAdapter
	{

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView(views.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			View view = views.get(position);
			container.addView(view);
			return view;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
	}

	/* 跳转到主页Main
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(viewPager.getCurrentItem() == views.size() - 1)
		{
			if(getIntent().getIntExtra("action", 0) == 1)
			{
				startActivity(new Intent(this,Main.class));
			}
			finish();
		}
		else
		{
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
		}
	}
/*****************begin OnPageChangeListener***************************/
	private boolean isStart;
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		if(arg0 == ViewPager.SCROLL_STATE_DRAGGING)
		{
			if (viewPager.getCurrentItem() == views.size() - 1) {
				isStart = true;
			}
			else
			{
				isStart = false;
			}
		}
		
		if(arg0 == ViewPager.SCROLL_STATE_IDLE )
		{
			if(viewPager.getCurrentItem() == views.size() - 1 && isStart)
			{
				if(getIntent().getIntExtra("action", 0) == 1)
				{
					startActivity(new Intent(this,Main.class));
				}
				finish();
			}
			else
			{
				isStart = false;
			}
		}
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		changeHint(arg0);
	}
	/*****************end OnPageChangeListener***************************/
	
	/**改变提示图片
	 * @param arg0
	 */
	private void changeHint(int arg0) {
		// TODO Auto-generated method stub
		for(int i = 0;i < hints.size();i++)
		{
			ImageView imageView = (ImageView) hints.get(i);
			if(i == arg0)		
			{
				//使用选中图片
				imageView.setImageResource(R.drawable.hint_selected);
			}
			else
			{
				//使用未选中图片
				imageView.setImageResource(R.drawable.hint_unselected);
			}
		}
	}
}
