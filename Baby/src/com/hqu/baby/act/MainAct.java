package com.hqu.baby.act;

import com.example.baby.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainAct extends Activity
{
	public static final String DATA="MainAct_DATA";
	private String user;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		Bundle bundle=getIntent().getExtras();
		if(bundle.containsKey(DATA))
			user=bundle.getString(DATA);
		else {
			Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
			this.finish();
		}
		initData();
		
		
	}
	
	
	private void initData()
	{
		TextView tv_takeVideo=(TextView) findViewById(R.id.tv_takeVideo);
		TextView tv_watchVideo=(TextView)findViewById(R.id.tv_watchVideo);
		tv_takeVideo.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainAct.this,TakeVedioAct.class);
				intent.putExtra(TakeVedioAct.DATA, user);
				startActivity(intent);
			}
		});
		tv_watchVideo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
