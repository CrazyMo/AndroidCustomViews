package crazy.com.customview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import crazy.com.customview.R;

/**
 * auther: Crazy.Mo
 * Date: 2017/3/30
 * Time:10:09
 * Des:
 */

public class CheckedableItemView extends TextView {

    private Context context;
    private int modeType;//默认单选0,多选 1
    private boolean isCheckedable=true;
    private boolean isChecked=false;
    private int rightIconId;
    private Drawable rightIcon;//未选中
    private Drawable rightCheckedIcon;//选中

    public CheckedableItemView(Context context) {
        super(context);
        this.context=context;
    }

    public CheckedableItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(attrs);
    }

    public CheckedableItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init(attrs);
    }
    private void init(AttributeSet attrs){
        initAttrs(attrs);
        setRightIcon();
    }

    private void initAttrs(AttributeSet attrs){
        TypedArray types = context.obtainStyledAttributes(attrs,
                R.styleable.checkedable_view);
        try {
            modeType = types.getInt( R.styleable.checkedable_view_modeType,0 );
            isCheckedable = types.getBoolean( R.styleable.checkedable_view_isChecked,true );
            rightIcon=types.getDrawable(R.styleable.checkedable_view_rightIcon);
            rightCheckedIcon=types.getDrawable(R.styleable.checkedable_view_rightCheckedIcon);
        } finally {
            types.recycle(); // TypeArray用完需要recycle
        }
    }

    public boolean isCheckedable() {
        return isCheckedable;
    }

    public void setCheckedable(boolean checkedable) {
        isCheckedable = checkedable;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private void setRightIcon(){

        if(isChecked) {
            rightCheckedIcon.setBounds(0, 0, 68, 68);
            setCompoundDrawables(getCompoundDrawables()[0],
                    getCompoundDrawables()[1], rightCheckedIcon, getCompoundDrawables()[3]);
        }else {
            rightIcon.setBounds(0, 0, 68, 68);
            setCompoundDrawables(getCompoundDrawables()[0],
                    getCompoundDrawables()[1], rightIcon, getCompoundDrawables()[3]);
        }
    }

    /**
     * 模拟点击事件当我们按下的位置 在  TextView的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * TextView的宽度 - 图标到控件右边的间距之间就相当于点击了右边的Icon
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return true;//只有在 MotionEvent.ACTION_DOWN 返回为true时，才会继续产生MotionEvent.ACTION_MOVE/UP事件，
            case MotionEvent.ACTION_UP:
                if (getCompoundDrawables()[2] != null) {

                    boolean isRightClick = event.getX() > (getWidth() - getTotalPaddingRight())
                            && (event.getX() < ((getWidth() - getPaddingRight())));
                    if (isRightClick) {
                        if(isCheckedable) {
                            doChecked();
                        }
                    }
                }
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    private void doChecked(){
        if(isChecked){
            rightIcon.setBounds(0, 0, 68, 68);
            setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1], rightIcon, getCompoundDrawables()[3]);
            isChecked=false;
        }else {
            rightCheckedIcon.setBounds(0, 0, 68, 68);
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], rightCheckedIcon, getCompoundDrawables()[3]);
            isChecked=true;
        }
    }
}
