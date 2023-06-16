package com.moutamid.controlsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.controlsapp.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotiVH> {

    Context context;
    ArrayList<String> list;

    public NotificationAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public NotiVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotiVH(LayoutInflater.from(context).inflate(R.layout.notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotiVH holder, int position) {
        String text = list.get(holder.getAdapterPosition());
        holder.textView.setText(text);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NotiVH extends RecyclerView.ViewHolder{
        TextView textView;
        public NotiVH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }

}
