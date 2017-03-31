package com.crazymo.training;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.crazymo.training.widget.EnhancedEditText;

public class MainActivity extends Activity implements EnhancedEditText.OnRightDrawableChanged {
    private final String TAG="EnhanceEditText";
    private Button btn;
    private EnhancedEditText clearEditText;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init(){
        getViews();
        setBtnClickListener();
        //clearEditText.setRightIco(getDrawable(R.mipmap.ic_user));
        clearEditText.setRightChangedListener(this);
    }
    private void getViews(){
        btn= (Button) findViewById(R.id.test_btn);
        clearEditText= (EnhancedEditText) findViewById(R.id.cleat_edt_user);
        imageView= (ImageView) findViewById(R.id.show_pwd);
    }
    private void setBtnClickListener(){
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(clearEditText.getText())){
                    //设置晃动
                    clearEditText.setShakeAnimation(null);
                    //设置提示
                    Toast.makeText(MainActivity.this,"用户名不能为空",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    //设置密码可见
                    clearEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //clearEditText.setRightIco(getDrawable(R.mipmap.ic_user));
                    //clearEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    public void onRightDisplay() {
        imageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRightInvisible() {
        imageView.setVisibility(View.GONE);
    }
}
