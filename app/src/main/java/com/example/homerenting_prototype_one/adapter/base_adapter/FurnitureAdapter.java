package com.example.homerenting_prototype_one.adapter.base_adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.homerenting_prototype_one.R;

import java.util.ArrayList;


public class FurnitureAdapter extends BaseAdapter implements View.OnClickListener{
    private Context context;
    private static String space;
    private static String order_id;
    private ArrayList<String[]> data;
    String TAG = "FurnitureAdapter";

    public FurnitureAdapter(ArrayList<String[]> data, String space){
        this.data = data;
        this.space = space;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.minus_btn:

            case R.id.plus_btn:

        }
    }

    @Override
    public int getCount() {
        return data == null ? 0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return  data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

//        Log.d(TAG,"position: "+position);

        ViewHolder viewHolder = null;

        if (context == null)
            context = parent.getContext();
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.furniture_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.furniture_text);
            viewHolder.number = convertView.findViewById(R.id.furniture_number_text);
            viewHolder.originalNumber = convertView.findViewById(R.id.originalNum_FI);
            viewHolder.to = convertView.findViewById(R.id.to_FI);
            viewHolder.minus_btn = convertView.findViewById(R.id.minus_btn);
            viewHolder.plus_btn = convertView.findViewById(R.id.plus_btn);
            viewHolder.add_btn = convertView.findViewById(R.id.add_furniture_btn);
            viewHolder.item = convertView.findViewById(R.id.item_LL_FI);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.name.setTag(R.id.furniture_text,position);
        viewHolder.name.setText(data.get(position)[1]);

        if(!data.get(position)[2].equals("-1")){
            viewHolder.originalNumber.setTag(R.id.furniture_number_text,position);
            viewHolder.originalNumber.setText(data.get(position)[2]);
            viewHolder.originalNumber.setVisibility(View.VISIBLE);
            viewHolder.to.setVisibility(View.VISIBLE);
            viewHolder.item.setBackgroundColor(Color.parseColor("#FFE7E7"));
        }

        viewHolder.number.setTag(R.id.furniture_number_text,position);
        viewHolder.number.setText(data.get(position)[3]);

//        Log.d(TAG, "name: "+data.get(position)[1]+", number: "+data.get(position)[2]);

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(data.get(position)[3]);
                if(num > 0)
                    data.get(position)[3] = String.valueOf(--num);
                finalViewHolder.number.setText(data.get(position)[3]);
            }
        });

        viewHolder.plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(data.get(position)[3]);
                data.get(position)[3] = String.valueOf(++num);
                finalViewHolder.number.setText(data.get(position)[3]);
            }
        });

        return convertView;
    }

    static class ViewHolder{
        TextView name, number, originalNumber, to;
        Button minus_btn, plus_btn;
        Button add_btn;
        LinearLayout item;
    }

    public void setOrder_id(String order_id){
        this.order_id = order_id;
    }

    public static String getOrder_id(){
        return order_id;
    }
}