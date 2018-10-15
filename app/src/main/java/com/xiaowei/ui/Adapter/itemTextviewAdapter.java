package com.xiaowei.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blibrary.flowlayout.FlowLayout;
import com.example.blibrary.flowlayout.TagAdapter;
import com.example.blibrary.flowlayout.TagFlowLayout;
import com.example.blibrary.utils.Utils;
import com.xiaowei.R;
import com.xiaowei.bean.ScreenBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class itemTextviewAdapter extends RecyclerView.Adapter<itemTextviewAdapter.ViewHolder> {
    private List<ScreenBean> datas;
    private Context context;
    int select = 0;
    OnItemClickLitener onItemClickLitener;
    private TagAdapter<String> mAdapter ;

    public itemTextviewAdapter(List<ScreenBean> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.onItemClickLitener = mOnItemClickLitener;
    }

    public void setSelection(int position) {
        this.select = position;
        notifyDataSetChanged();
    }
    @Override
    public itemTextviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textview, parent, false);
        return new itemTextviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final itemTextviewAdapter.ViewHolder holder, final int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        holder.title.setText(datas.get(position).getTitle()+"");
        final LayoutInflater mInflater = LayoutInflater.from(context);

        holder.flowLayout.setAdapter(mAdapter=new TagAdapter<String>(datas.get(position).getName()) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        holder.flowLayout, false);
                tv.setText(o);
                return tv;
            }
        });
        mAdapter.setSelectedList(0);//预先设置选中
        holder.flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int posi, FlowLayout parent)
            {
//                Toast.makeText(context, datas.get(position).getName().get(posi), Toast.LENGTH_SHORT).show();

                if (onItemClickLitener!=null){
                    onItemClickLitener.onItemClick(position,posi);
                }
                return true;
            }
        });



    }



    @Override
    public int getItemCount() {

        return datas.size();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.id_flowlayout)
        TagFlowLayout flowLayout;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickLitener{
        void onItemClick(int pos,int itempos);
        void OnItemLongClick(int pos);
    }


}