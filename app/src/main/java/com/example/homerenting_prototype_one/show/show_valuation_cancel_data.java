package com.example.homerenting_prototype_one.show;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class show_valuation_cancel_data extends show_data {
    public show_valuation_cancel_data(Context page, Context maxLayout) {
        super(page, maxLayout);
    }

    public ConstraintLayout newCustomerInfoLayout(String name, String nametitle, String phone, String address){
        CustomerInfo = new ConstraintLayout(page);
        createMainSection(name, nametitle, phone, address);
        return CustomerInfo;
    }

    @Override
    protected int setNameSpace(){
        return 1;
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
