package com.crazymo.views.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.crazymo.views.R;

/**
 * auther: Crazy.Mo
 * Date: 2016/12/13
 * Time:10:27
 * Des:自定义的调色板，可以绘制圆形指示器，也可以自定义图标指示器,但是指示器会超出边界
 */
public class RainbowPalette extends View {
    private Context context;
    private Paint borderPaint;//色环外圈的彩虹圆环
    private Paint palettePaint;//渐变色环的画笔
    private Paint centerPaint;//中间圆画笔，用于显示当前选中的颜色
    private Paint indictorPaint; // 可移动小球画笔
    private int indictorColor;
    private int[] gradientColors;//渐变色环颜色
    private int centerCircleColor;
    private int width;//当前调色板的宽度
    private int height;//当前调色板的高度
    private float paletteRadius;//色环半径,整个环形外径，直接决定这个调色板的整体的大小，画渐变环可以看成画一个完整的圆形再挖掉一个小圆
    private float centerRadius;//中心圆半径
    private float paletteWidth;//色环的宽度
    private float indictorRadius;//小球的半径
    private Point indictorPosition;// 小球当前位置
    private Point centerPosition;//圆心的位置，色环圆心的位置
    private Bitmap indicatorBitmap; // 指示器小球的图标
    private int indictorResId;//指示器图标的id
    private RainbowPalette.OnColorChangedListen listen;
    public static boolean isNeedShowIndictor=false;

    private final static int BORDER_WIDTH=2;
    private final static int PALETTE_WIDTH=100;
    private final static int CENTER_CIRCLE_WIDTH=5;
    private final static int INDICTOR_WIDTH=5;
    private final static int DEF_INDICTOR_COLOR=0xFFc9f5f1;//设置指示小圆形的颜色
    private final static int DEF_CIRCLE_COLOR=0xFF0511FB;//设置中间圆的默认颜色

    public RainbowPalette(Context context) {
        super(context);
    }
    public RainbowPalette(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }
    public RainbowPalette(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        this.context=context;
        init(attrs);
    }

    private void init(AttributeSet attrs){
        setPaletteSize();
        initAttrColor(attrs);
        initPaint();
        initPosition();
        initRadiusWidth(attrs);
    }

    /**
     * 用于设置中间圆的颜色
     * @param color
     */
    public void setCenterPaint(int color){
        centerPaint.setColor(color);
    }

    /**
     * 设置调色板的尺寸
     */
    private void setPaletteSize(){
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);//获取WM对象
        int height = (int) (manager.getDefaultDisplay().getHeight() * 0.5f);//获取屏幕的高度*0.5
        int width = (int) (manager.getDefaultDisplay().getWidth() * 0.7f);//获取屏幕宽度的0.7
        this.height = height - 36;
        this.width = width;
        setMinimumHeight(height - 36);
        setMinimumWidth(width);
    }

    /**
     * 设置颜色指示器的位置
     * @param point
     */
    public void setIndictorPosition(Point point){
        if(point!=null) {
            this.indictorPosition.x = point.x;
            this.indictorPosition.y = point.y;
        }
    }

    /**
     * 设置指示器小球Color的默认值
     * @param color
     */
    public void setBallColor(int color){
        this.indictorColor=color;
    }

    /**
     * 初始化各种Paint对象
     */
    private void initPaint(){
        setGradientColors();
        Shader shader = new SweepGradient(0, 0, gradientColors, null);//SweepGradient渐变
        //外层彩虹光环
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//在画图的时候，图片如果旋转或缩放之后，总是会出现那些华丽的锯齿。给Paint加上抗锯齿标志
        borderPaint.setAntiAlias(true);
        borderPaint.setShader(shader);//传入着色器
        borderPaint.setStyle(Paint.Style.STROKE);//设置仅描边
        borderPaint.setStrokeWidth(BORDER_WIDTH);//设置描边的宽度，直接对应

        //初始化色环的Paint对象
        palettePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//在画图的时候，图片如果旋转或缩放之后，总是会出现那些华丽的锯齿。给Paint加上抗锯齿标志
        palettePaint.setAntiAlias(true);
        palettePaint.setShader(shader);//传入着色器
        palettePaint.setStyle(Paint.Style.STROKE);//设置仅描边
        palettePaint.setStrokeWidth(PALETTE_WIDTH);//设置描边的宽度，直接对应
        //初始化中心圆的Paint对象
        centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPaint.setAntiAlias(true);
        centerPaint.setColor(centerCircleColor);
        centerPaint.setStrokeWidth(CENTER_CIRCLE_WIDTH);
        //初始化小球对象
        indictorPosition=new Point();
        indictorPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        indictorPaint.setAntiAlias(true);
        indictorPaint.setColor(indictorColor);
        indictorPaint.setStrokeWidth(INDICTOR_WIDTH);
    }

    private void initAttrColor(AttributeSet attrs){
        TypedArray types = context.obtainStyledAttributes(attrs,
                R.styleable.rainbow_palette);
        try {
            centerCircleColor = types.getColor( R.styleable.rainbow_palette_center_circle_defcolor,DEF_CIRCLE_COLOR );
            indictorColor = types.getColor( R.styleable.rainbow_palette_indicator_color,DEF_INDICTOR_COLOR );
        } finally {
            types.recycle(); // TypeArray用完需要recycle
        }
    }

    /**
     * 设置色环和中心圆、圆形指示小球的半径,宽度
     */
    private void initRadiusWidth(AttributeSet attrs){
        TypedArray types = context.obtainStyledAttributes(attrs,
                R.styleable.rainbow_palette);
        try {

            paletteWidth = types.getDimensionPixelOffset( R.styleable.rainbow_palette_palette_width, PALETTE_WIDTH);
            paletteRadius=types.getDimensionPixelOffset(R.styleable.rainbow_palette_palette_radius,(int)(width / 2 - palettePaint.getStrokeWidth()*1.2f));
            centerRadius=types.getDimensionPixelOffset(R.styleable.rainbow_palette_palette_radius,(int)((paletteRadius - palettePaint.getStrokeWidth() / 2 ) * 0.5f));
            indictorResId=types.getResourceId(R.styleable.rainbow_palette_ic_indicator,0);
            if(indictorResId==0) {
                //未指定指示器的图标采用默认的绘制一个小圆形
                indictorRadius = (float) (centerRadius * 0.5);
            }else {
                initIndictorImg();//使用设置的指示器目标
            }
        } finally {
            types.recycle(); // TypeArray用完需要recycle
        }
    }

    /**
     * 初始化颜色指示器，设置指定图片
     */
    private void initIndictorImg(){
        // 将背景图片大小设置为属性设置的直径
        indicatorBitmap = BitmapFactory.decodeResource(getResources(), indictorResId );
        //indicatorBitmap = Bitmap.createScaledBitmap(indicatorBitmap, (int)centerRadius,(int)centerRadius, false);
        indicatorBitmap=Bitmap.createScaledBitmap(indicatorBitmap,indicatorBitmap.getWidth(),indicatorBitmap.getHeight(),true);
        indictorRadius=indicatorBitmap.getHeight();
    }

    /**
     * 设置色环的绘制圆心
     */
    private void initPosition(){
        centerPosition=new Point();
        centerPosition.set(width/2,height/2-50);
        indictorPosition.set(0,0);
    }

    /**
     * 设置渐变环的颜色取值
     */
    public void setGradientColors(){
        gradientColors = new int[] {0xFFFF0000, 0xFFFF00FF, 0xFF0000FF,0xFF00FFFF, 0xFF00FF00,0xFFFFFF00, 0xFFFF0000};
    }

    private void drawBorder(Canvas canvas){
        borderPaint.setAlpha(220);
        canvas.drawOval(new RectF(-(paletteRadius+55), -(paletteRadius+55), (paletteRadius+55), (paletteRadius+55)), borderPaint);//画次外圈
        borderPaint.setAlpha(100);
        canvas.drawOval(new RectF(-(paletteRadius+60), -(paletteRadius+60), (paletteRadius+60), (paletteRadius+60)), borderPaint);//画外圈
        borderPaint.setAlpha(60);
        canvas.drawOval(new RectF(-(paletteRadius+65), -(paletteRadius+65), (paletteRadius+65), (paletteRadius+65)), borderPaint);//画外圈
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerPosition.x,centerPosition.y);//移动中心,要不然会导致画出来之后不能完全显示,此时圆心相当于是由（0,0）变为(width / 2, height / 2 - 50)
        canvas.drawCircle(0,0, centerRadius,  centerPaint);//画中心圆
        canvas.drawOval(new RectF(-paletteRadius, -paletteRadius, paletteRadius, paletteRadius), palettePaint);//画色环
        drawBorder(canvas);
        if(isNeedShowIndictor) {
            if (indictorResId == 0) {
                canvas.drawCircle(indictorPosition.x, indictorPosition.y, (float) (centerRadius * 0.5), indictorPaint);//画颜色指示器小球
            } else {
                canvas.drawBitmap(indicatorBitmap, indictorPosition.x, indictorPosition.y, indictorPaint);//画指示器 指定图片
            }
        }
        Log.e("Position", "indictorPosition: X:"+indictorPosition.x+"Y:"+indictorPosition.y );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(width, height);//重新设置View的位置，若不重写的话，则不会布局，即使设置centerInParent为true也无效
        //setMeasuredDimension(width,height);
    }

   // 可以实现点击的时候显示对应的指示点，但会超过边界
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - width / 2;//event.getX()以父视图的左上角作为原点
        float y = event.getY() - height / 2 + 50;
        boolean isEcfect = isEfectiveTouch(x, y,
                paletteRadius + palettePaint.getStrokeWidth() / 2, paletteRadius - palettePaint.getStrokeWidth() / 2);
        int choosedColor;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(isEcfect){
                    float angle = (float) Math.atan2(y, x);
                    float unit = (float) (angle / (2 * Math.PI));
                    if (unit < 0) {
                        unit += 1;
                    }
                    choosedColor=getColorByTouchPalette(gradientColors,unit);
                    indictorPosition.set((int) (event.getX()-(width / 2)), (int) (event.getY()-(width / 2)));
                    centerPaint.setColor(choosedColor);
                    if(listen!=null) {
                        listen.onColorChange(choosedColor);
                    }
                }
                else {
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(isEcfect){
                    float angle = (float) Math.atan2(y, x);
                    float unit = (float) (angle / (2 * Math.PI));
                    if (unit < 0) {
                        unit += 1;
                    }
                    choosedColor=getColorByTouchPalette(gradientColors,unit);
                    indictorPosition.set((int) (event.getX()-(width / 2)), (int) (event.getY()-(width / 2)));
                    centerPaint.setColor(choosedColor);
                    if(listen!=null) {
                        listen.onColorChange(choosedColor);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                return true;
            default:
                break;
        }
        if (isEcfect) {
            isNeedShowIndictor=true;
            invalidate();
        }
        return true;
    }
    /**
     * 获取圆环上颜色
     * @param colors
     * @param unit
     * @return
     */
    private int getColorByTouchPalette(int colors[], float unit) {
        if (unit <= 0) {
            return colors[0];
        }
        if (unit >= 1) {
            return colors[colors.length - 1];
        }
        float p = unit * (colors.length - 1);
        int i = (int)p;
        p -= i;

        // now p is just the fractional part [0...1) and i is the index
        int c0 = colors[i];
        int c1 = colors[i+1];
        int a = ave(Color.alpha(c0), Color.alpha(c1), p);
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);

        return Color.argb(a, r, g, b);
    }

    private int ave(int s, int d, float p) {
        return s + Math.round(p * (d - s));
    }

    /**
     * 是否是有效Touch即是否是按在圆环之上的
     * @return
     */
    private boolean isEfectiveTouch(float x, float y, float outRadius, float inRadius){
        double outCircle = Math.PI * outRadius * outRadius;
        double inCircle = Math.PI * inRadius * inRadius;
        double fingerCircle = Math.PI * (x * x + y * y);
        if(fingerCircle < outCircle && fingerCircle > inCircle) {
            return true;
        }else {
            return false;
        }
    }


    /**
     * @describe 勾股定理求触摸点与圆心之间直线与水平方向的夹角角度
     * @param a 触摸点
     * @param b 圆心
     * @return
     */
    public float getRadian(Point a, Point b) {
        float lenA = Math.abs(b.x - a.x);
        float lenB = Math.abs(b.y - a.y);
        float lenC = (float) Math.sqrt(lenA * lenA + lenB * lenB);
        float ang = (float) Math.acos(lenA / lenC);
        ang = ang * (b.y < a.y ? -1 : 1);
        return ang;
    }

    /**
     * 设置小球的绘制位置，只能绘制在色环内
     * @return
     */
    private Point setIndictorPositionBorder(int x,int y){
        Point touchPosition=new Point(x,y);
        Point centerCircle=new Point(0,0);
        float radian=getRadian(touchPosition,centerCircle);
        float distance=getTwoPointDistance(centerCircle,touchPosition);//touch点和圆心之间的距离
        float touchToBallDistance;//touch点和内切时小球圆心所在的位置之间的距离,如果不超出色环之外，这两点位置重合，如果超出了则自动移到内切位置处
        if(distance+indictorRadius>(int)paletteRadius){
            touchToBallDistance=Math.abs(distance-paletteRadius+indictorRadius);//touch点和内切（与外环）时小球圆心所在的位置之间的距离
            if(Math.abs(Math.cos(radian))==1){//如果夹角为0或者180°
                if(x<0){
                    indictorPosition.set(-(int)(paletteRadius-20),0);
                }else {
                    indictorPosition.set((int)(paletteRadius-20),0);
                }
            }
            indictorPosition.set((int)(x-(Math.cos(radian)*touchToBallDistance)),(int)(y-(Math.sin(radian)*touchToBallDistance)));
        }else if(distance<((int)paletteRadius-160)){
            touchToBallDistance=Math.abs(paletteRadius-160-distance+indictorRadius);//touch点和外切（与内环）时小球圆心所在的位置之间的距离
            indictorPosition.set((int)(x+(Math.cos(radian)*touchToBallDistance)),(int)(y+(Math.sin(radian)*touchToBallDistance)));
        }else {
            indictorPosition.set(x,y);
        }
        return indictorPosition;
    }

    /**
     * 求两点之间的距离
     * @param a
     * @param b
     * @return
     */
    public float getTwoPointDistance(Point a,Point b){
        return (float) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public void setOnChangeListen(OnColorChangedListen listen){
        this.listen=listen;
    }

    public interface OnColorChangedListen{
        void onColorChange(int color);//自定义的颜色切换的时候触发的回调
        //void onColorAlphaChange(int color);
    }
}
