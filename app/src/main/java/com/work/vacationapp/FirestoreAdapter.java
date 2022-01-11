package com.work.vacationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FirestoreAdapter extends FirestorePagingAdapter<Advertisement, FirestoreAdapter.AdvertisementViewHolder> {

    public OnListItemClick onListItemClick;

    public FirestoreAdapter(@NonNull @NotNull FirestorePagingOptions<Advertisement> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull AdvertisementViewHolder holder, int position, @NonNull @NotNull Advertisement model) {
        holder.textViewName.setText(Objects.requireNonNull(model.getName()));
        Glide.with(holder.itemView.getContext()).load(model.getPhoto1()).centerCrop().placeholder(R.drawable.ic_baseline_image_24).into(holder.image);

    }

    @NonNull
    @NotNull
    @Override
    public AdvertisementViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertisement,parent,false);
        return new AdvertisementViewHolder(view);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull @NotNull LoadingState state) {

        super.onLoadingStateChanged(state);
        switch (state){
            case LOADING_MORE:
            case LOADED:
            case FINISHED:
            case ERROR:
                break;
        }
    }

    public class AdvertisementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textViewName;
        private final ImageView image;

        public AdvertisementViewHolder(@NonNull @NotNull  View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick{
        void onItemClick(DocumentSnapshot snapshot, int position);
    }
}
