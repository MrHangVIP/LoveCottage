package com.zln.lovecottage.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.szh.commonBase.BaseApplication;
import com.szh.commonBase.BaseFragment;
import com.szh.commonBase.api.OkHttpHelp;
import com.szh.commonBase.item.ResultItem;
import com.szh.commonBase.listener.ResponseListener;
import com.szh.commonBase.util.Constant;
import com.szh.commonBase.util.ProgressDialogUtil;
import com.szh.commonBase.util.SpfUtil;
import com.szh.commonBase.util.StringUtils;
import com.zln.lovecottage.MyApplication;
import com.zln.lovecottage.R;
import com.zln.lovecottage.item.UserItem;
import com.zln.lovecottage.ui.activity.ForgetPassActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by moram on 2016/9/21.
 */
public class LoginFragment extends BaseFragment {

    private static final String TAG = "LoginFragment";
    private View view;
    private EditText phoneNumberET;
    private EditText passwordET;
    private TextView frogetPassTV;
    private Button loginBT;


    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        phoneNumberET = (EditText) view.findViewById(R.id.et_username);
        passwordET = (EditText) view.findViewById(R.id.et_password);
        frogetPassTV = (TextView) view.findViewById(R.id.tv_forget_pass);
        loginBT = (Button) view.findViewById(R.id.bt_login);
    }

    @Override
    protected void initEvent() {
        frogetPassTV.setOnClickListener(this);
        loginBT.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_login:
                String username = phoneNumberET.getText().toString();
                String userpass = passwordET.getText().toString();
                if (!StringUtils.isMobile(username)) {
                    toast("请输入正确的手机号");
                    loginFail();
                    break;
                }

                if (userpass.length() < 8) {
                    toast("密码长度不能地域8位");
                    loginFail();
                    break;
                }
                ProgressDialogUtil.showProgressDialog(getActivity(), true);
                Map<String, String> params = new HashMap<>();
                params.put("userPhone", username);
                params.put("userPass", userpass);
                params.put("MAC", Constant.MacAddress);
                OkHttpHelp<ResultItem> httpHelp = OkHttpHelp.getInstance();
                httpHelp.httpRequest("post", Constant.LOGIN_URL, params, new ResponseListener<ResultItem>() {
                    @Override
                    public void onSuccess(ResultItem object) {
                        ProgressDialogUtil.dismissProgressdialog();
                        if (!object.getResult().equals("fail")) {
                            toast("登录成功！");
                            SpfUtil.saveString(Constant.TOKEN, object.getResult());
                            JSONObject userJson = null;
                            try {
                                userJson = new JSONObject(object.getData());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            UserItem userItem = (new Gson()).fromJson(userJson.toString(), UserItem.class);
                            ((MyApplication) BaseApplication.getAPPInstance()).setUser(userItem);
                            SpfUtil.saveBoolean(Constant.IS_LOGIN, true);
                            SpfUtil.saveString(Constant.LOGIN_USERPHONE, userItem.getUserPhone());
//                            getActivity().setResult(SettingActivity.RESULT_LOGIN);
//                            ((BaseActivity)getActivity()).goToNext(SettingActivity.class);
                        } else {
                            toast("用户名或密码错误");
                            loginFail();
                        }
                    }

                    @Override
                    public void onFailed(String message) {
                        ProgressDialogUtil.dismissProgressdialog();
                    }

                    @Override
                    public Class<ResultItem> getEntityClass() {
                        return ResultItem.class;
                    }
                });

                break;


            case R.id.tv_forget_pass:
                jumpToNext(ForgetPassActivity.class);
                getActivity().finish();
                break;
        }
    }

    private void loginFail() {
        phoneNumberET.setText("");
        phoneNumberET.requestFocus();
        passwordET.setText("");
    }

}
