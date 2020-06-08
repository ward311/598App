package com.example.homerenting_prototype_one;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class FurnitureAdapter<total> extends BaseAdapter implements View.OnClickListener{
    private Context context;
    private List<String> data;

    int[] total;

    public FurnitureAdapter(List<String> data){
        this.data = data;
        total = new int[data.size()];
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (context == null)
            context = parent.getContext();
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.furniture_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.furniture_text);
            viewHolder.number = convertView.findViewById(R.id.furniture_number_text);
            viewHolder.minus_btn = convertView.findViewById(R.id.minus_btn);
            viewHolder.plus_btn = convertView.findViewById(R.id.plus_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // viewHolder.minus_btn.setTag(R.id.minus_btn,position);
        viewHolder.minus_btn.setText("-");
        //final ViewHolder finalViewHolder = viewHolder;
        //final int total = 0;
//        final int[] total = new int[data.size()];
        viewHolder.number.setText( String.valueOf( total ) );
        viewHolder.plus_btn.setText("+");
        viewHolder.name.setText(data.get(position));
        viewHolder.number.setText(String.valueOf(0));

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.minus_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total[position] > 0) {
                    total[position] = total[position] - 1;
                }

                finalViewHolder.number.setText( String.valueOf( total[position] ) );
            }
        });
        // viewHolder.plus_btn.setTag(R.id.minus_btn,position);

        // final ViewHolder finalViewHolder1 = viewHolder;
        final ViewHolder finalViewHolder1 = viewHolder;
        viewHolder.plus_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total[position] = total[position] + 1;
                // TextView tv = v.findViewById( R.id.furniture_number_text );
                finalViewHolder1.number.setText( String.valueOf( total[position] ) );
            }
        });
        // viewHolder.name.setTag(R.id.furniture_text,position);
        // viewHolder.name.setText(data.get(position));
        //viewHolder.name.setOnClickListener(this);
        // viewHolder.number.setTag(R.id.furniture_number_text,position);
        // viewHolder.number.setText(String.valueOf(0));
        //viewHolder.number.setOnClickListener(this);
        return convertView;
    }

    static class ViewHolder{
        TextView name, number;
        Button minus_btn, plus_btn;
    }
}
