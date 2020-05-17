package com.example.homerenting_prototype_one.show;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import com.example.homerenting_prototype_one.R;

public abstract class show_data {
    Context page;
    Context maxLayout;
    ConstraintLayout CustomerInfo;

    public show_data(Context page, Context maxLayout){
        setPage(page);
        setMaxLayout(maxLayout);
    }

    private void setPage(Context page){ this.page = page;}
    private void setMaxLayout(Context maxLayout){ this.maxLayout = maxLayout;}

    protected void createTimeSection(String datetime, String time){
        ConstraintSet s = new ConstraintSet();
        Guideline timeG = newGuideline(1);
        CustomerInfo.addView(timeG);
        TextView dtText = newdtText(datetime);
        CustomerInfo.addView(dtText);
        TextView tText = newtText(time);
        CustomerInfo.addView(tText);
        s.clone(CustomerInfo);
        s.connect(dtText.getId(), ConstraintSet.START, timeG.getId(), ConstraintSet.START, 0);
        s.connect(dtText.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        s.connect(tText.getId(), ConstraintSet.START, timeG.getId(), ConstraintSet.START, 0);
        s.connect(tText.getId(), ConstraintSet.TOP, dtText.getId(), ConstraintSet.BOTTOM, 0);
        s.applyTo(CustomerInfo);
    }

    protected void createMainSection(String name, String nametitle, String phone, String address){
        ConstraintSet s = new ConstraintSet();
        Guideline nameG = newGuideline(2);
        CustomerInfo.addView(nameG);
        TextView nText = newNameText(name);
        CustomerInfo.addView(nText);
        TextView ntText = newNoFormatText(nametitle, "nametitle");
        CustomerInfo.addView(ntText);
        TextView pText = newNoFormatText(phone, "phone");
        CustomerInfo.addView(pText);
        TextView aText = newNoFormatText(address, "address");
        CustomerInfo.addView(aText);
        s.clone(CustomerInfo);
        s.connect(nText.getId(), ConstraintSet.START, nameG.getId(), ConstraintSet.START, 0);
        s.connect(nText.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        s.connect(ntText.getId(), ConstraintSet.START, nText.getId(), ConstraintSet.END, dip2px(maxLayout, 4));
        s.connect(ntText.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, dip2px(maxLayout, 12));
        s.connect(pText.getId(), ConstraintSet.START, nameG.getId(), ConstraintSet.START, 0);
        s.connect(pText.getId(), ConstraintSet.TOP, nText.getId(), ConstraintSet.BOTTOM, 0);
        s.connect(aText.getId(), ConstraintSet.START, nameG.getId(), ConstraintSet.START, 0);
        s.connect(aText.getId(), ConstraintSet.TOP, pText.getId(), ConstraintSet.BOTTOM, 0);
        s.applyTo(CustomerInfo);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

    public View create_view(){
        View view = new View(page);
        view.setMinimumHeight(dip2px(maxLayout, 1));
        view.setBackgroundColor(Color.rgb(152, 152, 152));
        return view;
    }

    protected Guideline newGuideline(int kind){
        Guideline G = new Guideline(page);
        ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        if(kind == 1){
            G.setId(R.id.time_guideline_id);
            p.guideBegin = dip2px(maxLayout, 10);
        }
        else if(kind == 2){
            G.setId(R.id.name_guideline_id);
            p.guidePercent = 0.22f;
        }
        else if(kind == 3){
            G.setId(R.id.icon_guideline_id);
            p.guidePercent = 0.80f;
        }
        p.orientation = ConstraintLayout.LayoutParams.VERTICAL;
        G.setLayoutParams(p);
        return G;
    }

    protected TextView newdtText(String datetime){
        TextView dtText = new TextView(page);//日期

        dtText.setId(R.id.datetime_id);
        dtText.setText(datetime);
        dtText.setTextSize(25);
        dtText.setTypeface(null, Typeface.BOLD);
        dtText.setTextColor(setTimeColor());

        return dtText;
    }

    protected TextView newtText(String time){
        TextView tText = new TextView(page);//時間

        tText.setId(R.id.time_id);
        tText.setText(time);
        tText.setTextSize(20);
        tText.setTextColor(setTimeColor());

        return tText;
    }
    protected abstract int setTimeColor();

    protected TextView newNameText(String name){
        TextView nText = new TextView(page);//人名

        nText.setId(R.id.name_id);
        nText.setText(name);
        nText.setTextSize(25);
        nText.setTextColor(setNameColor());

        return nText;
    }
    protected abstract int setNameColor();

    protected TextView newNoFormatText(String words, String type){
        TextView T = new TextView(page);
        if(type.equals("nametitle")) T.setId(R.id.nametitle_id);
        else if(type.equals("phone")) T.setId(R.id.phone_id);
        else T.setId(R.id.address_id);
        T.setText(words);
        T.setTextColor(setNoFormatColor(T));
        return T;
    }
    protected abstract int setNoFormatColor(TextView T);
}
