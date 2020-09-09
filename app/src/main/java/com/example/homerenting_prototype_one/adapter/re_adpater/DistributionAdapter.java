package com.example.homerenting_prototype_one.adapter.re_adpater;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.R;

import java.util.ArrayList;

public class DistributionAdapter  extends RecyclerView.Adapter<DistributionAdapter.ViewHolder>{
    private ArrayList<String> mData;
    private ArrayList<String> s = new ArrayList<>();

    public DistributionAdapter(ArrayList<String> data){
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distribution_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.employee.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView employee;
        EditText salaryText;
        String salary;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            employee = itemView.findViewById(R.id.employee_name_DI);
            salaryText = itemView.findViewById(R.id.salary_DI);
//            setSalary();
        }

        public void setSalary() {
            salaryText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    salary = salaryText.getText().toString();
                }
            });
        }

    }
}
