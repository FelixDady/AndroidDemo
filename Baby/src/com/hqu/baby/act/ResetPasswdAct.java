package com.hqu.baby.act;

import com.example.baby.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPasswdAct extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resetpassword);
		Button bt_finished=(Button)findViewById(R.id.bt_finished);
		final String passwd=((EditText)findViewById(R.id.et_newpassword)).getText().toString();
		final String repasswd=((EditText)findViewById(R.id.et_confirmpassword)).getText().toString();
		bt_finished.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(!passwd.equals(repasswd))	
				{
					Toast.makeText(ResetPasswdAct.this, "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
					return;
				}
				setResult(RESULT_OK);
				finish();
			}
		});
		
	}

}
