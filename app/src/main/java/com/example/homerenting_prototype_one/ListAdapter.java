package com.example.homerenting_prototype_one;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends BaseAdapter implements View.OnClickListener{
    private Context context;
    private List<String> data;

    public ListAdapter(List<String> data){
        this.data = data;
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public int getCount() {
        //return 0;
        return data == null ? 0:data.size();
    }

    @Override
    public Object getItem(int position) {
        //return null;
        return  data.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return 0;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return null;
        ViewHolder viewHolder = null;
        if(context == null){
            context = parent.getContext();
        }
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,null);
            viewHolder = new ViewHolder();
            viewHolder.item_layout = convertView.findViewById(R.id.row_layout);
            viewHolder.date_text = convertView.findViewById(R.id.month_date_text);
            viewHolder.time_text = convertView.findViewById(R.id.time_text);
            viewHolder.name_text = convertView.findViewById(R.id.name_text);
            viewHolder.gender_text = convertView.findViewById(R.id.gender_text);
            viewHolder.phone_text = convertView.findViewById(R.id.phone_text);
            viewHolder.address_text = convertView.findViewById(R.id.address_text);
            viewHolder.icon_img = convertView.findViewById(R.id.new_icon_img);
            convertView.setTag(viewHolder);

        }
        viewHolder = (ViewHolder)convertView.getTag();
        viewHolder.item_layout.setTag(R.id.row_layout,position);
        viewHolder.date_text.setTag(R.id.data_text,position);
        viewHolder.time_text.setTag(R.id.time_text,position);
        viewHolder.name_text.setTag(R.id.name_text,position);
        viewHolder.gender_text.setTag(R.id.gender_text,position);
        viewHolder.phone_text.setTag(R.id.phone_text,position);
        viewHolder.address_text.setTag(R.id.address_text,position);
        viewHolder.icon_img.setTag(R.id.new_icon_img,position);

        viewHolder.date_text.setText(data.get(position));
        viewHolder.time_text.setText(data.get(position));
        viewHolder.name_text.setText(data.get(position));
        viewHolder.gender_text.setText(data.get(position));
        viewHolder.phone_text.setText(data.get(position));
        viewHolder.address_text.setText(data.get(position));
        return convertView;
    }
    static class ViewHolder{
        LinearLayout item_layout;
        TextView date_text, time_text, name_text, gender_text, phone_text, address_text;
        ImageView icon_img;
    }
}
