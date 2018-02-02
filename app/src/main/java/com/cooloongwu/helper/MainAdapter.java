package com.cooloongwu.helper;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooloongwu.helper.frog.FrogActivity;
import com.cooloongwu.helper.jump.JumpActivity;

/**
 * Created by CooLoongWu on 2018-2-1 13:43.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final static int[] ic = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_frog
    };
    private final static String[] name = {
            "跳一跳",
            "旅行青蛙"
    };

    private final static Class[] clas = {
            JumpActivity.class,
            FrogActivity.class
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_ic,
                parent,
                false
        );
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.ic.setImageResource(ic[position]);
        holder.name.setText(name[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (position == 1) {
//                    Toast.makeText(holder.itemView.getContext(), "暂未开放", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                Intent intent = new Intent();
                intent.setClass(holder.itemView.getContext(), clas[position]);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ic;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            ic = itemView.findViewById(R.id.img_ic);
            name = itemView.findViewById(R.id.text_name);
        }
    }

}
