package com.crazymo.views;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.crazymo.views.widget.RainbowPalette;

public class MainActivity extends AppCompatActivity implements RainbowPalette.OnColorChangedListen ,View.OnClickListener,SeekBar.OnSeekBarChangeListener{

    private RainbowPalette rainbowPalette;
    private TextView txtTitle,txtChoosePre,txtChooseNext;
    private SeekBar seekBar;
    private SparseArray sparseArray;
    private int currentIndex = 6;//当前的颜色的index，默认为蓝色
    private int currentColor;//选中的颜色
    private String currentRGB;
    private int[] ledNormalColor = {0xFFFFFFFF, 0xFFFF0000, 0xFFF3990C, 0xFFEEF60B, 0xFF3C981B, 0xFF3CE2F3, 0xFF0511FB, 0xFFAB56EE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void initViews(){
        rainbowPalette= (RainbowPalette) findViewById(R.id.imv_led_palettle);
        txtTitle= (TextView) findViewById(R.id.id_txt_palette);
        txtChoosePre= (TextView) findViewById(R.id.tv_choose_pre);
        txtChooseNext= (TextView) findViewById(R.id.tv_choose_next);
        seekBar= (SeekBar) findViewById(R.id.id_alpha_seek_bar);
        txtChoosePre.setOnClickListener(this);
        txtChooseNext.setOnClickListener(this);
        rainbowPalette.setOnChangeListen(this);
        seekBar.setOnSeekBarChangeListener(this);
    }
    private void init(){
        initViews();
        initColorPosition();
    }

    /**
     * 手动设置指示小球显示的位置
     */
    public void setIndictorPosition(int index){
        if (sparseArray!=null) {
            rainbowPalette.setIndictorPosition((Point) sparseArray.get(ledNormalColor[index]));
            rainbowPalette.setCenterPaint((ledNormalColor[currentIndex]));
        }
        RainbowPalette.isNeedShowIndictor=false;
        rainbowPalette.invalidate();
    }

    private void onChooseColor(View view){
        switch (view.getId()){
            case R.id.tv_choose_pre:
                setIndictorPosition(choosePreColor());
                break;
            case R.id.tv_choose_next:
                setIndictorPosition(chooseNextColor());
                break;
            default:
                break;
        }
    }

    /**
     * 选择前一种颜色，并返回对应的索引
     *
     * @return
     */
    private int choosePreColor() {
        int size = ledNormalColor.length;
        if (currentIndex == 0) {
            currentIndex = size;
        }
        currentIndex = currentIndex - 1;
        currentColor = ledNormalColor[currentIndex];
        return currentIndex;
    }

    /**
     * 选择后一种颜色，并返回对应的索引
     *
     * @return
     */
    private int chooseNextColor() {
        int size = ledNormalColor.length;
        if (currentIndex == size - 1) {
            currentIndex = -1;
        }
        currentIndex = currentIndex + 1;
        currentColor = ledNormalColor[currentIndex];
        return currentIndex;
    }

    /**
     * 初始化固定颜色对应的坐标值
     */
    private void initColorPosition(){

        Point[] points={new Point(-75,1),new Point(110,-2),new Point(102,-88),new Point(57,-120),
                new Point(-69,-131),new Point(-145,-18),new Point(-64,102),new Point(44,103)} ;
        sparseArray = new SparseArray<String>();
        for(int i=0;i<points.length;i++) {

            sparseArray.append(ledNormalColor[i],points[i] );
        }
    }

    /**
     * 获取argb模式的颜色值并转为字符串
     * @param color
     * @return
     */
    private String parseArgb(int color){
        int a = (color >>> 24);
        int r = (color >>  16) & 0xFF;
        int g = (color >>   8) & 0xFF;
        int b = (color)        & 0xFF;
        return String.valueOf(a)+String.valueOf(r)+String.valueOf(g)+String.valueOf(b);
    }

    private String parseRGB(int color){
        int r = (color >>  16) & 0xFF;
        int g = (color >>   8) & 0xFF;
        int b = (color)        & 0xFF;
        return String.valueOf(r)+String.valueOf(g)+String.valueOf(b);
    }


    @Override
    public void onColorChange(int color) {
        txtTitle.setTextColor(color);
        currentColor=color;
        currentRGB=Integer.toHexString(color);
        Log.e("Color", "onColorChange: "+ Color.red(color)+Color.green(color)+Color.blue(color));
        Log.e("Color", "onColorChange:parseArgb "+parseArgb(color) );
        Log.e("Color", "onColorChange:parseRGB "+parseRGB(color) );
        Log.e("Color", "onColorChange: "+Integer.toHexString(color) );//获取十进制字符串表示argb模式的颜色0xFFF3990C-->fff3990c
    }

    /**
     * 获取最终的颜色值ARGB模式的
     * @param progress
     * @return
     */
    private int getChangedColor(int progress){
        String red,green,blue;
        if(progress==0){
            progress=1;
        }
        if(currentRGB==null){
            currentRGB="FF0511FB";
        }
        red=currentRGB.substring(2,4);
        green=currentRGB.substring(4,6);
        blue=currentRGB.substring(6);
        return Color.argb(progress,Integer.parseInt(red,16),Integer.parseInt(green,16),Integer.parseInt(blue,16));
    }

    @Override
    public void onClick(View v) {
        onChooseColor(v);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        currentColor = getChangedColor(progress);
        rainbowPalette.setCenterPaint(currentColor);
        Log.e("Color", "onProgressChanged: "+Integer.toHexString(currentColor) );
        rainbowPalette.invalidate();
        txtTitle.setTextColor(currentColor);

        Log.e("Color", "onProgressChanged: rgb"+ Color.red(currentColor)+Color.green(currentColor)+Color.blue(currentColor));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        return;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        return;
    }
}
