package com.example.testappsfuntash.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.testappsfuntash.Constants;
import com.example.testappsfuntash.Model.CategoryModel;
import com.example.testappsfuntash.Model.Model_Adapter;
import com.example.testappsfuntash.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    List<CategoryModel.Datum> list;
    Context context;
    private itemclickListner clicklistner;


    public MyAdapter(List<CategoryModel.Datum> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_picture,parent,false);
     return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.text1.setText(list.get(position).getTitle());
        Glide.with(context)
                .load(Constants.images_path+list.get(position).getFile())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setClicklistner(itemclickListner itemclickListner){
        this.clicklistner = itemclickListner;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text1;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
            imageView = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (clicklistner != null){
                clicklistner.onClick(v,getAdapterPosition());
            }
        }
    }
    public interface  itemclickListner{
        void onClick(View v,int position);
    }
}
