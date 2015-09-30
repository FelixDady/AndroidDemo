package com.hqu.baby.act;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import com.example.baby.R;
import com.hqu.baby.camera.CameraPreview;
import com.hqu.baby.constant.SocketConstant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class TakeVedioAct extends Activity
{

	private final String TAG = TakeVedioAct.class.getName();
	private final String SEND = "take vedio";
	private final String CLOSE = "close vedio";

	public static final String DATA = "TakeVedioAct_DATA";
	private final int NET_ERROR = 100;
	private final int SetText = 101;
	private final int INIT_BTN = 102;

	private String user;
	Socket socket;
	Context context;
	Button btn_take;
	Camera mCamera;
	MediaRecorder mMediaRecorder;
	SurfaceView mPreview;
	LocalServerSocket lss;
	LocalSocket receiver, sender;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_take_vedio);
		Bundle bundle = getIntent().getExtras();
		if (bundle.containsKey(DATA))
			user = bundle.getString(DATA);
		else
		{
			this.finish();
		}
		context = this;
		initData();
	}

	private void initData()
	{
		// mPreview=(SurfaceView)findViewById(R.id.sfv_vedio);
		// mPreview.getHolder().addCallback(this);

		try
		{
			if (mCamera == null)
				mCamera = getCameraInstance();
			mPreview = new CameraPreview(this, mCamera);
			FrameLayout fl = (FrameLayout) findViewById(R.id.fl_view);
			fl.addView(mPreview);
			mCamera.setPreviewDisplay(mPreview.getHolder());
			mCamera.startPreview();
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// prepareVideoRecorder();
		btn_take = (Button) findViewById(R.id.btn_take);
		btn_take.setText(SEND);
		btn_take.setVisibility(View.INVISIBLE);
		btn_take.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Log.i(TAG, "take vedio click");
				switch (btn_take.getText().toString())
				{
				case SEND:
					Log.i(TAG, "take vedio click send");
					sendVedio();

					break;
				case CLOSE:
					Log.i(TAG, "take vedio click close");
					
					try
					{
						socket.close();
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mMediaRecorder.stop(); // stop the recording
					releaseMediaRecorder(); // release the MediaRecorder object
					mCamera.lock();
					btn_take.setText(SEND);
					break;

				default:
					break;
				}
			}
		});
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				Looper.prepare();
				try
				{
					Log.i(TAG, "start a socket");
					socket = new Socket();
					SocketAddress socketAddress = new InetSocketAddress(
							SocketConstant.IpAddr, SocketConstant.PORT);
					socket.connect(socketAddress, 5000);
					Message msg = new Message();
					msg.what = INIT_BTN;
					handler.sendMessage(msg);
					Log.i(TAG, "connect the socket successful");
				} catch (UnknownHostException e)
				{
					// TODO Auto-generated catch block
					Message msg = new Message();
					msg.what = NET_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();

				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					Message msg = new Message();
					msg.what = NET_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void sendVedio()
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				Looper.prepare();
				try
				{
					Log.i(TAG, "send vedio run");
					OutputStream outputStream = socket.getOutputStream();
					String sendMsg = SocketConstant.SEND_VEDIO+user;
					outputStream.write(sendMsg.getBytes());
					InputStream inputStream = socket.getInputStream();
					byte[] buffer = new byte[1024];
					int length;
					Log.i(TAG, "send vedio start to read");
					length = inputStream.read(buffer);
					Log.i(TAG, "send vedio run read compelete");
					String result = new String(buffer).substring(0, length);
					Log.i(TAG, result);
					if (result.equals("OK"))
					{
						if (prepareVideoRecorder())
						{
							// Camera is available and unlocked, MediaRecorder
							// is prepared,
							// now you can start recording
							mMediaRecorder.start();
							//sendStream();

						} else
						{
							// prepare didn't work, release the camera
							releaseMediaRecorder();
							// inform user
						}
					}
					Message msg = new Message();
					msg.what = SetText;
					msg.obj = CLOSE;
					handler.sendMessage(msg);
					// else {
					// mMediaRecorder.stop(); // stop the recording
					// releaseMediaRecorder(); // release the MediaRecorder
					// object
					// mCamera.lock(); // take camera access back from
					// MediaRecorder
					// }

				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}
	@SuppressWarnings("unused")
	private void sendStream()
	{
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				Looper.prepare();
				try
				{
					OutputStream outputStream = socket.getOutputStream();
					InputStream inputStream=receiver.getInputStream();
					byte[]buffer =new byte[500000];
					
					File file=new File(Environment.getExternalStorageDirectory(),"abcd.mp4");
					OutputStream localOutputStream=new FileOutputStream(file);
					while(true)
					{
						int length=inputStream.read(buffer);
						if(length==-1)
							break;
						outputStream.write(buffer);
						localOutputStream.write(buffer);
					}
					inputStream.close();
					outputStream.close();
					localOutputStream.close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}).start();
	}

	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what)
			{
			case NET_ERROR:
				Log.e(TAG, "net error");
				Toast.makeText(context, "net error", Toast.LENGTH_SHORT).show();
				finish();
				break;
			case SetText:
				btn_take.setText((String) msg.obj);
				break;
			case INIT_BTN:
				btn_take.setVisibility(View.VISIBLE);
				btn_take.setText(SEND);
				break;
			default:
				break;
			}
		};

	};

	private boolean prepareVideoRecorder()
	{

		if (mCamera == null)
			mCamera = getCameraInstance();
		mMediaRecorder = new MediaRecorder();

		// Step 1: Unlock and set camera to MediaRecorder
		Log.d(TAG, "mcamera==null?" + (mCamera == null));
		mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);

		// Step 2: Set sources
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		

		// Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
		mMediaRecorder.setProfile(CamcorderProfile
				.get(CamcorderProfile.QUALITY_HIGH));

		// Step 4: Set output file

		// mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO)
		// .toString());
		
		
		//InitLocalSocket();
		//mMediaRecorder.setOutputFile(sender.getFileDescriptor());
		mMediaRecorder.setOutputFile(ParcelFileDescriptor.fromSocket(socket).getFileDescriptor());

		// Step 5: Set the preview output
		mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

		// Step 6: Prepare configured MediaRecorder
		try
		{
			mMediaRecorder.prepare();

		} catch (IllegalStateException e)
		{
			Log.d(TAG,
					"IllegalStateException preparing MediaRecorder: "
							+ e.getMessage());
			releaseMediaRecorder();
			return false;
		} catch (IOException e)
		{
			Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
			releaseMediaRecorder();
			return false;
		}
		return true;
	}

	public static Camera getCameraInstance()
	{
		Camera c = null;
		try
		{
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e)
		{
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		releaseMediaRecorder(); // if you are using MediaRecorder, release it
								// first
		releaseCamera(); // release the camera immediately on pause event
	}

	private void releaseMediaRecorder()
	{
		if (mMediaRecorder != null)
		{
			mMediaRecorder.reset(); // clear recorder configuration
			mMediaRecorder.release(); // release the recorder object
			mMediaRecorder = null;
			mCamera.lock(); // lock camera for later use
		}
	}

	private void releaseCamera()
	{
		if (mCamera != null)
		{
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/** Create a file Uri for saving an image or video */
	public static Uri getOutputMediaFileUri(int type)
	{
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	public static File getOutputMediaFile(int type)
	{
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"MyCameraApp");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists())
		{
			if (!mediaStorageDir.mkdirs())
			{
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault())
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE)
		{
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO)
		{
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else
		{
			return null;
		}

		return mediaFile;
	}

	@SuppressWarnings("unused")
	private void InitLocalSocket()
	{
		try
		{
			lss = new LocalServerSocket("H263");
			receiver = new LocalSocket();

			receiver.connect(new LocalSocketAddress("H263"));
			receiver.setReceiveBufferSize(500000);
			sender = lss.accept();
			sender.setSendBufferSize(500000);

		} catch (IOException e)
		{
			Log.e(TAG, e.toString());
			this.finish();
			return;
		}

	}

}
