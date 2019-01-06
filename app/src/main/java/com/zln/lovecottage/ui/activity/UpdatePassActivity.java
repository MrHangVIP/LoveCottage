package com.zln.lovecottage.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.szh.commonBase.BaseActivity;
import com.szh.commonBase.BaseApplication;
import com.szh.commonBase.api.OkHttpHelp;
import com.szh.commonBase.item.ResultItem;
import com.szh.commonBase.listener.ResponseListener;
import com.szh.commonBase.util.Constant;
import com.szh.commonBase.util.ProgressDialogUtil;
import com.szh.commonBase.util.SpfUtil;
import com.zln.lovecottage.MyApplication;
import com.zln.lovecottage.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class UpdatePassActivity extends BaseActivity {


    private EditText newPassET, newPassAgainET;
    private Button saveBT;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_update_pass);
    }


    @Override
    protected void findViews() {
        setTitle("忘记密码");
        newPassET = (EditText) findViewById(R.id.et_new_pass);
        newPassAgainET = (EditText) findViewById(R.id.et_new_pass_again);
        saveBT = (Button) findViewById(R.id.bt_save);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        saveBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                updatePassWord();
                break;
        }
    }


    private void updatePassWord() {
        String newPass = newPassET.getText().toString();
        String newPassAgain = newPassAgainET.getText().toString();
        if (newPass.length() < 8 || newPassAgain.length() < 8) {
            toast("密码长度不能低于8位！");
            return;
        }
        if (!TextUtils.equals(newPass, newPassAgain)) {
            toast("2次密码不一致！");
            return;
        }
        if (TextUtils.equals(newPass, ((MyApplication) BaseApplication.getAPPInstance()).getUser().getUserPass())) {
            toast("密码与原密码相同，无需修改！");
            return;
        }
        ProgressDialogUtil.showProgressDialog(this, true);
        Map<String, String> params = new HashMap<>();
        params.put("userPhone", ((MyApplication) BaseApplication.getAPPInstance()).getUser().getUserPhone());
        params.put("password", newPass);
        params.put("token", SpfUtil.getString(Constant.TOKEN, ""));
        OkHttpHelp<ResultItem> okHttpHelp = OkHttpHelp.getInstance();
        okHttpHelp.httpRequest("", Constant.UPDATE_PASS_WORD, params, new ResponseListener<ResultItem>() {
            @Override
            public void onSuccess(ResultItem object) {
                ProgressDialogUtil.dismissProgressdialog();
                if (object.getResult().equals("fail")) {
                    if (object.getData().equals("token error")) {
                        toast("token失效,请重新登录");
                        tokenError();
                    } else {
                        toast("更新失败");
                    }
                } else {
                    toast("修改成功,请重新登陆");
                    //重新登陆
                    tokenError();
                }
            }

            @Override
            public void onFailed(String message) {
                ProgressDialogUtil.dismissProgressdialog();
                toast("网络错误");
            }

            @Override
            public Class<ResultItem> getEntityClass() {
                return ResultItem.class;
            }
        });

    }

}

