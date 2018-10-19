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
import com.cooloongwu.helper.like.LikeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CooLoongWu on 2018-2-1 13:43.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<MainBean> beans = new ArrayList<>();

    public MainAdapter() {
        MainBean mainBean1 = new MainBean("跳一跳",
                "手机需已获取root权限，否则无法使用", "2018-01-03", JumpActivity.class);
        beans.add(mainBean1);

        MainBean mainBean2 = new MainBean("旅行青蛙",
                "手机需已获取root权限，否则无法使用", "2018-02-01", FrogActivity.class);
        beans.add(mainBean2);

        MainBean mainBean3 = new MainBean("淘宝点赞",
                "手机需已获取root权限，否则无法使用", "2018-10-17", LikeActivity.class);
        beans.add(mainBean3);
    }

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

        holder.name.setText(beans.get(position).getTitle());
        holder.content.setText(beans.get(position).getContent());
        holder.time.setText(beans.get(position).getTime());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (position == 1) {
//                    Toast.makeText(holder.itemView.getContext(), "暂未开放", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                Intent intent = new Intent();
                intent.setClass(holder.itemView.getContext(), beans.get(position).getClazz());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ic;
        TextView name;
        TextView content;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            ic = itemView.findViewById(R.id.img_ic);
            name = itemView.findViewById(R.id.text_name);
            content = itemView.findViewById(R.id.text_content);
            time = itemView.findViewById(R.id.text_time);
        }
    }

}
