package com.isoftstone.tianlai.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class ViewGroupHook extends FrameLayout
{
  @SuppressWarnings("unused")
private Context mContext = null;

  public ViewGroupHook(Context paramContext)
  {
    super(paramContext);
    mContext = paramContext;
  }

  public ViewGroupHook(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    mContext = paramContext;
  }

  public ViewGroupHook(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    mContext = paramContext;
  }

  @SuppressLint("ClickableViewAccessibility")
public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    return true;
  }
}
