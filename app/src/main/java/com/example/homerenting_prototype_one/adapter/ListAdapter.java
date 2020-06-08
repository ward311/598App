package com.example.homerenting_prototype_one.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.homerenting_prototype_one.R;

import java.util.Arrays;
import java.util.List;

import static com.example.homerenting_prototype_one.show.global_function.dip2px;


public class ListAdapter extends BaseAdapter implements View.OnClickListener{
    private Context context;
    private List<String[]> data;
    String TAG = "LiseAdapter";

    public ListAdapter(List<String[]> data){
        this.data = data;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return  data.get(position);
    }

    @Override
    public long getItemId(int position) {
        Object object = data.get(position);
        return data.indexOf(object);
    }

    static class ViewHolder{
        LinearLayout item_layout;
        LinearLayout time_layout, main_layout;
        TextView date_text, time_text, name_text, gender_text, phone_text, address_text;
        ImageView icon_img;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(context == null){
            context = parent.getContext();
        }
        if (view == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,null);
            viewHolder = new ViewHolder();
            viewHolder.item_layout = view.findViewById(R.id.row_layout);
            viewHolder.time_layout = view.findViewById(R.id.timeLayout);
            viewHolder.main_layout = view.findViewById(R.id.mainLayout);
            viewHolder.date_text = view.findViewById(R.id.month_date_text);
            viewHolder.time_text = view.findViewById(R.id.time_text);
            viewHolder.name_text = view.findViewById(R.id.name_SD);
            viewHolder.gender_text = view.findViewById(R.id.gender_text);
            viewHolder.phone_text = view.findViewById(R.id.phone_text);
            viewHolder.address_text = view.findViewById(R.id.address_text);
            viewHolder.icon_img = view.findViewById(R.id.new_icon_img);
            view.setTag(viewHolder);
        }
        Log.i(TAG, "position: "+position+"/"+getCount());
        viewHolder = (ViewHolder)view.getTag();
        viewHolder.item_layout.setTag(R.id.row_layout,position);
        viewHolder.date_text.setTag(R.id.data_text,position);
        viewHolder.time_text.setTag(R.id.time_text,position);
        viewHolder.name_text.setTag(R.id.name_SD,position);
        viewHolder.gender_text.setTag(R.id.gender_text,position);
        viewHolder.phone_text.setTag(R.id.phone_text,position);
        viewHolder.address_text.setTag(R.id.address_text,position);
        viewHolder.icon_img.setTag(R.id.new_icon_img,position);

        int i = 1;
        if(data.get(position).length >= 8){
            Log.i(TAG, "date: "+data.get(position)[i]);
            viewHolder.date_text.setText(data.get(position)[i++]);
            Log.i(TAG, "time: "+data.get(position)[i]);
            viewHolder.time_text.setText(data.get(position)[i++]);
        }
        else{
            viewHolder.time_layout.setVisibility(View.GONE);
            viewHolder.main_layout.setPadding(dip2px(viewHolder.item_layout.getContext(), 10),0,0,0);
        }
        Log.i(TAG, "name: "+data.get(position)[i]);
        viewHolder.name_text.setText(data.get(position)[i++]);
        Log.i(TAG, "gender: "+data.get(position)[i]);
        viewHolder.gender_text.setText(data.get(position)[i++]);
        Log.i(TAG, "phone: "+data.get(position)[i]);
        viewHolder.phone_text.setText(data.get(position)[i++]);
        Log.i(TAG, "address: "+data.get(position)[i]);
        viewHolder.address_text.setText(data.get(position)[i++]);
        if(!data.get(position)[i++].equals("1"))
            viewHolder.icon_img.setVisibility(View.INVISIBLE);
        if(Arrays.asList(data.get(position)).contains("cancel")){
            viewHolder.date_text.setTextColor(Color.rgb(152, 152, 152));
            viewHolder.time_text.setTextColor(Color.rgb(152, 152, 152));
            viewHolder.name_text.setTextColor(Color.rgb(112, 112, 112));
        }
        return view;
    }
}
