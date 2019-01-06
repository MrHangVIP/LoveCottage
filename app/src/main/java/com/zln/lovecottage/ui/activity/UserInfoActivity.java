package com.zln.lovecottage.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.szh.commonBase.BaseActivity;
import com.szh.commonBase.BaseApplication;
import com.szh.commonBase.api.OkHttpHelp;
import com.szh.commonBase.item.ResultItem;
import com.szh.commonBase.listener.ResponseListener;
import com.szh.commonBase.model.UploadImageModel;
import com.szh.commonBase.ui.widget.CircleImageView;
import com.szh.commonBase.ui.widget.EditDialog;
import com.szh.commonBase.ui.widget.WheelViewDialog;
import com.szh.commonBase.util.Constant;
import com.szh.commonBase.util.FileUtils;
import com.szh.commonBase.util.MyUtil;
import com.szh.commonBase.util.ProgressDialogUtil;
import com.szh.commonBase.util.SelectPhotoTools;
import com.szh.commonBase.util.SpfUtil;
import com.szh.commonBase.util.WheelDateUtil;
import com.szh.commonBase.util.WheelViewDialogUtil;
import com.zln.lovecottage.MyApplication;
import com.zln.lovecottage.R;
import com.zln.lovecottage.item.UserItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息中心页面
 */
public class UserInfoActivity extends BaseActivity {

    private static final String TAG = "MyselfActivity";
    private CircleImageView headImgIV;
    private TextView nickNameTV;
    private TextView cityTV;
    private TextView birthTV;
    private LinearLayout update_pass_layout;
    private Uri photoUri;
    private String headUrl;
    private WheelDateUtil wheelDateUtil;
    private WheelViewDialog timeWheelDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgressDialogUtil.dismissProgressdialog();
            if (msg.what == Constant.IMAGE_UPLOAD_OK) {
                ResultItem object = (ResultItem) msg.obj;
                if (object.getResult().equals("token error")) {
                    toast("登录超时，请重新登录！");
                    tokenError();
                    finish();
                } else {
                    toast("修改成功");
                    try {
                        JSONArray jsonArray = new JSONArray(object.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            headUrl = new Gson().fromJson(jsonArray.get(i).toString(), String.class);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        MyUtil.MyLogE(TAG, e.toString());
                    }
                    ((MyApplication) BaseApplication.getAPPInstance()).getUser().setHeadUrl(headUrl);
                    updateHeadUrl();
                }
            }
            if (msg.what == Constant.IMAGE_UPLOAD_FAIL) {
                toast("网络异常");
            }

        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_user_info_layout);
    }

    @Override
    protected void findViews() {
        setTitle("信息修改");
        headImgIV = (CircleImageView) findViewById(R.id.am_ci_headimg);
        nickNameTV = (TextView) findViewById(R.id.lmi_tv_nickname);
        cityTV = (TextView) findViewById(R.id.lmi_tv_city);
        birthTV = (TextView) findViewById(R.id.lmi_tv_birth);
        update_pass_layout = (LinearLayout) findViewById(R.id.update_pass_layout);
    }

    @Override
    protected void initData() {
        UserItem user = ((MyApplication) BaseApplication.getAPPInstance()).getUser();
        if (user != null) {
            if (!TextUtils.isEmpty(user.getNickName())) {
                nickNameTV.setText(user.getNickName());
            }
            if (!TextUtils.isEmpty(user.getCity())) {
                cityTV.setText(user.getCity());
            }
            if (!TextUtils.isEmpty(user.getBirthday())) {
                birthTV.setText(user.getBirthday());
            }
            if (!"".equals(user.getHeadUrl())) {
                Glide.with(this)
                        .load(Constant.DEFAULT_URL + Constant.IMAGE_URL + user.getHeadUrl())
                        .placeholder(R.drawable.img_loading_2)
                        .into(headImgIV);
            }
        }
    }

    private void initTimeDialog() {
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params1.weight = 1;
        params1.gravity = Gravity.RIGHT;
        wheelDateUtil = new WheelDateUtil(mContext);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(MyUtil.toDip(50), ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params3.weight = 1;
        params3.gravity = Gravity.LEFT;
        wheelDateUtil.setCyclic(true);
        wheelDateUtil.textSize = 17;
        LinkedHashMap<View, LinearLayout.LayoutParams> map = new LinkedHashMap<>();
        map.put(wheelDateUtil.getWv_year(), params1);
        map.put(wheelDateUtil.getWv_month(), params2);
        map.put(wheelDateUtil.getWv_day(), params3);
        timeWheelDialog = WheelViewDialogUtil.showWheelViewDialog(UserInfoActivity.this, "请选择生日", new WheelViewDialog.DialogSubmitListener() {
            @Override
            public void onSubmitClick(View v) {
                String finishTime = wheelDateUtil.getYear()
                        + "-" + wheelDateUtil.getMonth() + "-" + wheelDateUtil.getDay();
                birthTV.setText(finishTime);
                timeWheelDialog.dismiss();
                UserItem user = ((MyApplication) BaseApplication.getAPPInstance()).getUser();
                user.setBirthday(finishTime);
                updateModel(user);
            }
        }, map);
    }

    @Override
    protected void setListener() {
        headImgIV.setOnClickListener(this);

        ((LinearLayout) nickNameTV.getParent()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyUtil.MyLogE(TAG, "click");
                        dialogShow(nickNameTV.getText().toString(), "输入昵称", nickNameTV);
                    }
                });

        ((LinearLayout) cityTV.getParent()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyUtil.MyLogE(TAG, "click");
                        dialogShow(cityTV.getText().toString(), "输入城市", cityTV);
                    }
                });

        ((LinearLayout) birthTV.getParent()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (timeWheelDialog == null) {
                            initTimeDialog();
                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            if (!TextUtils.isEmpty(((MyApplication) BaseApplication.getAPPInstance()).getUser().getBirthday())) {
                                try {
                                    String birth = ((MyApplication) BaseApplication.getAPPInstance()).getUser().getBirthday();
                                    year = Integer.parseInt(birth.split("-")[0]);
                                    month = Integer.parseInt(birth.split("-")[1]) - 1;
                                    day = Integer.parseInt(birth.split("-")[2]);
                                } catch (Exception e) {

                                }
                            }
                            wheelDateUtil.setPicker(year, month, day);
                        }
                        timeWheelDialog.show();
                    }
                });

        update_pass_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToNext(UpdatePassActivity.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SpfUtil.getBoolean(Constant.IS_LOGIN, false)) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.am_ci_headimg:
                changePhoto();
                break;
        }

    }

    private void dialogShow(String content, String title, TextView view) {
        new MyEditDialog(this, content, view).setTitle(title).show();
    }


    /**
     * 点击头像按钮
     */
    private void changePhoto() {
        if (!FileUtils.isSdCardExist()) {
            toast("没有找到SD卡，请检查SD卡是否存在");
            return;
        }
        try {
            photoUri = FileUtils.getUriByFileDirAndFileName(Constant.SystemPicture.SAVE_DIRECTORY, Constant.SystemPicture.SAVE_PIC_NAME);
        } catch (IOException e) {
            toast("创建文件失败");
            return;
        }
        SelectPhotoTools.openDialog(this, photoUri);
    }

    private class MyEditDialog extends EditDialog {

        TextView view;

        public MyEditDialog(Context context, String content, TextView view) {
            super(context, content);
            this.view = view;
        }

        @Override
        protected void confirmListener() {
            view.setText(editContent);
            UserItem user = ((MyApplication) BaseApplication.getAPPInstance()).getUser();

            switch (view.getId()) {

                case R.id.lmi_tv_nickname:
                    user.setNickName(editContent);
                    break;

                case R.id.lmi_tv_city:
                    user.setCity(editContent);
                    break;

                case R.id.lmi_tv_birth:
                    user.setBirthday(editContent);
                    break;
            }
            updateModel(user);
        }
    }

    private void updateModel(UserItem userItem) {
        ProgressDialogUtil.showProgressDialog(UserInfoActivity.this, true);
        Map<String, String> params = new HashMap<>();
        params.put("userPhone", userItem.getUserPhone());
        params.put("nickName", userItem.getNickName());
        params.put("city", userItem.getCity());
        params.put("birthday", userItem.getBirthday());
        params.put("token", SpfUtil.getString(Constant.TOKEN, ""));
        OkHttpHelp<ResultItem> okHttpHelp = OkHttpHelp.getInstance();
        okHttpHelp.httpRequest("", Constant.UPDATE_USER_URL, params, new ResponseListener<ResultItem>() {
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
                    toast("修改成功");
                    getUserData();
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

    private void getUserData() {
        Map<String, String> params = new HashMap<>();
        params.put("token", SpfUtil.getString(Constant.TOKEN, ""));
        params.put("userPhone", SpfUtil.getString(Constant.LOGIN_USERPHONE, ""));
        OkHttpHelp<ResultItem> httpHelp = OkHttpHelp.getInstance();
        httpHelp.httpRequest("", Constant.GET_USER_URL, params, new ResponseListener<ResultItem>() {
                    @Override
                    public void onSuccess(ResultItem object) {
                        if ("fail".equals(object.getResult())) {
                            if ("token error".equals(object.getData())) {
                                toast("token失效,请重新登录");
                                tokenError();
                            }
                        } else {
                            JSONObject userJson = null;
                            try {
                                userJson = new JSONObject(object.getData());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            UserItem userItem = (new Gson()).fromJson(userJson.toString(), UserItem.class);
                            UserItem mUser = userItem;
                            ((MyApplication) BaseApplication.getAPPInstance()).setUser(userItem);
                        }
                    }

                    @Override
                    public void onFailed(String message) {
                        ProgressDialogUtil.dismissProgressdialog();
                    }

                    @Override
                    public Class getEntityClass() {
                        return ResultItem.class;
                    }
                }

        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.SystemPicture.PHOTO_REQUEST_TAKEPHOTO: // 拍照
                SelectPhotoTools.startPhotoZoom(UserInfoActivity.this, null, photoUri, 300);
                break;
            case Constant.SystemPicture.PHOTO_REQUEST_GALLERY://相册获取
                if (data == null)
                    return;
                SelectPhotoTools.startPhotoZoom(UserInfoActivity.this, null, data.getData(), 300);
                break;
            case Constant.SystemPicture.PHOTO_REQUEST_CUT:  //接收处理返回的图片结果
                if (data == null)
                    return;
                File file = FileUtils.getFileByUri(UserInfoActivity.this, photoUri);
                MyUtil.MyLogE(TAG, file.toString());
                Bitmap bit = data.getExtras().getParcelable("data");
                headImgIV.setImageBitmap(bit);
                try {
                    bit.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    MyUtil.MyLogE(TAG, e.toString());
                }
                //读取本地
//                bit= BitmapFactory.decodeFile(file.getAbsolutePath());
//                toast("设置成功");

                imageUpload(file.getAbsolutePath());
                break;
        }
    }

    private void imageUpload(String path) {
        ProgressDialogUtil.showProgressDialog(this, true);
        List<String> pathList = new ArrayList<String>();
        pathList.add(path);
        UploadImageModel uploadImageModel = new UploadImageModel(pathList, mHandler);
        uploadImageModel.imageUpload();
    }

    private void updateHeadUrl() {
        Map<String, String> params = new HashMap<>();
        params.put("token", SpfUtil.getString(Constant.TOKEN, ""));
        params.put("userPhone", SpfUtil.getString(Constant.LOGIN_USERPHONE, ""));
        params.put("headUrl", headUrl);
        OkHttpHelp<ResultItem> httpHelp = OkHttpHelp.getInstance();
        httpHelp.httpRequest("", Constant.UPDATE_HEAD_URL, params, new ResponseListener<ResultItem>() {
            @Override
            public void onSuccess(ResultItem object) {
                if ("fail".equals(object.getResult())) {
                    if ("token error".equals(object.getData())) {
                        toast("token失效,请重新登录");
                        tokenError();
                        finish();
                    }
                } else {
                    MyUtil.MyLogE(TAG, "插入成功！");
                }
            }

            @Override
            public void onFailed(String message) {
                MyUtil.MyLogE(TAG, "连接失败");
            }

            @Override
            public Class<ResultItem> getEntityClass() {
                return ResultItem.class;
            }
        });
    }

}
