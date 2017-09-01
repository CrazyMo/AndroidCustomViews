package crazymo.training.colorfultextview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Auther: Crazy.Mo
 * DateTime: 2017/5/5 10:20
 * Summary:
 */
public class ColorfulTextView extends TextView {
    private Paint shineyPaint;
    private LinearGradient linearGradient;
    private Matrix shinerMatrix;
    private int width,translate;
    public ColorfulTextView(Context context) {
        super(context);
        Log.e("ViewRun","构造方法");
    }

    public ColorfulTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("ViewRun","构造方法");
    }

    public ColorfulTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("ViewRun","构造方法");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.e("ViewRun","onLayout"+changed+"left:"+left+"top:"+ top+"right:"+ right+"bottom"+ bottom);
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("ViewRun","onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        Log.e("ViewRun","onFinishInflate");
        super.onFinishInflate();
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.e("ViewRun","onSizeChanged"+"w"+w+"h"+h+"oldw"+oldw+"oldh"+oldh);
        super.onSizeChanged(w, h, oldw, oldh);
        if(width==0){
            width=getMeasuredWidth();
            if(width>0){
                shineyPaint=getPaint();//获取当前的Paint对象
                linearGradient=new LinearGradient(0,0,width,0,new int[]{Color.GREEN,0xffffffff,Color.GREEN},null, Shader.TileMode.CLAMP);
                shineyPaint.setShader(linearGradient);
                shinerMatrix=new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("ViewRun","onDraw");
        super.onDraw(canvas);
        if(shinerMatrix!=null){
            translate+=width/5;
            if(translate>width*2){
                translate=-width;
            }
            shinerMatrix.setTranslate(translate,0);
            linearGradient.setLocalMatrix(shinerMatrix);
            postInvalidateDelayed(100);
        }
    }
}
