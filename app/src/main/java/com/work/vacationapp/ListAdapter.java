package com.work.vacationapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class ListAdapter extends ArrayAdapter<Add> {

    Context context;
    int layoutResourceId;
    List<Add> data = null;

    public ListAdapter(@NonNull Context context,int resource,ArrayList<Add> objects) {
        super(context,resource,objects);
        this.layoutResourceId = resource;
        this.context = context;
        this.data = objects;
    }


    static class DataHolder{
        ImageView imageView;
        TextView textView;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataHolder holder = null;
        if(convertView==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId,parent,false);
            holder = new DataHolder();
            holder.textView = convertView.findViewById(R.id.name);
            holder.imageView = convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }else{
            holder = (DataHolder)convertView.getTag();
        }
        Add a = getItem(position);
        holder.textView.setText(a.getName());
        Picasso.get().load(a.getPhoto1()).into(holder.imageView);

        return convertView;
    }
}