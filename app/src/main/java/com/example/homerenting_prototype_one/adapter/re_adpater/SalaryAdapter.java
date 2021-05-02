package com.example.homerenting_prototype_one.adapter.re_adpater;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.R;

import java.util.ArrayList;

public class SalaryAdapter extends RecyclerView.Adapter<SalaryAdapter.ViewHolder> {
    private ArrayList<String[]> mData;
    private static final String TAG = SalaryAdapter.class.getSimpleName();

    public SalaryAdapter(ArrayList<String[]> data) {
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.salary_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.employee = view.findViewById(R.id.employee_SI);
        holder.salary = view.findViewById(R.id.salary_SI);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.employee.setText(mData.get(position)[0]);
        holder.salary.setText(mData.get(position)[1]);
    }

    @Override
    public int getItemCount() {
        if(mData.size() == 0 || mData.get(0)[0].equals("沒有資料")) return 0;
        return mData.size();
    }

    public void showDatas(){
        int i;
        Log.d(TAG, "showDatas:");
        for(i = 0; i < mData.size(); i++){
            Log.d(TAG, "{"+mData.get(i)[0]+", "+mData.get(i)[1]+"}");
        }
    }

    public ArrayList<String> getStaffName(){
        ArrayList<String> employee_names = new ArrayList<>();
        int i;
        for(i = 0; i < mData.size(); i++){
            employee_names.add(mData.get(i)[0]);
        }
        return employee_names;
    }

    public ArrayList<Integer> getStaffPay(){
        ArrayList<Integer> salaries = new ArrayList<>();
        int i;
        for(i = 0; i < mData.size(); i++){
            if(!mData.get(i)[1].isEmpty() && !mData.get(i)[1].equals(""))
                salaries.add(Integer.valueOf(mData.get(i)[1]));
        }
        return salaries;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView employee, salary;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
