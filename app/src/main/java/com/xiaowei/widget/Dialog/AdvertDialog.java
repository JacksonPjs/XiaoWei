package com.xiaowei.widget.Dialog;
/*
* 广告dialog
* */

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaowei.R;

import butterknife.Bind;

public class AdvertDialog extends BaseDialog {
    Bitmap bitmap=null;
    TextView msg;
    ImageView bg;
    ImageView finish;


    public AdvertDialog(Context context) {
        super(context);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_advert);

        init();
    }
    public void init(){

        finish=findViewById(R.id.finish);
        bg=findViewById(R.id.bg);
        msg=findViewById(R.id.msg);


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener!=null){
                    onClickListener.onDraw();
                }
                dismiss();
            }
        });

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener!=null){
                    onClickListener.onFinish();
                }
                dismiss();
            }
        });


    }

    private OnLongClickShareListener OnLongClickShareListener;

    public interface OnLongClickShareListener {
        void onFinish(String str);
    }

    public void setOnLongClickNetListener(OnLongClickShareListener OnLongClickShareListener) {
        this.OnLongClickShareListener = OnLongClickShareListener;
    }

    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onFinish();
        void onDraw();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
