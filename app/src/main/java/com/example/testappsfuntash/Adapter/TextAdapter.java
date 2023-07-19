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
import com.example.testappsfuntash.Model.Text_Model;
import com.example.testappsfuntash.R;

import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder>
{
    List<Text_Model.Datum> mlist;
    Context context;
    private TextClicklistener textclicklistner;

    public TextAdapter(List<Text_Model.Datum> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_item_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text_title.setText(mlist.get(position).getTitle());
        Glide.with(context).load(Constants.images_path+mlist.get(position).getFile()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
    public void setClicklistner(TextClicklistener itemclickListner){
        this.textclicklistner = itemclickListner;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView text_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            text_title = itemView.findViewById(R.id.text1);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (textclicklistner != null){
                textclicklistner.onClick(v,getAdapterPosition());
            }
        }
    }
    public interface  TextClicklistener{
        void onClick(View v,int position);
    }
}
