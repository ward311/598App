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

    public ConstraintLayout newCustomerInfoLayout(String datetime, String time, final String name, String nametitle, String phone, String address, boolean icon){
        CustomerInfo = new ConstraintLayout(page);
        createTimeSection(datetime, time);
        createMainSection(name, nametitle, phone, address);
        if(icon) createIconSection(CustomerInfo);
        return CustomerInfo;
    }

    protected void createIconSection(ConstraintLayout peopleDetail){
        ConstraintSet s = new ConstraintSet();
        Guideline iconG = newGuideline(3);
        peopleDetail.addView(iconG);
        ImageView icImage = newIconImage();
        peopleDetail.addView(icImage);
        s.clone(peopleDetail);
        s.connect(icImage.getId(), ConstraintSet.START, iconG.getId(), ConstraintSet.START, 0);
        s.connect(icImage.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        s.connect(icImage.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
        s.applyTo(peopleDetail);
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

    private ImageView newIconImage(){
        ImageView icImage = new ImageView(page);//New標誌

        icImage.setId(R.id.icon_id);
        int dp50 = dip2px(maxLayout, 50);
        ConstraintLayout.LayoutParams pI = new ConstraintLayout.LayoutParams(dp50,dp50);
        icImage.setLayoutParams(pI);
        icImage.setImageResource(R.drawable.new_icon);

        return icImage;
    }
}
