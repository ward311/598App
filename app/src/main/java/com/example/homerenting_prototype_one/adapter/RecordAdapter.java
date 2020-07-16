package com.example.homerenting_prototype_one.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.order.Order_Detail;
import com.example.homerenting_prototype_one.valuation.MatchMaking_Detail;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    ArrayList<String[]> data;

    public RecordAdapter(ArrayList<String[]>data){
        this.data = data;
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public int getCount(){
        return data == null? 0:data.size();
    }

    @Override
    public Object getItem(int position){
        return data.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        RecordViewHolder viewHolder = null;
        if (context==null){
            context = parent.getContext();
        }
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, null);
            viewHolder = new RecordViewHolder();
            viewHolder.record = convertView.findViewById(R.id.record_item_R);
            viewHolder.day_text = convertView.findViewById(R.id.day_R);
            viewHolder.name_text = convertView.findViewById(R.id.name_R);
            convertView.setTag( viewHolder );
        }
        viewHolder.day_text.setText(data.get(position)[1]);
        if(!data.get(position)[3].equals("chosen")){
            viewHolder.day_text.setBackgroundColor(Color.parseColor("#FB8527"));
        }
        viewHolder.name_text.setText(data.get(position)[2]);
        viewHolder.record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("order_id", data.get(position)[0]);
                if(data.get(position)[3].equals("chosen")){
                    intent.setClass(context, Order_Detail.class);
                    bundle.putBoolean("btn", true);
                }
                else intent.setClass(context, MatchMaking_Detail.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class RecordViewHolder{
        LinearLayout record;
        TextView day_text, name_text;
    }
}
