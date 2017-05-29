package com.ustc.gjqapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ustc.gjqapp.R;

/**
 * Created by MaLei on 2017/5/29.
 * Email:ml1995@mail.ustc.edu.cn
 * 改变交通查询起点和终点
 */

public class ChangeRouteActivity extends Activity implements View.OnClickListener {

    private Button mSure;
    private Button mCancel;
    private  EditText mStartNodeStr;
    private  EditText mEndNodeStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_route);

        mStartNodeStr = (EditText) findViewById(R.id.startNodeStr);
        mEndNodeStr = (EditText)findViewById(R.id.endNodeStr);
        mSure = (Button) findViewById(R.id.sure);
        mSure.setOnClickListener(this);
        mCancel = (Button)findViewById(R.id.cancel);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sure:
                sure();
                break;
            case R.id.cancel:
                finish("文荟人才公寓", "中国科学技术大学苏州研究院");
                break;
            default:
                break;
        }
    }

    private void finish(String startNodeStr, String endNodeStr) {
        Intent mIntent = new Intent();
        mIntent.putExtra("startNodeStr", startNodeStr);
        mIntent.putExtra("endNodeStr", endNodeStr);
        this.setResult(1, mIntent);
        finish();
    }

    private void sure() {
        String startNodeStr = mStartNodeStr.getText().toString();
        String endNodeStr = mEndNodeStr.getText().toString();
        if (TextUtils.isEmpty(startNodeStr) || TextUtils.isEmpty(endNodeStr)){
            Toast.makeText(this,"输入不能为空，请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
        finish(startNodeStr, endNodeStr);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish("文荟人才公寓", "中国科学技术大学苏州研究院");
    }
}
