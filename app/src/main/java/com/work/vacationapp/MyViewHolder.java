package com.work.vacationapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewHolder {

    TextView nameTxt;
    ImageView img;

    public MyViewHolder(View itemView) {

        nameTxt= (TextView) itemView.findViewById(R.id.name);
        img= (ImageView) itemView.findViewById(R.id.image);


    }
}
