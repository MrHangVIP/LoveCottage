package com.zln.lovecottage.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.szh.commonBase.BaseActivity;
import com.zln.lovecottage.R;


/**
 * @discription 忘记密码activity
 * @autor songzhihang
 * @time 2017/10/13  下午5:52
 **/
public class ForgetPassActivity extends BaseActivity {


    private EditText phoneNumberET, checkCodeET;
    private Button nextBT;


    @Override
    protected void setView() {
        setContentView(R.layout.activity_forget_pass_layout);
    }

    @Override
    protected void findViews() {
        setTitle("忘记密码");
        phoneNumberET = (EditText) findViewById(R.id.et_username);
        checkCodeET = (EditText) findViewById(R.id.et_check_code);
        nextBT = (Button) findViewById(R.id.bt_next);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        nextBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_send:

                break;

            case R.id.bt_next:
                jumpToNext(UpdatePassActivity.class);
                finish();
            default:
                break;
        }
    }

}

