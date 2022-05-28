package com.ivan.iChat.ichat_android.utils;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ivan.iChat.ichat_android.R;

import java.util.ArrayList;

import com.ivan.iChat.ichat_android.model.Message;

import static com.ivan.iChat.ichat_android.utils.Constants.*;


public class MssgsAdapter extends RecyclerView.Adapter<MssgsAdapter.ViewHolder> {

    private ArrayList<Message> messages;
    private static int text_view_id;

    public MssgsAdapter(ArrayList<Message> messages) {
        this.messages = messages;
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
        if (messages.get(position).isOwn()) return MESSAGE_OUT;
        else return MESSAGE_IN;
    }

    @Override // Replace de contents of a View/item (invoked by the layout manager)
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.messageTextView.setText(messages.get(position).getContent());
    }

    @Override
    public int getItemCount() { return messages.size(); }

}
