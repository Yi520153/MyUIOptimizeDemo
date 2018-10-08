package com.lcl.ui.viewstubdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewStub viewStub;
    private TextView hintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    /**
     * 初始化
     */
    private void init() {
        viewStub = (ViewStub) findViewById(R.id.viewStubTest);
        Button btn_show = (Button) findViewById(R.id.btnShowView);
        Button btn_hide = (Button) findViewById(R.id.btnHideView);
        Button btn_change = (Button) findViewById(R.id.btnChangeHint);
        btn_show.setOnClickListener(this);
        btn_hide.setOnClickListener(this);
        btn_change.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowView:
                // inflate 方法只能被调用一次，因为调用后viewStub对象就被移除了视图树；
                // 所以，如果此时再次点击显示按钮，就会崩溃，错误信息：ViewStub must have a non-null ViewGroup viewParent；
                // 所以使用try catch ,当此处发现exception 的时候，在catch中使用setVisibility()重新显示

                // 也可以通过 viewStubContent 是否为空判断进行逻辑控制,如下
                //  if(viewStubContent==null){
                //       viewStubContent=viewStub.inflate();
                //  }else {
                //        viewStubContent.setVisibility(View.VISIBLE);
                //  }
                try {
                    View viewStubContent = viewStub.inflate(); //inflate 方法只能被调用一次，

                    hintText = (TextView) viewStubContent.findViewById(R.id.tvContent);
                    hintText.setText("没有相关数据，请刷新");
                } catch (Exception e) {
                    viewStub.setVisibility(View.VISIBLE);
                } finally {
                    hintText.setText("没有相关数据，请刷新");
                }
                break;
            case R.id.btnHideView: //如果显示
                viewStub.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnChangeHint:
                if (hintText != null) {
                    hintText.setText("网络异常，无法刷新，请检查网络");
                }
                break;
        }
    }
}
