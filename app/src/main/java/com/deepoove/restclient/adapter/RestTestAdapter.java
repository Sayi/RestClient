package com.deepoove.restclient.adapter;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deepoove.restclient.R;
import com.deepoove.restclient.model.RestTest;

import java.util.List;

/**
 * @author Sayi
 */

public class RestTestAdapter extends RecyclerView.Adapter<RestTestAdapter.MyViewHolder> {

    public List<RestTest> datas = null;

    public RestTestAdapter(List<RestTest> datas) {
        this.datas = datas;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_rest_test, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RestTest restTest = datas.get(position);
        RoundRectShape shape = new RoundRectShape(new float[]{8, 8, 8, 8, 8, 8, 8, 8}, null, null);
        ShapeDrawable drawable = new ShapeDrawable(shape);

        String color = "6bbd5b";
        switch (restTest.getMethod().toUpperCase()) {
            case "GET":
                break;
            case "POST":
                color = "248fb2";
                break;
            case "DELETE":
                color = "e27a7a";
                break;
            case "PUT":
                color = "9b708b";
                break;
        }
        drawable.getPaint().setColor(Color.parseColor("#" + color));
        holder.tv.setBackground(drawable);
        holder.tv.setText(restTest.getMethod().toUpperCase());

        holder.spec.setText(restTest.getUrl());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView tv;
        TextView spec;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.requestMethod);
            spec = (TextView) view.findViewById(R.id.requestURI);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, R.id.rest_copy_request, 0, "Copy");
            menu.add(0, R.id.rest_delete, 0, "Remove");//groupId, itemId, order, title
            menu.add(0, R.id.rest_sort_up, 0, "Move Up");
            menu.add(0, R.id.rest_sort_down, 0, "Move Down");
        }
    }
}
