package com.crazymo.training.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.crazymo.training.R;

/**
 * Created by cmo on 2016/10/6.
 * 加强型EditText:作为普通输入框可以设置非空提示的晃动动画效果，自带删除的按钮
 */
public class EnhancedEditText extends EditText implements OnFocusChangeListener, TextWatcher {

    private Drawable mRightIco;//显示于右边的Icon
    private Animation animtion;//用于提示非空的晃动动画
    private boolean isFocuse;//当前是否获得焦点
    private int counts;
    private int during;
    private float rightSize;
    private OnRightDrawableChanged rightChangedListener;

    public EnhancedEditText(Context context) {
        this(context, null);
    }

    public EnhancedEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);//直接引用系统的EditText的Style
    }

    public EnhancedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //引用自定义属性
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.enhancedEditText);
        during=typedArray.getInt(R.styleable.enhancedEditText_during,2000);
        counts=typedArray.getInt(R.styleable.enhancedEditText_counts,6);
        rightSize=typedArray.getFloat(R.styleable.enhancedEditText_rightSize,0.7f);
        init();
    }

    /**
     * 初始化右边的Icon和设置监听器
     */
    private void init() {
        //如果没有设置drawableRight属性则会获取默认的值，设置了drawableRight则会显示设置的值
        mRightIco = getCompoundDrawables()[2];
        if (mRightIco == null) {
            mRightIco = getResources().getDrawable(R.drawable.clear_selector);
        }
        //重新设置左边的Icon为左边Icon的大小
        if(getCompoundDrawables()[0]!=null) {
            mRightIco.setBounds(0, 0, (int) ((getCompoundDrawables()[0].getIntrinsicWidth()) * rightSize), (int) ((getCompoundDrawables()[0].getIntrinsicHeight()) * rightSize));
        }else {
            //若没有设置LeftDrawable的图像则默认设置右边的图像大小为96*96
            mRightIco.setBounds(0, 0, 96, 96);
        }

        setRightIconVisiable(false);//默认设置隐藏图标
        setOnFocusChangeListener(this);//设置焦点改变的监听
        addTextChangedListener(this);//设置输入框里面内容发生改变的监听
    }


    /**
     * 模拟点击事件当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间就相当于点击了右边的Icon
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean isRightClick = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (isRightClick) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置右边图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.isFocuse = hasFocus;
        if (hasFocus) {
            setRightIconVisiable(getText().length() > 0);
        } else {
            setRightIconVisiable(false);
        }
    }

    /**
     *设置Drawable显示于文字之上 setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom)
     *设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     * @param visible
     */
    protected void setRightIconVisiable(boolean visible) {
        Drawable right = visible ? mRightIco : null;
        if(rightChangedListener!=null) {
            if (visible) {
                rightChangedListener.onRightDisplay();
            } else {
                rightChangedListener.onRightInvisible();
            }
        }
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        if (isFocuse) {
            setRightIconVisiable(s.length() > 0);//如果没有输入字符串则会隐藏
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
    /**
     * 启动晃动动画
     * @param animation 可定义自定义的动画效果，若不定义则使用默认的动画
     */
    public void setShakeAnimation(Animation animation) {
        if(animation==null) {
            this.startAnimation(genDefaultAnimation());
        }else{
            this.startAnimation(animation);
        }
    }

    /**
     * 设置默认的晃动动画
     * @return animation
     */
    private Animation genDefaultAnimation() {
        animtion = new TranslateAnimation(0, 10, 0, 0);
        animtion.setInterpolator(new CycleInterpolator(counts));
        animtion.setDuration(during);
        return animtion;
    }
    public void setRightChangedListener(OnRightDrawableChanged listener){
        rightChangedListener=listener;
    }
    public interface OnRightDrawableChanged{

        void onRightDisplay();
        void onRightInvisible();
    }
}
