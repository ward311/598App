package com.example.homerenting_prototype_one;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DetailAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<String> data;
    public DetailAdapter(List<String>data){
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
        DetailViewHolder viewHolder = null;
        if (context==null)
            context = parent.getContext();
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.furniture_detail_btn, null);
            viewHolder = new DetailViewHolder();
            viewHolder.num_text = convertView.findViewById( R.id.detail_number );
            viewHolder.name_text = convertView.findViewById( R.id.detail_name );
            viewHolder.amount_text = convertView.findViewById( R.id.detail_total );
            viewHolder.ps_text = convertView.findViewById( R.id.detail_ps );
            convertView.setTag( viewHolder );
        }
        viewHolder.num_text.setTag( R.id.detail_number,position );
        viewHolder.num_text.setText(String.valueOf( position+1 ));
        viewHolder.name_text.setTag( R.id.detail_name,position );
        viewHolder.name_text.setText( data.get( position ) );
        viewHolder.amount_text.setTag( R.id.detail_total,position );
        //viewHolder.amount_text.setText(String.valueOf( (int)Math.random()*5+1 ));
        if(position==0 || position==9 || position==10){
            viewHolder.amount_text.setText("2");
        }
        else if (position==4 || position==14){
            viewHolder.amount_text.setText("3");
        }
        else if (position==17){
            viewHolder.amount_text.setText("5");
        }
        else {
            viewHolder.amount_text.setText("1");
        }
        viewHolder.ps_text.setTag( R.id.detail_ps,position );
        viewHolder.ps_text.setText( "" );
        return convertView;
    }

    static class DetailViewHolder{
        TextView num_text, name_text, amount_text, ps_text;
    }
}
