package com.example.asus.dantrirss.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.asus.dantrirss.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class ArticleAdapter extends BaseAdapter {

    class ViewHolder {
        ImageView appIcon;
        TextView appName;
        TextView size;
    }

    public List<RSSItem> mlistAppInfo;
    LayoutInflater infater = null;
    private Context mContext;


    public ArticleAdapter(Context context, List<RSSItem> apps) {
        infater = LayoutInflater.from(context);
        mContext = context;
        this.mlistAppInfo = apps;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mlistAppInfo.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mlistAppInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = infater.inflate(R.layout.item_listview,
                    null);
            holder = new ViewHolder();
            holder.appIcon = (ImageView) convertView
                    .findViewById(R.id.app_icon);
            holder.appName = (TextView) convertView
                    .findViewById(R.id.app_name);
            holder.size = (TextView) convertView
                    .findViewById(R.id.app_size);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final RSSItem item = (RSSItem) getItem(position);
        if (item != null) {

         //   holder.appIcon.setImageResource(item.);
            holder.appName.setText(item.get_title());
            holder.size.setText(item.get_pubdate());
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(item.get_img(), holder.appIcon);

        }


        return convertView;
    }


}
