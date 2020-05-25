package com.example.homerenting_prototype_one.show;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class show_noData {
    Context page;
    Context maxLayout;

    public show_noData(Context page, Context maxLayout){
        setPage(page);
        setMaxLayout(maxLayout);
    }
    private void setPage(Context page){ this.page = page;}
    private void setMaxLayout(Context maxLayout){ this.maxLayout = maxLayout;}

    public TextView noDataMessage(){
        //Toast.makeText(page, "responseDate is null", Toast.LENGTH_LONG).show();
        TextView noData = new TextView(page);
        noData.setText("現在沒有資料");
        noData.setTextSize(25);
        noData.setGravity(Gravity.CENTER);
        noData.setPadding(0,50,0,0);
        return noData;
    }
}
