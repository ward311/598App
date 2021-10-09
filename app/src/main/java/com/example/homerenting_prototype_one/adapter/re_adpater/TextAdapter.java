package com.example.homerenting_prototype_one.adapter.re_adpater;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.system.System_License;

import java.util.ArrayList;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {
    private ArrayList<String[]> data;
    private final String TAG = "TextAdapter";
    Context mContext;
    Bundle bundle = new Bundle();
    public TextAdapter(ArrayList<String[]> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_item, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = data.get(position)[1];
        final TextView input = new TextView (mContext);
        if(data.get(position).length > 2){
            text = text+"噸"+data.get(position)[2]+" "+data.get(position)[3];
            if(data.get(position)[4].equals("1") ){
                text = text + " (已審核)";
                holder.item.setTextColor(Color.parseColor("#19B0ED"));
                holder.item.setOnClickListener(v -> {
                    input.setText("通過時間: "+data.get(position)[5]);
                    input.setTextColor(Color.BLACK);
                    input.setPadding(0,100,0,0);
                    input.setGravity(Gravity.CENTER);
                    new AlertDialog.Builder(mContext)
                            .setTitle("審核通過")
                            .setView(input)
                            .setPositiveButton("瞭解", null)
                            .create()
                            .show();
                });
            }
            else if(data.get(position)[4].equals("0")){
                holder.item.setAlpha(.5f);
                holder.item.setTextColor(Color.parseColor("#FB8527"));
                text = text + " (審核中)";

            }else if(data.get(position)[4].equals("2")){
                holder.item.setAlpha(.5f);
                holder.item.setTextColor(Color.RED);
                text = text + " (審核失敗)";
                holder.item.setOnClickListener(v -> {
                    Log.d(TAG, ""+data.get(position)[3]);
                    input.setText("失敗原因 : "+data.get(position)[6]);
                    input.setTextColor(Color.BLACK);
                    input.setGravity(Gravity.CENTER);
                    new AlertDialog.Builder(mContext)
                            .setTitle("審核失敗")
                            .setMessage(input.getText().toString())
                            .setNegativeButton("取消", null)
                            .setPositiveButton("行照補件", (dialog, which) -> {
                                bundle.putString("new_plateNum", data.get(position)[3]);
                                Intent license = new Intent(mContext , System_License.class);
                                license.putExtras(bundle);
                                mContext.startActivity(license);

                            })
                            .create()
                            .show();
                });
            }else{
                holder.item.setAlpha(.5f);
                text = text + " (已報廢)";
            }

        }
            holder.item.setText(text);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void deleteItem(int position){
        data.remove(position);
        notifyItemRemoved(position);
        Log.d(TAG, "delete");
    }

    public String[] getItem(int position){
        return data.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.text_item_TI);
        }
    }
}
