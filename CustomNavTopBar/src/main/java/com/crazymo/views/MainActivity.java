package com.crazymo.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.crazymo.views.widget.NavTopBar;

/**
 * auther: Crazy.Mo
 * Date: 2016/12/26
 * Time:18:20
 * Des:
 */
public class MainActivity extends AppCompatActivity implements NavTopBar.NavOnClickListener{
    private NavTopBar navTopBar;
    private ImageView leftBtn,rightBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews(){
        navTopBar= (NavTopBar) findViewById(R.id.nav_top_main);
        navTopBar.setBarImageDisplay(true,true);//设置显示左右按钮
        navTopBar.setNavOnClickListener(this);//注册监听
        setTitle("ByCrazyMo_");
    }

    private void setTitle(String title){
        navTopBar.setTitle(title);
    }

    @Override
    public void leftOnClickListener() {
        Toast.makeText(this,"Clicked on the Left",Toast.LENGTH_LONG).show();
    }

    @Override
    public void rightOnClickListener() {
        Toast.makeText(this,"Clicked on the Right",Toast.LENGTH_LONG).show();
    }
}
