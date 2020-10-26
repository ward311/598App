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

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    private ArrayList<String[]> cars;

    private int limit = 40;

    String TAG = "CarAdapter";

    public CarAdapter(ArrayList cars){
        this.cars = cars;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.weight.setText(cars.get(position)[0]);
        holder.type.setText(cars.get(position)[1]);
        holder.num.setText(cars.get(position)[2]);

        setCarStr(holder.weight, position, 0);
        setCarStr(holder.type, position, 1);
        setCarStr(holder.num, position, 2);

        holder.setIsRecyclable(false);
        if(holder.getAdapterPosition() == 0) showAddBtn(holder.add, holder.delete);
        else {
            holder.add.setVisibility(View.GONE);
            holder.delete.setVisibility(View.VISIBLE);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.weight.setError(null);
                holder.type.setError(null);
                holder.num.setError(null);
                cars.remove(holder.getAdapterPosition());
//                notifyItemInserted(cars.size()-1);
                notifyDataSetChanged();

                for(int i = holder.getAdapterPosition(); i < getItemCount(); i++){
                    setCarStr(holder.weight, i, 0);
                    setCarStr(holder.type, i, 1);
                    setCarStr(holder.num, i, 2);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public void showAddBtn(Button add, Button delete){
        add.setVisibility(View.VISIBLE);
        delete.setVisibility(View.GONE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cars.size() < limit){
                    String[] n = {"", "", ""};
                    cars.add(n);
                    notifyItemInserted(cars.size()-1);
                }
            }
        });
    }

    public void setCarStr(EditText editText, int position, int i){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = editText.getText().toString();
                if(!str.isEmpty()) cars.get(position)[i] = str;
                else if(!cars.get(position)[i].equals("")) cars.get(position)[i] = "";

                Log.d(TAG, "cars.get("+position+")["+i+"] = "+cars.get(position)[i]);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText weight, type, num;
        TextView ton;
        Button add, delete;

        public ViewHolder(@NonNull View itemView) {
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
