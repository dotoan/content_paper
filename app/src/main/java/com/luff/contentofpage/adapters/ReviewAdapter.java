package com.luff.contentofpage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luff.contentofpage.R;
import com.luff.contentofpage.models.SlidingItem;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

/**
 * Created by Anonymous on 12/29/2015.
 */
public class ReviewAdapter extends BaseAdapter {
    private ArrayList<SlidingItem> list;
    private Context context;

    public ReviewAdapter(Context context, ArrayList<SlidingItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_items, viewGroup,false);
        TextView tv = (TextView) view.findViewById(R.id.tv1);
        ImageView iv = (ImageView) view.findViewById(R.id.iv1);
        tv.setText(list.get(i).getTitle());
        iv.setImageBitmap(list.get(i).getIcon());
        return view;
    }
}
