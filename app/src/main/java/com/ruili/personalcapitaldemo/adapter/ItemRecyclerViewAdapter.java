package com.ruili.personalcapitaldemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruili.personalcapitaldemo.R;
import com.ruili.personalcapitaldemo.WebViewActivity;
import com.ruili.personalcapitaldemo.model.Item;

import java.util.List;

/**
 * Created by Rui Li on 10/29/17.
 */

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {
    private Context mContext;
    private List<Item> mItemList;

    public ItemRecyclerViewAdapter(Context context, List<Item> itemList) {
        this.mContext = context;
        this.mItemList = itemList;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // each item is hosted inside a vertically oriented LinearLayout
        LinearLayout linearLayout = new LinearLayout(parent.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackground(mContext.getDrawable(R.drawable.bg_gray_rectangle));

        ImageView imageViewPic = new ImageView(mContext);
        TextView tvTitle = new TextView(mContext);

        linearLayout.addView(imageViewPic);
        linearLayout.addView(tvTitle);

        return new ItemViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.imageViewPic.setImageBitmap(mItemList.get(position).getImageBitmap());
        holder.imageViewPic.setAdjustViewBounds(true);
        holder.tvTitle.setMaxLines(2);
        holder.tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        holder.tvTitle.setTextColor(Color.BLACK);
        holder.tvTitle.setTextSize(12);
        holder.tvTitle.setPadding(10, 10, 10, 10);
        holder.tvTitle.setText(mItemList.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", mItemList.get(position).getLink());
                intent.putExtra("title", mItemList.get(position).getTitle());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView imageViewPic;
        TextView tvTitle;

        ItemViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.imageViewPic = (ImageView) ((LinearLayout) itemView).getChildAt(0);
            this.tvTitle = (TextView) ((LinearLayout) itemView).getChildAt(1);
        }
    }
}
