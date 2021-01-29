package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Model.TicketChatModel;
import com.crypto.croytowallet.R;

import java.util.ArrayList;
import java.util.List;

public class TickectChatAdapter extends  RecyclerView.Adapter<TickectChatAdapter.ViewHolder>{
    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private ArrayList<TicketChatModel> mChat;

    public TickectChatAdapter(Context mContext, ArrayList<TicketChatModel> mChat) {
        this.mContext = mContext;
        this.mChat = mChat;
    }

    public TickectChatAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new TickectChatAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new TickectChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.show_message.setText(mChat.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
      return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
           // profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
       // fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getRoleId().equals("Customer") ){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
