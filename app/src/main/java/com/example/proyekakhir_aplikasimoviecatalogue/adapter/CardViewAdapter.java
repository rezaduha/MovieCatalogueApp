package com.example.proyekakhir_aplikasimoviecatalogue.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyekakhir_aplikasimoviecatalogue.Data;
import com.example.proyekakhir_aplikasimoviecatalogue.R;
import com.example.proyekakhir_aplikasimoviecatalogue.activity.DetailActivity;
import com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite;

import java.util.ArrayList;
import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewViewHolder> {

    private Context context;
    private ArrayList<Data> mData = new ArrayList<>();
    private List<Favorite> listFavorite;

    public void setData(ArrayList<Data> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public ArrayList<Data> getmData() {
        return mData;
    }

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
    public void onBindViewHolder(@NonNull CardViewAdapter.CardViewViewHolder holder, final int position) {
        if (getListFavorite() != null) {
            holder.tvName.setText(getListFavorite().get(position).getName());
            holder.tvDescription.setText(getListFavorite().get(position).getDescription());
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w500/" + getListFavorite().get(position).getPhoto())
                    .placeholder(R.drawable.ic_burst_image)
                    .error(R.drawable.ic_broken_image)
                    .into(holder.imgPhoto);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_DATA_FAVORITE, getListFavorite().get(position));
                    context.startActivity(intent);
                }
            });
        } else if (getmData() != null) {
            if (getmData().get(position).getMovieName() != null) {
                holder.tvName.setText(getmData().get(position).getMovieName());
            } else if (getmData().get(position).getTvshowName() != null) {
                holder.tvName.setText(getmData().get(position).getTvshowName());
            }
            holder.bind(mData.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_DATA, getmData().get(position));
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int size;
        if (getListFavorite() != null) {
            size = getListFavorite().size();
        } else if (getmData() != null) {
            size = getmData().size();
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

        void bind(Data data) {
            tvDescription.setText(data.getDescription());

            String url_image = "https://image.tmdb.org/t/p/w500/" + data.getPhoto();

            Glide.with(itemView.getContext())
                    .load(url_image)
                    .placeholder(R.drawable.ic_burst_image)
                    .error(R.drawable.ic_broken_image)
                    .into(imgPhoto);
        }
    }
}
