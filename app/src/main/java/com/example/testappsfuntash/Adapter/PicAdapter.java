package com.example.testappsfuntash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testappsfuntash.Constants;
import com.example.testappsfuntash.Model.Model_Adapter;
import com.example.testappsfuntash.Model.picModel;
import com.example.testappsfuntash.R;

import java.util.List;

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.MyViewHolder>
{
    List<picModel.Datum> mlist;
    Context context;
    private PicAdapter.picclickListner pclicklistner;

    public PicAdapter(List<picModel.Datum> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pic_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(Constants.images_path+mlist.get(position).getFile())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public void setPicclickListner(picclickListner picclickListner){
        this.pclicklistner = picclickListner;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView imageView;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imgs);
        itemView.setOnClickListener(this);
    }

        @Override
        public void onClick(View v) {
            if (pclicklistner != null){
                pclicklistner.onClick(v,getAdapterPosition());
            }
        }

    }

    public interface  picclickListner{
        void onClick(View v,int position);
    }

}
