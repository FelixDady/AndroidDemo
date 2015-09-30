package com.hqu.baby.act;

import com.example.baby.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class RegisterAct extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
	}
	
	public void register(View view)
	{
		this.finish();
	}

}
