package com.example.homerenting_prototype_one.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.R;

import java.util.ArrayList;

public class DistributionAdapter  extends RecyclerView.Adapter<DistributionAdapter.ViewHolder>{
    private ArrayList<String> mData;

    public DistributionAdapter(ArrayList<String> data){
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distribution_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.employee = view.findViewById(R.id.employee_name_DI);
        holder.salaryText = view.findViewById(R.id.salary_DI);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.employee.setText(mData.get(position));
//        salary = Integer.parseInt(holder.salaryText.getText().toString());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView employee;
        EditText salaryText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
