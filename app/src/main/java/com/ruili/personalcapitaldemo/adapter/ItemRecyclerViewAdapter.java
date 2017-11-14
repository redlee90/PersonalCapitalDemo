package com.ruili.personalcapitaldemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ruili.personalcapitaldemo.WebViewActivity;
import com.ruili.personalcapitaldemo.model.Item;
import com.ruili.personalcapitaldemo.util.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.setBackground(Utilities.getGrayFrameBackground());

        ImageView imageViewPic = new ImageView(mContext);
        ProgressBar progressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleSmall);
        FrameLayout.LayoutParams progressBarLayoutParams = new FrameLayout.LayoutParams(20, 20);
        progressBarLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        progressBar.setLayoutParams(progressBarLayoutParams);
        TextView tvTitle = new TextView(mContext);

        linearLayout.addView(imageViewPic);
        linearLayout.addView(progressBar);
        linearLayout.addView(tvTitle);

        return new ItemViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.imageViewPic.setVisibility(View.GONE);
        holder.imageViewPic.setAdjustViewBounds(true);
        new LoadImageIconAsyncTask(holder.imageViewPic, holder.progressBar, mItemList.get(position).getImageUrl()).execute();
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
        ProgressBar progressBar;
        ImageView imageViewPic;
        TextView tvTitle;

        ItemViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.imageViewPic = (ImageView) ((LinearLayout) itemView).getChildAt(0);
            this.progressBar = (ProgressBar) ((LinearLayout) itemView).getChildAt(1);
            this.tvTitle = (TextView) ((LinearLayout) itemView).getChildAt(2);
        }
    }

    private static class LoadImageIconAsyncTask extends AsyncTask{
        private ImageView imageView;
        private ProgressBar progressBar;
        private String url;
        private Bitmap bitmap;

        LoadImageIconAsyncTask(ImageView imageView, ProgressBar progressBar, String url) {
            this.imageView = imageView;
            this.progressBar = progressBar;
            this.url = url;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent(),
                        null, bmOptions);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            super.onPostExecute(o);
        }
    }
}
