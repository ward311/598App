package com.example.homerenting_prototype_one.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.SwipeDeleteAdapter;
import com.example.homerenting_prototype_one.adapter.TextAdapter;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RecyclerViewAction extends ItemTouchHelper.Callback {
    Context context;
    SwipeDeleteAdapter s_adapter;
    TextAdapter t_adapter;
    int current = 0;
    String TAG = "RecyclerViewAction";

    public RecyclerViewAction(Context context, SwipeDeleteAdapter adapter){
        this.context = context;
        s_adapter = adapter;
        current = 1;
    }

    public RecyclerViewAction(Context context, TextAdapter adapter){
        this.context = context;
        t_adapter = adapter;
        current = 2;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("確定要刪除?");
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (current) {
                    case 1:
                        s_adapter.deleteItem(position);
                        break;
                    case 2:
                        t_adapter.deleteItem(position);
                        break;
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (current) {
                    case 1:
                        s_adapter.notifyItemChanged(position);
                        break;
                    case 2:
                        t_adapter.notifyItemChanged(position);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
        String warning_color = "#B63434";
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addBackgroundColor(Color.parseColor(warning_color))
                .addActionIcon(R.drawable.ic_baseline_delete_24)
                .create()
                .decorate();
    }
}
