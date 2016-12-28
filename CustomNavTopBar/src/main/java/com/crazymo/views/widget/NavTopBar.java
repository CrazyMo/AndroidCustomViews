package com.crazymo.views.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazymo.views.R;

/**
 * auther: Crazy.Mo
 * Date: 2016/12/26
 * Time:18:08
 * Des:
 */
public class NavTopBar extends RelativeLayout implements View.OnClickListener {
    private Context context;
    private TextView textView;
    private ImageView imageRight;
    private ImageView imageLeft;
    private NavOnClickListener navOnClickListener;
    private RelativeLayout topBg;
    private int bcgroundResId;
    private Drawable leftImgId,rightImgId;

    public NavTopBar(Context context) {
        super(context);
        this.context = context;
    }

    public NavTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public NavTopBar(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        View contentView = LayoutInflater.from(this.context).inflate(R.layout.navtop_layout, this, true);
        imageLeft = (ImageView) contentView.findViewById(R.id.id_nav_left);
        imageRight = (ImageView) contentView.findViewById(R.id.id_nav_right);
        topBg = (RelativeLayout) contentView.findViewById(R.id.rl_top_bg);
        textView = (TextView) contentView.findViewById(R.id.id_mid_nav);
        initAttr(attrs);
        //topBg.setBackgroundResource(bcgroundResId);
        topBg.setBackgroundColor(bcgroundResId);
        imageLeft.setOnClickListener(this);
        imageRight.setOnClickListener(this);
    }

    private void initAttr(AttributeSet attrs){
        TypedArray types = context.obtainStyledAttributes(attrs,
                R.styleable.nav_top_bar);
        try {
            bcgroundResId = types.getColor( R.styleable.nav_top_bar_bacgroud_color,getResources().getColor(R.color.blue ));
        } finally {
            types.recycle(); // TypeArray用完需要recycle
        }
    }

    @Override
    public void onClick(View v) {
        if (navOnClickListener != null) {
            switch (v.getId()) {
                case R.id.id_nav_left:
                    navOnClickListener.leftOnClickListener();
                    break;
                case R.id.id_nav_right:
                    navOnClickListener.rightOnClickListener();
                    break;
            }
        }
    }

    public void setNavBackground(int resId){
        topBg.setBackgroundResource(resId);
    }

    public void setTitle(String title) {
        textView.setText(title);
    }

    public void setNavOnClickListener(NavOnClickListener onClickListener) {
        navOnClickListener = onClickListener;
    }

    public interface NavOnClickListener {
        void leftOnClickListener();
        void rightOnClickListener();
    }

    /**
     * 设置默认的左边按钮的背景图
     * @param resouseId
     */
    public void setLeftImage(int resouseId) {
        imageLeft.setImageResource(resouseId);
    }

    public void setRightImage(int resouseId) {
        imageRight.setImageResource(resouseId);
    }

    /**
     * 设置是否显示左右按钮
     * @param left
     * @param right
     */
    public void setBarImageDisplay(boolean left, boolean right) {
        if(left){
            imageLeft.setVisibility(VISIBLE);
        }else {
            imageLeft.setVisibility(GONE);
        }
        if(right) {
            imageRight.setVisibility(VISIBLE);
        }else {
            imageRight.setVisibility(GONE);
        }
    }

}
