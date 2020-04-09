package com.example.physicalfitnessexamination.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.util.DeviceUtils;
import com.example.physicalfitnessexamination.util.Tool;

public class MyToolBar extends Toolbar {
    private TextView textTile, textRight;
    private ImageView imgBack;
    private ImageView imgRight;
    private MyToolBar.LayoutParams paramsTitle;
    private MyToolBar.LayoutParams paramsBack;
    private MyToolBar.LayoutParams paramsImgRight;
    private MyToolBar.LayoutParams paramsTextRight;
    private ToolBarLeftOnClickListener toolBarLeftOnClickListener;
    private ToolBarRightOnClickListener toolBarRightOnClickListener;


    public MyToolBar(Context context) {
        super(context);
        initDraw();
    }

    public MyToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDraw();
    }

    public MyToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDraw();
    }

    public void initDraw() {
        drawBack();
        drawTitle();
        drawImgRight();
        drawTextRight();
        addView(imgBack, paramsBack);
        addView(textTile, paramsTitle);
        addView(imgRight, paramsImgRight);
        addView(textRight, paramsTextRight);
    }

    public void setTitle(String title) {
        textTile.setText(title);
    }

    public void setRightText(String rightText) {
        textRight.setText(rightText);
    }

    public void setRightImg(int res) {
        imgRight.setBackgroundResource(res);
    }

    public void setLeftImg(int res) {
        imgBack.setBackgroundResource(res);
    }

    public void setLeftVisible(boolean b) {
        if (b) {
            imgBack.setVisibility(VISIBLE);
        } else {
            imgBack.setVisibility(GONE);
        }
    }

    public void setRightImgVislble(boolean b) {
        if (b) {
            imgRight.setVisibility(VISIBLE);
        } else {
            imgRight.setVisibility(GONE);
        }
    }

    public void drawTitle() {
        paramsTitle = new MyToolBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsTitle.gravity = Gravity.CENTER;
        textTile = new TextView(getContext());
        textTile.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        textTile.setTextSize(16);
        TextPaint paint = textTile.getPaint();
        paint.setFakeBoldText(true);
    }

    public void drawBack() {
        paramsBack = new MyToolBar.LayoutParams(DeviceUtils.dip2px(getContext(), 20), DeviceUtils.dip2px(getContext(), 20));
        paramsBack.gravity = Gravity.CENTER_VERTICAL;
        imgBack = new ImageView(getContext());
        imgBack.setBackgroundResource(R.mipmap.back1);
        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Tool.isEmpty(toolBarLeftOnClickListener)) {
                    toolBarLeftOnClickListener.setToolBarLeftOnClick();
                }
            }
        });
    }

    public void drawImgRight() {
        paramsImgRight = new MyToolBar.LayoutParams(DeviceUtils.dip2px(getContext(), 20), DeviceUtils.dip2px(getContext(), 20));
        paramsImgRight.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        paramsImgRight.setMargins(0, 0, DeviceUtils.dip2px(getContext(), 20), 0);
        imgRight = new ImageView(getContext());
        imgRight.setBackgroundResource(R.mipmap.back);
        imgRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Tool.isEmpty(toolBarRightOnClickListener)) {
                    toolBarRightOnClickListener.setToolBarRightOnClick();
                }
            }
        });
    }

    public void drawTextRight() {
        paramsTextRight = new MyToolBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsTextRight.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        paramsTextRight.setMargins(0, 0, DeviceUtils.dip2px(getContext(), 20), 0);
        textRight = new TextView(getContext());
        textRight.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        textRight.setTextSize(16);
        textRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Tool.isEmpty(toolBarRightOnClickListener)) {
                    toolBarRightOnClickListener.setToolBarRightOnClick();
                }
            }
        });
    }

    public interface ToolBarLeftOnClickListener {
        void setToolBarLeftOnClick();
    }

    public interface ToolBarRightOnClickListener {
        void setToolBarRightOnClick();
    }

    public void setToolBarLeftOnClickListener(ToolBarLeftOnClickListener toolBarLeftOnClickListener) {
        this.toolBarLeftOnClickListener = toolBarLeftOnClickListener;
    }

    public void setToolBarRightOnClickListener(ToolBarRightOnClickListener toolBarRightOnClickListener) {
        this.toolBarRightOnClickListener = toolBarRightOnClickListener;
    }
}
