package com.ivan.iChat.ichat_android.utils;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ivan.iChat.ichat_android.R;

import java.util.ArrayList;

import com.ivan.iChat.ichat_android.model.Mensaje;
import com.ivan.iChat.ichat_android.model.User;

import static com.ivan.iChat.ichat_android.utils.Constants.*;


public class MssgsAdapter extends RecyclerView.Adapter<MssgsAdapter.ViewHolder> {

    private ArrayList<Mensaje> messages;
    private User user;
    private static int text_view_id;

    public MssgsAdapter(ArrayList<Mensaje> messages, User user) {
        this.messages = messages;
        this.user = user;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;

        public ViewHolder(View messageView) {
            super(messageView);

            this.messageTextView = messageView.findViewById(text_view_id);
        }
    }

    @Override // Create new Views/items
    public MssgsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == MESSAGE_OUT) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_out_layout, parent, false);
            text_view_id = R.id.chat_message_out_textView;
        }
        else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_in_layout, parent, false);
            text_view_id = R.id.chat_message_in_textView;
        }
        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {

        if (messages.get(position).getSender() == user.getId()) return MESSAGE_OUT;
        else return MESSAGE_IN;
    }

    @Override // Replace the content of a View/item (invoked by the layout manager)
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getItemViewType() == MESSAGE_OUT)
            holder.messageTextView.setText(messages.get(position).getContenido());
        else
            holder.messageTextView.setText(messages.get(position).getSender() + " > " + messages.get(position).getContenido());
    }

    @Override
    public int getItemCount() { return messages.size(); }

}
