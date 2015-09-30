package com.hqu.baby.algorithm;

import android.content.Context;

public class Tools {
	public static String getSystemId(String userId) {
		StringBuilder sb = new StringBuilder();
		long time = System.currentTimeMillis();
		short mode = 0;
		while (true) {
			if (time <= 0)
				break;
			mode = (short) (time & 077);
			if (mode <= 9)
				sb.insert(0, (char) (mode + 48));
			else if (mode <= 35)
				sb.insert(0, (char) (mode + 55));
			else if (mode <= 61)
				sb.insert(0, (char) (mode + 61));
			else if (mode == 62)
				sb.insert(0, (char) (mode - 16));
			else
				sb.insert(0, (char) (mode + 32));
			time = time >> 6;
		}
		sb.insert(0, userId);
		return sb.toString();
	}

	public static String getSystemId() {
		StringBuilder sb = new StringBuilder();
		long time = System.currentTimeMillis();
		short mode = 0;
		while (true) {
			if (time <= 0)
				break;
			mode = (short) (time & 077);
			if (mode <= 9)
				sb.insert(0, (char) (mode + 48));
			else if (mode <= 35)
				sb.insert(0, (char) (mode + 55));
			else if (mode <= 61)
				sb.insert(0, (char) (mode + 61));
			else if (mode == 62)
				sb.insert(0, (char) (mode - 16));
			else
				sb.insert(0, (char) (mode + 32));
			time = time >> 6;
		}
		return sb.toString();
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
