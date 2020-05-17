package com.example.homerenting_prototype_one;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class LocationAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<String> data;
    public LocationAdapter(List<String>data){
        this.data=data;
    }
    @Override
    public void onClick(View v) {

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
    public View getView(int position, View convertView, ViewGroup parent) {
        LocationViewHolder viewHolder = new LocationViewHolder();
        if (context==null)
            context=parent.getContext();
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.furniture_location_item, null);
            viewHolder = new LocationViewHolder();
            viewHolder.floor_text = convertView.findViewById( R.id.location_floor );
            viewHolder.item_text = convertView.findViewById( R.id.location_item );
            viewHolder.amount_text = convertView.findViewById( R.id.location_total );
            convertView.setTag( viewHolder );
        }
        viewHolder = (LocationViewHolder)convertView.getTag();
        viewHolder.floor_text.setTag( R.id.location_floor,position );
//        for (int i=0;i<data.size();i++){
//            if (i==0){
//                viewHolder.floor_text.setText( "1樓/房間1" );
//            }
//            else if (i==5){
//                viewHolder.floor_text.setText( "1樓/廳1" );
//            }
//            else {
//                viewHolder.floor_text.setText( "" );
//            }
//        }
        if (position==0){
            viewHolder.floor_text.setText( "1樓/房間1" );
        }
        else if (position==5){
            viewHolder.floor_text.setText( "1樓/廳1" );
        }
        else {
            viewHolder.floor_text.setText( "" );
        }
        viewHolder.item_text.setTag( R.id.location_item,position );
        viewHolder.item_text.setText( data.get( position ) );
        viewHolder.amount_text.setTag( R.id.location_total,position );
        //viewHolder.amount_text.setText( String.valueOf( (int)Math.random()+1 ) );
        if (position==4 || position==9){
            viewHolder.amount_text.setText( "2");
        }
        else {
            viewHolder.amount_text.setText( "1" );
        }
        return convertView;
    }
    static class LocationViewHolder{
        TextView floor_text,item_text,amount_text;
    }
}
