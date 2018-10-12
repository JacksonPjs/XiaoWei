package com.xiaowei.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.blibrary.banner.Banner;
import com.example.blibrary.banner.BannerIndicator;
import com.example.blibrary.utils.T;
import com.xiaowei.ui.Adapter.HomeAdapter;
import com.xiaowei.MyApplication;
import com.xiaowei.R;
import com.xiaowei.bean.ProductListBean;
import com.xiaowei.net.NetWorks;
import com.xiaowei.ui.Adapter.itemTextviewAdapter;
import com.xiaowei.utils.AppUtils;
import com.xiaowei.utils.SharedPreferencesUtils;
import com.xiaowei.widget.Dialog.AdvertDialog;
import com.xiaowei.widget.Dialog.CustomDialog;
import com.xiaowei.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class MainActivity extends BaseActivity {
    // 定义一个变量，来标识是否退出app
    private static boolean isExit = false;
    private String TAG="com.xiaowei.ui.activity.MainActivity";
    Activity activity;
    @Bind(R.id.recycle)
    RecyclerView recyclerView;
    @Bind(R.id.textview_auto_roll)
    TextSwitcher textSwitcher;
    @Bind(R.id.main_banner)
    Banner banner;
    @Bind(R.id.indicator)
    BannerIndicator bannerIndicator;
    @Bind(R.id.screen_home)
    TextView screenHome;
    @Bind(R.id.tv_sort)
    TextView sortHome;
    HomeAdapter adapter;
    List<ProductListBean.DataBean.contentBean> datas;
    private int index = 0;
    private BitHandler bitHandler;
    AdvertDialog advertDialog = null;
    int page = 1;
    int pageSize = 10;
    String minLoan = "";//最小金额
    String maxLoan = "";//最大金额
    String minTerm = "";//最低期限(天)
    String maxTerm = "";//最高期限
    //    String condition="speed";//精准speed/rate/amount
    int sort = 1;//1/-1  （1：升序，-1：降序）
    boolean isfirst=true;
    CustomDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        ButterKnife.bind(this);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        if (AppUtils.isForeground(activity,TAG)){
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"onstop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG,"onRestart");
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.e(TAG,"onSaveInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"onStart");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
    }

    public void showDialog(){
        if (isfirst) {
            advertDialog.setOnClickListener(new AdvertDialog.OnClickListener() {
                @Override
                public void onFinish() {
                    isfirst=false;
                }

                @Override
                public void onDraw() {
                    isfirst=false;
                    SharedPreferencesUtils.setIsFirst(activity, false);

                }
            });
            advertDialog.show();

        }
    }
    public void initView() {
//        boolean isfirst = SharedPreferencesUtils.getIsFirst(activity);
        advertDialog = new AdvertDialog(activity);

        showDialog();


        datas = new ArrayList<>();

        bitHandler = new BitHandler();
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(activity);
                textView.setSingleLine();
                //  textView.setTextSize(Utils.sp2px(getContext(),12));
                textView.setTextSize(12);
                textView.setTextColor(Color.parseColor("#333231"));
                textView.setEllipsize(TextUtils.TruncateAt.END);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                lp.gravity = Gravity.CENTER_VERTICAL;
                textView.setLayoutParams(lp);
                return textView;
            }
        });


        adapter = new HomeAdapter(datas, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));


        recyclerView.setNestedScrollingEnabled(false);
        adapter.setOnItemClickLitener(new HomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int pos) {

            }

            @Override
            public void OnItemViewClick(int pos) {
                Intent intent = new Intent(activity, DesignActivity.class);
                startActivity(intent);
            }
        });


        List<String> drawables = new ArrayList<>();
        drawables.add("1");
//        drawables.add("2");
//        drawables.add("3");
        banner.setInterval(3000);
        banner.setPageChangeDuration(300);
        banner.setBannerDataInit(new Banner.BannerDataInit() {
            @Override
            public ImageView initImageView() {
                return (ImageView) getLayoutInflater().inflate(R.layout.imageview, null);
            }

            @Override
            public void initImgData(ImageView imageView, Object imgPath) {
                if (imgPath.equals("1"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.mipmap.banner));
                if (imgPath.equals("2"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.mipmap.icon_phone));
                if (imgPath.equals("3"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.mipmap.ic_launcher));
            }
        });
        banner.setDataSource(drawables);

        //----------------------indicator start------------------------------
        bannerIndicator = (BannerIndicator) findViewById(R.id.indicator);
        bannerIndicator.setIndicatorSource(
                ContextCompat.getDrawable(getBaseContext(), R.mipmap.zuobiao_dangqian_banner),//select
                ContextCompat.getDrawable(getBaseContext(), R.mipmap.baisezuobiao_banner),//unselect
                50//widthAndHeight
        );
        banner.attachIndicator(bannerIndicator);
        //----------------------indicator end------------------------------


        banner.setOnBannerItemClickListener(new Banner.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getBaseContext(), "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        getData();
    }

    //公告handler
    class BitHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (textSwitcher != null) {
                textSwitcher.setText(datas.get(index).getName());
                index++;
                if (index == datas.size()) {
                    index = 0;
                    bitHandler.sendEmptyMessageDelayed(0, 2000);

                } else if (index < datas.size()) {
                    bitHandler.sendEmptyMessageDelayed(0, 2000);
                }

            }

        }
    }

    /*
     * 获取数据
     * */
    public void getData() {


        NetWorks.productList(page + "", pageSize + "", minLoan + "", maxLoan + "",
                minTerm + "", maxTerm + "", sort + "", new Subscriber<ProductListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        T.ShowToastForLong(activity, "网络异常");
                    }

                    @Override
                    public void onNext(ProductListBean product) {
                        List<ProductListBean.DataBean.contentBean> contentBean = product.getData().getContent();
                        setDatas(contentBean);
                    }
                });
    }

    /*
     * 设置数据
     * */
    public void setDatas(List<ProductListBean.DataBean.contentBean> contentBean) {
//        bitHandler.removeMessages(0);
        datas.clear();
        if (contentBean.size() > 0) {
            datas.addAll(contentBean);
//            bitHandler.sendEmptyMessage(0);
        }
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.gonggao, R.id.screen_home, R.id.call, R.id.tv_sort})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.screen_home:
                View popview = View.inflate(MainActivity.this, R.layout.pop_screen_home, null);

                initPopuptWindow(popview);

                break;
            case R.id.call:
                 builder = new CustomDialog.Builder(activity);
                builder.setTitle("联系客服");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(activity, new String[]{
                                    Manifest.permission.CALL_PHONE}, 1);
                        } else {
                            call();
                        }

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
                    }
                });

                builder.setMessage("400-0651520");
                builder.create().show();


                break;

            case R.id.tv_sort:
                View popsortview = View.inflate(MainActivity.this, R.layout.pop_sort_home, null);

                initSortPopuptWindow(popsortview);
                break;
        }

    }

    private void call() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.CALL_PHONE}, 1);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + "4000651520"));
            startActivity(intent);
        }
    }

    //动态权限结果返回
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (grantResults != null && grantResults.length > 0) {
            switch (requestCode) {
                case 1:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        call();
                    }
                    break;

                default:
                    break;
            }
        }
    }


    /**
     * 设置额度高低pop框
     */
    int ScreenWidth;
    int ScreenHeight;
    int PopupWindowWidth;
    int PopupWindowHeight;
    PopupWindow PopupWindow;
    TextView sortDi, sortGao;

    private void initSortPopuptWindow(View view) {
        // 获取屏幕的width和height
        ScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
        ScreenHeight = getWindowManager().getDefaultDisplay().getHeight();

        //加载pop框的视图布局view
        // 创建一个PopupWindow
        // 参数1：contentView 指定PopupWindow的内容
        // 参数2：width 指定PopupWindow的width
        // 参数3：height 指定PopupWindow的height
        PopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        //获取pop框的宽和高
        PopupWindowWidth = PopupWindow.getWidth();
        PopupWindowHeight = PopupWindow.getHeight();
        // 需要设置一下此参数，点击外边可消失
        PopupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失    这两步用于点击手机的返回键的时候，不是直接关闭activity,而是关闭pop框
        PopupWindow.setOutsideTouchable(true);

        // 设置此参数获得焦点，否则无法点击，即：事件拦截消费
        PopupWindow.setFocusable(true);

//        //设置动画   采用属性动画
        // 动画效果必须放在showAsDropDown()方法上边，否则无效

        PopupWindow.setAnimationStyle(R.style.style_pop_animation);

//
//获取需要在其上方显示的控件的位置信息
        if (Build.VERSION.SDK_INT != 24) {
            //只有24这个版本有问题，好像是源码的问题
            PopupWindow.showAsDropDown(sortHome);
        } else {
            //7.0 showAsDropDown没卵子用 得这么写
            int[] location = new int[2];
            sortHome.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            PopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, y + sortHome.getHeight());
        }
        sortDi = view.findViewById(R.id.sort_di);
        sortGao = view.findViewById(R.id.sort_gao);

        sortDi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindow.dismiss();
                sort = -1;
                getData();
            }
        });
        sortGao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindow.dismiss();
                sort = 1;
                getData();
            }
        });

        PopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopupWindow = null;// 当点击屏幕时，使popupWindow消失


            }
        });
    }


    /**
     * 设置筛选pop框
     */
    int mScreenWidth;
    int mScreenHeight;
    int mPopupWindowWidth;
    int mPopupWindowHeight;
    PopupWindow mPopupWindow;
    List<String> loanList;
    List<String> termList;
    List<String> precisionList;
    itemTextviewAdapter loanAdapter;
    itemTextviewAdapter termAdapter;
    itemTextviewAdapter precisionAdapter;
    RecyclerView loanView;
    RecyclerView termView;
    RecyclerView precisionView;
    TextView del, complete;

    private void initPopuptWindow(View view) {
        // 获取屏幕的width和height
        mScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
        mScreenHeight = getWindowManager().getDefaultDisplay().getHeight();

        //加载pop框的视图布局view
        // 创建一个PopupWindow
        // 参数1：contentView 指定PopupWindow的内容
        // 参数2：width 指定PopupWindow的width
        // 参数3：height 指定PopupWindow的height
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        //获取pop框的宽和高
        mPopupWindowWidth = mPopupWindow.getWidth();
        mPopupWindowHeight = mPopupWindow.getHeight();
        // 需要设置一下此参数，点击外边可消失
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失    这两步用于点击手机的返回键的时候，不是直接关闭activity,而是关闭pop框
        mPopupWindow.setOutsideTouchable(true);

        // 设置此参数获得焦点，否则无法点击，即：事件拦截消费
        mPopupWindow.setFocusable(true);

//        //设置动画   采用属性动画
        // 动画效果必须放在showAsDropDown()方法上边，否则无效

        mPopupWindow.setAnimationStyle(R.style.style_pop_animation);

        backgroundAlpha(0.5f);
//
//获取需要在其上方显示的控件的位置信息
        if (Build.VERSION.SDK_INT != 24) {
            //只有24这个版本有问题，好像是源码的问题
            mPopupWindow.showAsDropDown(screenHome);
        } else {
            //7.0 showAsDropDown没卵子用 得这么写
            int[] location = new int[2];
            screenHome.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, y + screenHome.getHeight());
        }
        loanView = view.findViewById(R.id.loanview);
        termView = view.findViewById(R.id.termview);
        precisionView = view.findViewById(R.id.precisionview);
        del = view.findViewById(R.id.del);
        complete = view.findViewById(R.id.complete);
        initPopData();
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopupWindow = null;// 当点击屏幕时，使popupWindow消失
                backgroundAlpha(1.0f);// 当点击屏幕时，使半透明效果取消


            }
        });

    }

    /*pop子控件数据
     * */
    public void initPopData() {
        loanList = new ArrayList();
        termList = new ArrayList();
        precisionList = new ArrayList();
        loanList.add("不限额度");
        loanList.add("0~3千");
        loanList.add("3千~一万");
        loanList.add("1万~5万");
        loanList.add("5万以上");
        termList.add("不限期限");
        termList.add("6个月以下");
        termList.add("6个月以上");
        termList.add("1-2年");
        termList.add("2年及以上");
        precisionList.add("最热门");
        precisionList.add("申请人数最多");
        precisionList.add("速度最快");
        precisionList.add("成功率最高");
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        GridLayoutManager manager1 = new GridLayoutManager(this, 3);
        GridLayoutManager manager2 = new GridLayoutManager(this, 3);
        loanView.setLayoutManager(manager);
        termView.setLayoutManager(manager1);
        precisionView.setLayoutManager(manager2);

        loanAdapter = new itemTextviewAdapter(loanList, activity);


        loanView.setAdapter(loanAdapter);
        loanAdapter.setOnItemClickLitener(new itemTextviewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int pos) {
                loanAdapter.setSelection(pos);
                switch (pos) {
                    case 0:
                        break;
                    case 1:
                        maxLoan = 3000 + "";
                        minLoan = "0";
                        break;
                    case 2:
                        maxLoan = 10000 + "";
                        minLoan = 3000 + "";
                        break;
                    case 3:
                        maxLoan = 50000 + "";
                        minLoan = 10000 + "";
                        break;
                    case 4:
                        maxLoan = "";
                        minLoan = 50000 + "";
                        break;
                }
            }

            @Override
            public void OnItemLongClick(int pos) {

            }
        });

        termAdapter = new itemTextviewAdapter(termList, activity);
        termView.setAdapter(termAdapter);
        termAdapter.setOnItemClickLitener(new itemTextviewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int pos) {
                termAdapter.setSelection(pos);
                switch (pos) {
                    case 0:
                        break;
                    case 1:
                        maxLoan = 180 + "";
                        minLoan = "0";
                        break;
                    case 2:
                        maxLoan = 365 + "";
                        minLoan = 180 + "";
                        break;
                    case 3:
                        maxLoan = 731 + "";
                        minLoan = 365 + "";
                        break;
                    case 4:
                        maxLoan = "";
                        minLoan = 731 + "";
                        break;
                }
            }

            @Override
            public void OnItemLongClick(int pos) {

            }
        });

        precisionAdapter = new itemTextviewAdapter(precisionList, activity);
        precisionView.setAdapter(precisionAdapter);
        precisionAdapter.setOnItemClickLitener(new itemTextviewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int pos) {
                precisionAdapter.setSelection(pos);
            }

            @Override
            public void OnItemLongClick(int pos) {

            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                page = 1;
                pageSize = 10;
                minLoan = "";//最小金额
                maxLoan = "";//最大金额
                minTerm = "";//最低期限(天)
                maxTerm = "";//最高期限
//    String condition="speed";//精准speed/rate/amount
                sort = 1;//1/-1  （1：升序，-1：降序）
                getData();
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                getData();
            }
        });
    }

    // 设置popupWindow背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        // 0.0-1.0
        getWindow().setAttributes(lp);
    }


    /*返回键监听*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    /*退出app
     * */
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            MyApplication.instance.Allfinlish();

            System.exit(0);
        }
    }


}
