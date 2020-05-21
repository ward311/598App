package com.example.homerenting_prototype_one.show;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import com.example.homerenting_prototype_one.R;

public class show_user_data extends show_data {

    public show_user_data(Context page, Context maxLayout) {
        super(page, maxLayout);
    }

    public ConstraintLayout newCustomerInfoLayout(String datetime, String name, String nametitle, String phone, String address, boolean icon){
        CustomerInfo = new ConstraintLayout(page);
        createTimeSection(datetime);
        createMainSection(name, nametitle, phone, address);
        if(setIcon(datetime)) createIconSection(CustomerInfo);
        return CustomerInfo;
    }

    @Override
    protected int setNameSpace(){
        return 2;
    }

    @Override
    protected int setTimeColor(){
        return Color.rgb(251, 133, 39);
    }

    @Override
    protected int setNameColor(){
        return Color.rgb(25, 176, 237);
    }

    @Override
    protected int setNoFormatColor(TextView T){
        return T.getCurrentTextColor();
    }
}
