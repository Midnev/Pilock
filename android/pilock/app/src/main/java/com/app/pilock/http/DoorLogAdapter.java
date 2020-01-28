package com.app.pilock.http;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.pilock.R;

import java.util.ArrayList;

public class DoorLogAdapter  extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;

    public DoorLogAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(ArrayList<String> list) {
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
    public View getView(int i, View v, ViewGroup viewGroup) {
        if (v== null){
            v = View.inflate(context, R.layout.module_dlog,null);
        }
        TextView log =v.findViewById(R.id.logView);
        log.setText(list.get(i));

        return v;
    }
}
