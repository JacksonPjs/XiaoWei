package com.xiaowei.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blibrary.utils.Utils;
import com.xiaowei.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class itemTextviewAdapter extends RecyclerView.Adapter<itemTextviewAdapter.ViewHolder> {
    private List<String> datas;
    private Context context;
    int select = 0;
    OnItemClickLitener onItemClickLitener;


    public itemTextviewAdapter(List<String> datas, Context context) {
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
        holder.title.setText(datas.get(position)+"");
        if (select == position) {
            holder.itemView.setSelected(true);
            holder.title.setTextColor(Utils.getColor(context,R.color.white));
        }else {
            holder.itemView.setSelected(false);
            holder.title.setTextColor(Utils.getColor(context,R.color.gray_item));

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickLitener!=null){
                    onItemClickLitener.onItemClick(position);
                }

            }
        });

        //  holder.circleProgressbar.setProgressNotInUiThread(80);


    }



    @Override
    public int getItemCount() {

        return datas.size();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.title)
        TextView title;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickLitener{
        void onItemClick(int pos);
        void OnItemLongClick(int pos);
    }


}