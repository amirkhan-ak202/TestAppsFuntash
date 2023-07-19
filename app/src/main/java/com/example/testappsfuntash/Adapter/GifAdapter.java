package com.example.testappsfuntash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testappsfuntash.Constants;
import com.example.testappsfuntash.Model.Gif_Model;
import com.example.testappsfuntash.Model.picModel;
import com.example.testappsfuntash.R;

import java.util.List;

public class GifAdapter extends RecyclerView.Adapter<GifAdapter.ViewHolder>
{
    List<Gif_Model.Datum> mlist;
    Context context;
    private GifClickListener gfclicklistener;

    public GifAdapter(List<Gif_Model.Datum> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gif_itemlist,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(Constants.images_path+mlist.get(position).getFile())
                .into(holder.gifImage);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
    public void setGfclicklistener(GifClickListener gifClickListener){
        this.gfclicklistener = gifClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView gifImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gifImage = itemView.findViewById(R.id.imgs_gif);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (gfclicklistener != null){
                gfclicklistener.onClick(v,getAdapterPosition());
            }
        }
    }
    public interface  GifClickListener{
        void onClick(View v,int position);
    }
}
