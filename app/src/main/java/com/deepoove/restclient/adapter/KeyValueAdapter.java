package com.deepoove.restclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepoove.restclient.R;
import com.deepoove.restclient.model.RestTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Sayi
 */

public class KeyValueAdapter extends RecyclerView.Adapter<KeyValueAdapter.MyViewHolder> {

    public List<RestTest.KeyValue> datas;

    public KeyValueAdapter(List<RestTest.KeyValue> datas) {
        this.datas = datas;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.action_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datas.remove(position);
                notifyDataSetChanged();
            }
        });
        RestTest.KeyValue keyValue = datas.get(position);
        holder.content.setText(keyValue.getKey() + " = " + (null == keyValue.getValue() ? "" : keyValue.getValue()));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView indicator;
        TextView content;

        public MyViewHolder(View view) {
            super(view);
            indicator = (ImageView) view.findViewById(R.id.item_remove);
            content = (TextView) view.findViewById(R.id.content);

        }
    }

}
