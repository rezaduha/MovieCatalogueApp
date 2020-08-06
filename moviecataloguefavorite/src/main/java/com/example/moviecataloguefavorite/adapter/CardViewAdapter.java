package com.example.moviecataloguefavorite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviecataloguefavorite.R;
import com.example.moviecataloguefavorite.db.Favorite;

import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewViewHolder> {

    private Context context;
    private List<Favorite> listFavorite;

    public List<Favorite> getListFavorite() {
        return listFavorite;
    }

    public void setListFavorite(List<Favorite> listFavorite) {
        this.listFavorite = listFavorite;
    }

    public CardViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview, viewGroup, false);
        return new CardViewViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewAdapter.CardViewViewHolder holder, final int position) {
        holder.bind(listFavorite.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, listFavorite.get(position).getName() + ", " + context.getString(R.string.toast_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        int size;
        if (getListFavorite() != null) {
            size = getListFavorite().size();
        } else {
            size = 0;
        }
        return size;
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvDescription;
        ImageView imgPhoto;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvDescription = itemView.findViewById(R.id.tv_item_description);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
        }

        void bind(Favorite favorite) {
            tvName.setText(favorite.getName());
            tvDescription.setText(favorite.getDescription());
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w500/" + favorite.getPhoto())
                    .placeholder(R.drawable.ic_burst_image)
                    .error(R.drawable.ic_broken_image)
                    .into(imgPhoto);
        }
    }
}
