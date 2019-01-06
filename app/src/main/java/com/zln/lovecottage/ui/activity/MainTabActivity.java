package com.zln.lovecottage.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.szh.commonBase.BaseActivity;
import com.szh.commonBase.util.Constant;
import com.szh.commonBase.util.SpfUtil;
import com.zln.lovecottage.R;
import com.zln.lovecottage.ui.fragment.HomeFragment;
import com.zln.lovecottage.ui.fragment.UserCenterFragment;


/**
 * Created by Songzhihang on 2017/10/6.
 * 首页tabActivity
 */
public class MainTabActivity extends BaseActivity {
    private static final String TAG = "MainTabActivity";
    private FrameLayout contentFL;
    private LinearLayout tabLL;
    private LinearLayout aml_ll_left;
    private LinearLayout aml_ll_right;
    private ImageView aml_iv_left;
    private ImageView aml_iv_right;
    private ImageView addIV;
//    private FloatingActionMenu centerBottomMenu;
    private View aml_translate_bg;
    private HomeFragment homeFragment;
    private UserCenterFragment userCenterFragment;
    private long curtime;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_maintab_layout);
    }

    @Override
    protected void findViews() {
        contentFL = (FrameLayout) findViewById(R.id.aml_fl_content);
        tabLL = (LinearLayout) findViewById(R.id.aml_ll_tab_layout);
        aml_ll_left = (LinearLayout) findViewById(R.id.aml_ll_left);
        aml_ll_right = (LinearLayout) findViewById(R.id.aml_ll_right);
        aml_iv_left = (ImageView) findViewById(R.id.aml_iv_left);
        aml_iv_right = (ImageView) findViewById(R.id.aml_iv_right);
        addIV = (ImageView) findViewById(R.id.aml_iv_add);
        aml_translate_bg = (View) findViewById(R.id.aml_translate_bg);
    }

    @Override
    protected void initData() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        replaceFragment(homeFragment);
    }

    @Override
    protected void setListener() {
        aml_ll_right.setOnClickListener(this);
        aml_ll_left.setOnClickListener(this);
        addIV.setOnClickListener(this);
        aml_translate_bg.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SpfUtil.getBoolean(Constant.IS_LOGIN, false)) {//已登陆获取用户信息
//            getUserData();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.aml_ll_left) {
            if (homeFragment == null) {
                homeFragment = new HomeFragment();
            }
            replaceIcon(0);
            replaceFragment(homeFragment);
        }
        if (v.getId() == R.id.aml_ll_right) {
            if (userCenterFragment == null) {
                userCenterFragment = new UserCenterFragment();
            }
            replaceIcon(1);
            replaceFragment(userCenterFragment);
        }

        if (v.getId() == R.id.aml_iv_add) {
//            showFloatActionMenu();
        }
        if (v.getId() == R.id.aml_translate_bg) {
//            closeWindow();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if(!fragment.isAdded()){
            transaction.add(R.id.aml_fl_content, fragment);
        }
        if(fragment ==homeFragment){
            transaction.show(homeFragment);
            if(userCenterFragment!=null && userCenterFragment.isAdded()){
                transaction.hide(userCenterFragment);
            }
        }
        if(fragment ==userCenterFragment){
            transaction.show(userCenterFragment);
            if(homeFragment!=null && homeFragment.isAdded()){
                transaction.hide(homeFragment);
            }
        }
        transaction.commit();
    }

//    private void showFloatActionMenu() {
//        //否则显示弹出框
//        if (centerBottomMenu == null) {
//            MenuAnimationHandler2 animationHandler = new DefaultAnimationHandler2();
//            List<FloatingActionMenu.Item> subActionItems = new ArrayList<>();
//            for (int i = 0; i < 4; i++) {
//                ImageView rlIcon = new ImageView(mContext);
//                rlIcon.setImageResource(R.drawable.icon_publish);
//                subActionItems.add(new FloatingActionMenu.Item(rlIcon, MyUtil.toDip(38), MyUtil.toDip(38)));
//                rlIcon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(SpfUtil.getBoolean(Constant.IS_LOGIN, false)){
//                            jumpToNext(CreateQuestionActivity.class);
//                            closeWindow();
//                        }else{
//                            toast("请先登陆");
//                            jumpToNext(LoginActivity.class);
//                            closeWindow();
//                        }
//                    }
//                });
//            }
//            centerBottomMenu = new FloatingActionMenu(addIV, 200, 340, MyUtil.toDip(100),
//                    subActionItems, animationHandler, true, null, false);
//
//            centerBottomMenu
//                    .setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
//
//                        @Override
//                        public void onMenuOpened(FloatingActionMenu menu) {
//                            aml_translate_bg.setVisibility(View.VISIBLE);
//                            addIV.startAnimation(getRotateAnim(0f, 45f));
//                        }
//
//                        @Override
//                        public void onMenuClosed(FloatingActionMenu menu) {
//                            aml_translate_bg.setVisibility(View.GONE);
//                            addIV.startAnimation(getRotateAnim(45f, 0f));
//                        }
//                    });
//        }
//
//        if (centerBottomMenu != null && centerBottomMenu.isOpen()) {
//            centerBottomMenu.close(true);
//        } else if (centerBottomMenu != null) {
//            centerBottomMenu.open(true);
//        }
//    }
//
//    private void closeWindow() {
//        if (centerBottomMenu != null && centerBottomMenu.isOpen()) {
//            centerBottomMenu.close(true);
//            aml_translate_bg.setVisibility(View.GONE);
//        }
//    }
//
//    protected RotateAnimation getRotateAnim(Float from, Float to) {
//        final RotateAnimation animation = new RotateAnimation(from, to,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);
//        animation.setDuration(300);// 设置动画持续时间
//        animation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
//        animation.setStartOffset(0);
//        return animation;
//    }

    private void replaceIcon(int positon) {
        if (positon == 0) {
            aml_iv_left.setImageResource(R.drawable.icon_shouye_press);
            aml_iv_right.setImageResource(R.drawable.icon_mime_normal);
        } else {
            aml_iv_left.setImageResource(R.drawable.icon_shouye_normal);
            aml_iv_right.setImageResource(R.drawable.icon_mime_press);
        }
    }

//    private void getUserData() {
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
//                            ProgressDialogUtil.dismissProgressdialog();
//                            BaseApplication.getAPPInstance().setmUser(userItem);
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(String message) {
//                        ProgressDialogUtil.dismissProgressdialog();
//                    }
//
//                    @Override
//                    public Class getEntityClass() {
//                        return ResultItem.class;
//                    }
//                }
//
//        );
//    }

    @Override
    public void onBackPressed() {
        long time= System.currentTimeMillis();
        if(time-curtime<=3000){//3秒内重复点击退出
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }else{
            curtime=time;
            toast("再点击一次退出");
        }
//        super.onBackPressed();
    }
}
