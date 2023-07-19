package com.example.testappsfuntash.Adapter;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.testappsfuntash.Constants;
import com.example.testappsfuntash.Model.VideoModel;
import com.example.testappsfuntash.R;

import java.util.List;

public class videosAdapter extends RecyclerView.Adapter<videosAdapter.MyViewHolder>
{
    List<VideoModel.Datum> list;
    Context context;
    private VidesclickListner videsclickListner;



    public videosAdapter(List<VideoModel.Datum> list,Context context) {
        this.list = list;
        this.context = context;
    }

    private MyAdapter.itemclickListner clicklistner;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_item_list,parent,false);
        return  new videosAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull videosAdapter.MyViewHolder holder, int position) {

        long thumb =holder.getLayoutPosition()*1000;
        RequestOptions options = new RequestOptions().frame(thumb);
        Glide.with(context).load(Constants.images_path+list.get(position).getFile()).apply(options).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videsclickListner.onClick(v,position);


            }
        });
        holder.sharevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videsclickListner.onSaveClick(v,position);
            }
        });
        holder.save_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videsclickListner.onShareClick(v,position);
            }
        });

//        Glide.with(context)
//                .load(list.get(position).getVideos())
//                .into(holder.videoView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setVidesclickListner(VidesclickListner videsclickListner){
        this.videsclickListner = videsclickListner;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       ImageView imageView;
       ImageView sharevideo;
       ImageView save_video;
       ImageView imgsplay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pic_image);
            sharevideo = itemView.findViewById(R.id.ic_sharebtn);
            save_video = itemView.findViewById(R.id.savebtn);
            imgsplay = itemView.findViewById(R.id.playbtn);


            imageView.setOnClickListener(this);
            //itemView.setOnClickListener(this);
            sharevideo.setOnClickListener(this);
            save_video.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {


        }
    }

    public interface  VidesclickListner{
        void onClick(View v,int position);
        void onSaveClick(View v,int position);
        void onShareClick(View v,int position);
    }
}
