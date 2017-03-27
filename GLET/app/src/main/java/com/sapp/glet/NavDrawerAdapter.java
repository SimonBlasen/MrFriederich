package com.sapp.glet;

/**
 * Created by Simon on 27.03.2017.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sapp.glet.database.Database;

import java.util.HashMap;
import java.util.List;

public class NavDrawerAdapter extends BaseExpandableListAdapter {


    private List<String> header_titles;
    private HashMap<String,List<String>> child_titles;
    private Context ctx;

    NavDrawerAdapter(Context ctx, List<String> header_titles, HashMap<String,List<String>> child_titles){
        this.ctx = ctx;
        this.child_titles = child_titles;
        this.header_titles = header_titles;
    }

    @Override
    public int getGroupCount() {
        return header_titles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child_titles.get(header_titles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return header_titles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child_titles.get(header_titles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) this.getGroup(groupPosition);
        boolean onlineStatus = Database.getPlayer(0).isOnline();
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.parent_layout,null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.heading_item);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(title);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon_parent);
        switch (groupPosition){
            case 0:
                icon.setImageResource(R.mipmap.ic_game);
                break;
            case 1:
                if(onlineStatus){
                    icon.setImageResource(R.mipmap.ic_online);
                    textView.setText("Online");
                }
                else {
                    icon.setImageResource(R.mipmap.ic_offline);
                    textView.setText("Beschäftigt");
                }
                break;
            case 2:
                icon.setImageResource(R.mipmap.ic_agora);
                break;
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String title = (String) this.getChild(groupPosition,childPosition);
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child_layout,null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.child_item);
        textView.setText(title);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon_child);
        switch (groupPosition){
            case 0: break;
            case 1:
                switch (childPosition){
                    case 0:
                        icon.setImageResource(R.mipmap.ic_online);
                        break;
                    case 1:
                        icon.setImageResource(R.mipmap.ic_offline);
                        break;
                }

                break;
            case 2: break;
        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

