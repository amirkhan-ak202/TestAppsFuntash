package com.example.testappsfuntash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testappsfuntash.Constants;
import com.example.testappsfuntash.Model.SongModel;
import com.example.testappsfuntash.R;

import java.util.List;

public class SongAdapter  extends RecyclerView.Adapter<SongAdapter.MyViewHolder>
{
    List<SongModel.Datum> mlistsong;
    Context context;
    private SongItemClickListener sItemClickListener;

    public SongAdapter(List<SongModel.Datum> mlistsong, Context context) {
        this.mlistsong = mlistsong;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item_list,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textsong.setText(mlistsong.get(position).getTitle());
        Glide.with(context).load(R.drawable.baseline_music_note_24).into(holder.imgs);

    }

    @Override
    public int getItemCount() {
        return mlistsong.size();
    }

    public void setSongItemClickListener(SongItemClickListener songItemClickListener){
        this.sItemClickListener = songItemClickListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgs;
        TextView textsong;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgs = itemView.findViewById(R.id.mother_day);
            textsong = itemView.findViewById(R.id.textsong);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (sItemClickListener != null){
                sItemClickListener.onClick(v,getAdapterPosition());
            }
        }
    }
    public interface  SongItemClickListener{
        void onClick(View v,int position);
    }
}
