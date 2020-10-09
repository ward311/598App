package com.example.homerenting_prototype_one.adapter.re_adpater;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.R;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHoler> {
    private ArrayList<ViewHoler> viewHolers = new ArrayList<>();
    private ArrayList<String[]> cars;

    private int limit = 4;

    String TAG = "CarAdapter";

    public CarAdapter(ArrayList cars){
        this.cars = cars;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item, parent, false);
        ViewHoler viewHoler = new ViewHoler(view);
        viewHolers.add(viewHoler);
        Log.d(TAG, "create holder:"+viewHolers.size());
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHoler holder, final int position) {
        holder.weight.setText(cars.get(position)[0]);
        holder.type.setText(cars.get(position)[1]);
        holder.num.setText(cars.get(position)[2]);
        holder.position = position;

        if(position == 0){
            holder.add.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.GONE);
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cars.size() < limit){
                        String[] n = {"", "", String.valueOf(cars.size())};
                        cars.add(n);
                        notifyDataSetChanged();
                        Log.d(TAG, position+". add holder("+viewHolers.size()+").position:"+holder.position+", cars.size:"+cars.size());
                    }
                }
            });
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != 0) {
                    cars.remove(holder.position);
                    viewHolers.remove(holder.position);
                    notifyItemRemoved(holder.position);
                    for(int i = 0; i < cars.size(); i++){
                        viewHolers.get(i).position = i;
                    }
                    Log.d(TAG, position+". remove holder("+viewHolers.size()+").position:"+holder.position+", cars.size:"+cars.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        EditText weight, type, num;
        TextView ton;
        Button add, delete;
        int position;

        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            weight = itemView.findViewById(R.id.weight_CI);
            type = itemView.findViewById(R.id.type_CI);
            num = itemView.findViewById(R.id.num_CI);
            ton = itemView.findViewById(R.id.ton_CI);
            add = itemView.findViewById(R.id.add_btn_CI);
            delete = itemView.findViewById(R.id.delete_btn_CI);
        }
    }
}
