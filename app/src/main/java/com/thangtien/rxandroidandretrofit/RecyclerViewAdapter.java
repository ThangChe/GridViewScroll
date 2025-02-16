package com.thangtien.rxandroidandretrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    String TAG = Constant.PERFIX + "RecyclerViewAdapter";
    private final List<Item> items;
    private final Context context;
    private final EventListener eventListener;
    public RecyclerViewAdapter(Context context, List<Item> items, EventListener listener){
        this.context = context;
        this.items = items;
        this.eventListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Item item = items.get(position);
        holder.checkBox.setChecked(item.isChecked());
        holder.textView.setText(item.getText());
        holder.viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"onClick: position: " + position);
                eventListener.onClick(position);
            }
        });

        holder.viewLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i(TAG,"onLongClick: position: " + position);
                eventListener.onLongClick(position);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View viewLayout;
        CheckBox checkBox;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewLayout = itemView.findViewById(R.id.layout_recyclerview);
            checkBox = itemView.findViewById(R.id.item_checkbox);
            textView = itemView.findViewById(R.id.item_textview);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void selectItem(int position) {
        if (position >= 0 && position < items.size()) {
            Item item = items.get(position);
            item.setChecked(!item.isChecked());
            notifyDataSetChanged();
        }
    }

    interface EventListener{
        void onClick(int position);
        void onLongClick(int position);
    }
}
