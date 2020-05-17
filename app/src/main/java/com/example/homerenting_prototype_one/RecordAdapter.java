package com.example.homerenting_prototype_one;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<String> day, employee;
    public RecordAdapter(List<String> day){
        this.day = day;
        //this.employee = employee;
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public int getCount() {
        return day == null? 0:day.size();
    }

    @Override
    public Object getItem(int position) {
        return day.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecordViewHolder viewHolder = null;
        if (context==null){
            context = parent.getContext();
        }
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, null);
            viewHolder = new RecordViewHolder();
            viewHolder.day_text = convertView.findViewById( R.id.record_date_text );
            viewHolder.day_list = convertView.findViewById( R.id.record_date_list );
            convertView.setTag( viewHolder );
        }
        viewHolder = (RecordViewHolder) convertView.getTag();
        viewHolder.day_text.setTag( R.id.record_date_text,position );
        viewHolder.day_text.setText( day.get( position ) );
        viewHolder.day_list.setTag( R.id.record_date_list,position );
        //String[] employees = {"唐文心","","唐文心","","唐文心","唐文心","唐文心","","","唐文心","唐文心","","","唐文心","唐文心","唐文心","","唐文心","唐文心","","唐文心","唐文心"};
        ArrayList employees = new ArrayList<>();
        employees.add( "唐文心" );
        ArrayAdapter employee_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,employees);
        viewHolder.day_list.setAdapter(employee_adapter);
        viewHolder.day_list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        } );
        return convertView;
    }

    static class RecordViewHolder{
        TextView day_text;
        ListView day_list;
    }
}
