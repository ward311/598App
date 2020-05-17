package com.example.homerenting_prototype_one.show;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import com.example.homerenting_prototype_one.R;

public class show_canceled_user_data extends show_data {

    public show_canceled_user_data(Context page, Context maxLayout) {
        super(page, maxLayout);
    }

    public ConstraintLayout newCustomerInfoLayout(String datetime, String time, final String name, String nametitle, String phone, String address){
        CustomerInfo = new ConstraintLayout(page);
        createTimeSection(datetime, time);
        createMainSection(name, nametitle, phone, address);
        return CustomerInfo;
    }

    @Override
    protected int setTimeColor(){
        return Color.rgb(152, 152, 152);
    }

    @Override
    protected int setNameColor(){
        return Color.rgb(112, 112, 112);
    }

    @Override
    protected int setNoFormatColor(TextView T){
        return Color.rgb(152, 152, 152);
    }
}
