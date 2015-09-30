package com.hqu.baby.act;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import com.example.baby.R;
import com.hqu.baby.constant.SocketConstant;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginAct extends Activity {
	private final int FAIL_LOGIN = 101;
	private final int FAIL_NET = 102;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);
		context = this;
		initData();
	}

	private void initData() {
		Button btn_login = (Button) findViewById(R.id.btn_login);
		final EditText et_user = (EditText) findViewById(R.id.et_user);
		final EditText et_passwd = (EditText) findViewById(R.id.et_passwd);

		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final String user = et_user.getText().toString();
				final String passwd = et_passwd.getText().toString();

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						try {
							Socket socket = new Socket(SocketConstant.IpAddr,
									SocketConstant.PORT);
							OutputStream outputStream = socket
									.getOutputStream();
							String sendMsg = new StringBuilder(user)
									.append("|").append(passwd).append("|")
									.toString();
							sendMsg = SocketConstant.CHECK_USER + user + "|"
									+ passwd;
							byte[] b = sendMsg.getBytes();
							outputStream.write(b, 0, b.length);
							InputStream inputStream = socket.getInputStream();
							byte[] buffer = new byte[100];
							int length = inputStream.read(buffer);
							Log.i("length", "" + length);
							String result = new String(buffer).substring(0,
									length);

							Log.i("LoginAct", result);
							try {
								int resultCode = Integer.parseInt(result);

								if (resultCode == 0) {
									Intent intent = new Intent(LoginAct.this,
											MainAct.class);
									intent.putExtra(MainAct.DATA, user);
									startActivity(intent);
								} else {
									Message msg = new Message();
									msg.what = FAIL_LOGIN;
									msg.arg1 = resultCode;
									handler.sendMessage(msg);
								}

							} catch (NumberFormatException e) {
								// TODO: handle exception
								e.printStackTrace();
							} finally {
								if (socket != null)
									socket.close();
							}

						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							Message msg = new Message();
							msg.what = FAIL_NET;
							handler.sendMessage(msg);
							e.printStackTrace();
						} catch (IOException e) {
							// TODO: handle exception
							Message msg = new Message();
							msg.what = FAIL_NET;
							handler.sendMessage(msg);
							e.printStackTrace();
						}

					}
				}).start();

			}
		});
	}

	private final Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case FAIL_LOGIN:
				switch (msg.arg1) {
				case -1:
					Toast.makeText(context, "user is not exist!",
							Toast.LENGTH_SHORT).show();
					break;
				case 1:
					Toast.makeText(context, "password is not corect!",
							Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}

				break;
			case FAIL_NET:
				Toast.makeText(context, "intnet error", Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}
			return false;
		}
	});

	public void forgetPasswd(View view) {
		startActivity(new Intent(LoginAct.this, FindPasswdAct.class));
	}

	public void register(View view) {
		startActivity(new Intent(LoginAct.this, RegisterAct.class));
	}
}
