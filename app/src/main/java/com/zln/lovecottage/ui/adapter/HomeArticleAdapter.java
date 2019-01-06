package com.zln.lovecottage.ui.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.szh.commonBase.BaseActivity;
import com.szh.commonBase.util.Constant;
import com.zln.lovecottage.R;
import com.zln.lovecottage.item.ArticleItem;
import com.zln.lovecottage.ui.activity.NewsDetailActivity;

import java.util.List;


public class HomeArticleAdapter extends RecyclerView.Adapter<HomeArticleAdapter.ViewHolder> {

    private Context mContext;

    private List<ArticleItem> articleList;


    public HomeArticleAdapter(Context mContext, List<ArticleItem> articleList, String str) {
        this.mContext = mContext;
        this.articleList = articleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_home_article, parent, false);
        ImageView imageIV = (ImageView) view.findViewById(R.id.icha_iv_image);
        TextView titleTV = (TextView) view.findViewById(R.id.icha_tv_title);
        TextView readNumTV = (TextView) view.findViewById(R.id.icha_tv_readnum);
        TextView contentTV = (TextView) view.findViewById(R.id.icha_tv_content);

        return new ViewHolder(view, imageIV, titleTV, readNumTV, contentTV);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final View view = holder.mView;
        if (articleList != null) {
//            final ArticleItem articleItem = articleList.get(position);
//            Glide.with(mContext)
//                    .load(Constant.DEFAULT_URL + Constant.IMAGE_URL + articleItem.getImageUrl())
//                    .into(holder.imageIV);
//            holder.titleTV.setText(articleItem.getTitle());
//            holder.readNumTV.setText("阅读量：" + articleItem.getReadnum());
//            holder.contentTV.setText(articleItem.getContent());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("newsdetail", articleList.get(position));
                    ((BaseActivity) mContext).jumpToNext(NewsDetailActivity.class, bundle);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return articleList == null || articleList.size() == 0 ? 10 : articleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageIV;
        public final TextView titleTV;
        public final TextView readNumTV;
        public final TextView contentTV;


        public ViewHolder(View view, ImageView imageIV
                , TextView titleTV, TextView readNumTV, TextView contentTV) {
            super(view);
            mView = view;
            this.imageIV = imageIV;
            this.titleTV = titleTV;
            this.readNumTV = readNumTV;
            this.contentTV = contentTV;
        }
    }
}
