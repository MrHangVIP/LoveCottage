package com.zln.lovecottage.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.szh.commonBase.BaseApplication;
import com.szh.commonBase.BaseFragment;
import com.szh.commonBase.api.OkHttpHelp;
import com.szh.commonBase.item.ResultItem;
import com.szh.commonBase.listener.ResponseListener;
import com.szh.commonBase.model.UploadImageModel;
import com.szh.commonBase.ui.widget.CircleImageView;
import com.szh.commonBase.util.Constant;
import com.szh.commonBase.util.FileUtils;
import com.szh.commonBase.util.MyUtil;
import com.szh.commonBase.util.ProgressDialogUtil;
import com.szh.commonBase.util.SelectPhotoTools;
import com.szh.commonBase.util.SpfUtil;
import com.zln.lovecottage.MyApplication;
import com.zln.lovecottage.R;
import com.zln.lovecottage.item.UserItem;
import com.zln.lovecottage.ui.activity.AboutUsActivity;
import com.zln.lovecottage.ui.activity.LoginActivity;
import com.zln.lovecottage.ui.activity.UserInfoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Songzhihang on 2017/10/6.
 * 用户中心fragment
 */
public class UserCenterFragment extends BaseFragment {
    private static final String TAG = "UserCenterFragment";

    private CircleImageView ful_iv_head;
    private TextView ful_tv_login_and_regist;
    private TextView ful_tv_nickname;
    private LinearLayout asl_ll_replaceuser;
    private LinearLayout asl_ll_aboutus;
    private LinearLayout asl_ll_update;
    private TextView ful_tv_score;

    private Uri photoUri;
    private String headUrl;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgressDialogUtil.dismissProgressdialog();
            if(msg.what== Constant.IMAGE_UPLOAD_OK){
                ResultItem object=(ResultItem) msg.obj;
                if (object.getResult().equals("token error")) {
                    toast("登录超时，请重新登录！");
                    tokenError();
                } else {
                    toast("修改成功");
                    try {
                        JSONArray jsonArray=new JSONArray(object.getData());
                        for(int i=0;i<jsonArray.length();i++){
                            headUrl=  new Gson().fromJson(jsonArray.get(i).toString(), String.class);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        MyUtil.MyLogE(TAG,e.toString());
                    }
                    ((MyApplication)BaseApplication.getAPPInstance()).getUser().setHeadUrl(headUrl);
                    updateHeadUrl();
                }
            }
            if(msg.what==Constant.IMAGE_UPLOAD_FAIL){
                toast("网络异常");
            }

        }
    };

    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_usercenter_layout, container, false);
    }

    @Override
    protected void initView(View view) {
        ful_iv_head = (CircleImageView) contentView.findViewById(R.id.ful_iv_head);
        ful_tv_login_and_regist = (TextView) contentView.findViewById(R.id.ful_tv_login_and_regist);
        ful_tv_nickname = (TextView) contentView.findViewById(R.id.ful_tv_nickname);
        ful_tv_score = (TextView) contentView.findViewById(R.id.ful_tv_score);
        asl_ll_replaceuser = (LinearLayout) contentView.findViewById(R.id.asl_ll_replaceuser);
        asl_ll_aboutus = (LinearLayout) contentView.findViewById(R.id.asl_ll_aboutus);
        asl_ll_update = (LinearLayout) contentView.findViewById(R.id.asl_ll_update);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        ful_iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SpfUtil.getBoolean(Constant.IS_LOGIN,false)){
                    jumpToNext(UserInfoActivity.class);
                }
            }
        });

        ful_tv_login_and_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToNext(LoginActivity.class);
            }
        });
        asl_ll_replaceuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpfUtil.clearAll();
                ((MyApplication)BaseApplication.getAPPInstance()).setUser(null);
                jumpToNext(LoginActivity.class);
            }
        });

        ful_tv_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                jumpToNext(ScoreRecordActivity.class);
            }
        });

        asl_ll_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("当前已经是最新版本！");
            }
        });

        asl_ll_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToNext(AboutUsActivity.class);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (SpfUtil.getBoolean(Constant.IS_LOGIN, false)) {//已登陆获取用户信息
            setSelfData();
        } else {
            unLogin();
        }
    }

    private void unLogin() {
        ful_iv_head.setImageResource(R.drawable.img_loading_2);
        ful_tv_nickname.setVisibility(View.GONE);
        ful_tv_score.setVisibility(View.GONE);
        ful_tv_login_and_regist.setVisibility(View.VISIBLE);
    }

    private void updateData(UserItem user) {
        ful_tv_nickname.setText(user.getNickName());
        if (!TextUtils.isEmpty(user.getHeadUrl())) {
            Glide.with(context)
                    .load(Constant.DEFAULT_URL + Constant.IMAGE_URL + user.getHeadUrl())
                    .placeholder(R.drawable.img_user_default)
                    .into(ful_iv_head);
        }
        ful_tv_nickname.setVisibility(View.VISIBLE);
        ful_tv_score.setVisibility(View.VISIBLE);
//        ful_tv_score.setText(user.getScore() + "");
        ful_tv_login_and_regist.setVisibility(View.GONE);
    }

    private void setSelfData() {
        ProgressDialogUtil.showProgressDialog(context, true);
        if (BaseApplication.getAPPInstance().getUser() != null) {
            UserItem mUser = (UserItem) BaseApplication.getAPPInstance().getUser();
            updateData(mUser);
            ProgressDialogUtil.dismissProgressdialog();
        }
        getUserData();
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
        SelectPhotoTools.openDialog(getActivity(), photoUri);
    }

    private void getUserData() {
//        Map<String, String> params = new HashMap<>();
//        params.put("token", SpfUtil.getString(Constant.TOKEN, ""));
//        params.put("userPhone", SpfUtil.getString(Constant.LOGIN_USERPHONE, ""));
//        OkHttpHelp<ResultItem> httpHelp = OkHttpHelp.getInstance();
//        httpHelp.httpRequest("", Constant.GET_USER_URL, params, new ResponseListener<ResultItem>() {
//                    @Override
//                    public void onSuccess(ResultItem object) {
//                        if ("fail".equals(object.getResult())) {
//                            if ("token error".equals(object.getData())) {
//                                toast("token失效,请重新登录");
//                                tokenError();
//                            }
//                        } else {
//                            JSONObject userJson = null;
//                            try {
//                                userJson = new JSONObject(object.getData());
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            UserItem userItem = (new Gson()).fromJson(userJson.toString(), UserItem.class);
//                            UserItem mUser = userItem;
//                            updateData(mUser);
//                            ProgressDialogUtil.dismissProgressdialog();
//                            BaseApplication.getAPPInstance().setmUser(userItem);
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(String message) {
//                        ProgressDialogUtil.dismissProgressdialog();
//                        tokenError();
//                    }
//
//                    @Override
//                    public Class getEntityClass() {
//                        return ResultItem.class;
//                    }
//                }
//
//        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.SystemPicture.PHOTO_REQUEST_TAKEPHOTO: // 拍照
                SelectPhotoTools.startPhotoZoom(getActivity(), null, photoUri, 300);
                break;
            case Constant.SystemPicture.PHOTO_REQUEST_GALLERY://相册获取
                if (data == null)
                    return;
                SelectPhotoTools.startPhotoZoom(getActivity(), null, data.getData(), 300);
                break;
            case Constant.SystemPicture.PHOTO_REQUEST_CUT:  //接收处理返回的图片结果
                if (data == null)
                    return;
                File file = FileUtils.getFileByUri(getActivity(), photoUri);
                MyUtil.MyLogE(TAG, file.toString());
                Bitmap bit = data.getExtras().getParcelable("data");
                ful_iv_head.setImageBitmap(bit);
                try {
                    bit.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    MyUtil.MyLogE(TAG,e.toString());
                }
                imageUpload(file.getAbsolutePath());
                break;
        }
    }

    private void imageUpload(String path){
        ProgressDialogUtil.showProgressDialog(context,true);
        List<String> pathList=new ArrayList<String>();
        pathList.add(path);
        UploadImageModel uploadImageModel=new UploadImageModel(pathList,mHandler);
        uploadImageModel.imageUpload();
    }

    private void updateHeadUrl(){
        Map<String,String> params=new HashMap<>();
        params.put("token",SpfUtil.getString(Constant.TOKEN,""));
        params.put("userPhone",SpfUtil.getString(Constant.LOGIN_USERPHONE,""));
        params.put("headUrl",headUrl);
        OkHttpHelp<ResultItem> httpHelp=OkHttpHelp.getInstance();
        httpHelp.httpRequest("", Constant.UPDATE_HEAD_URL, params, new ResponseListener<ResultItem>() {
            @Override
            public void onSuccess(ResultItem object) {
                if("fail".equals(object.getResult())){
                    if("token error".equals(object.getData())){
                        toast("token失效,请重新登录");
                        tokenError();
                    }
                }else{
                    MyUtil.MyLogE(TAG,"插入成功！");
                }
            }
            @Override
            public void onFailed(String message) {
                MyUtil.MyLogE(TAG,"连接失败");
            }

            @Override
            public Class<ResultItem> getEntityClass() {
                return ResultItem.class;
            }
        });
    }
}
