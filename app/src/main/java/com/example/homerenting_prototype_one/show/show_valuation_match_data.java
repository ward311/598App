package com.example.homerenting_prototype_one.show;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class show_valuation_match_data extends show_data {
    public show_valuation_match_data(Context page, Context maxLayout) {
        super(page, maxLayout);
    }

    public ConstraintLayout newCustomerInfoLayout(String name, String nametitle, String phone, String address, boolean icon){
        CustomerInfo = new ConstraintLayout(page);
        createMainSection(name, nametitle, phone, address);
        if(icon) createIconSection(CustomerInfo);
        return CustomerInfo;
    }

    @Override
    protected int setNameSpace(){
        return 1;
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
