package com.jscompany.tp15hugpet2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NoticeAdapter extends BaseExpandableListAdapter {

    Context context;
    private int groupLayout = 0;
    private int chlidLayout = 0;
    ArrayList<NoticeItem> items;
    LayoutInflater myinf = null;

    public NoticeAdapter(Context context, int groupLayout, int chlidLayout, ArrayList<NoticeItem> items) {
        this.context = context;
        this.groupLayout = groupLayout;
        this.chlidLayout = chlidLayout;
        this.items = items;
        this.myinf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    // 자식 몇개 인지 보여주는 곳
    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return items.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return items.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = myinf.inflate(this.groupLayout,viewGroup, false);
        }
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(items.get(i).title);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = myinf.inflate(this.chlidLayout,viewGroup, false);
        }
        TextView content = (TextView) view.findViewById(R.id.content);
        content.setText(items.get(i).content);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
