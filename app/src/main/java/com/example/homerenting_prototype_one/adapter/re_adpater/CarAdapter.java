package com.example.homerenting_prototype_one.adapter.re_adpater;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.R;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHoler> {
    private ArrayList<String[]> cars;
    private volatile int ready = 0;

    String TAG = "CarAdapter";

    public CarAdapter(ArrayList cars){
        this.cars = cars;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item, parent, false);
        if(view == null) Log.d(TAG, "ready "+ready+". view is null!?");
        else Log.d(TAG, "ready "+ready+". view is ok!");
        ready = ready+1;
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        holder.weight.setText(cars.get(position)[0]);
        holder.type.setText(cars.get(position)[1]);
        holder.num.setText(cars.get(position)[2]);
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public int getReady(){
        if(ready == getItemCount()) return -1;
        else return ready;
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        EditText weight, type, num;
        TextView ton;

        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            weight = itemView.findViewById(R.id.weight_CI);
            type = itemView.findViewById(R.id.type_CI);
            num = itemView.findViewById(R.id.num_CI);
            ton = itemView.findViewById(R.id.ton_CI);
        }
    }
}
