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

    String TAG = "CarAdapter";

    public CarAdapter(ArrayList cars){
        this.cars = cars;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item, parent, false);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {

    }

    @Override
    public int getItemCount() {
        return cars.size();
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
