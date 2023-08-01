package com.example.socketclientchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> messages;

    public ChatAdapter(Context context, ArrayList<String> messages) {
        super(context, 0, messages);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_chat_message, null);
        }

        String message = messages.get(position);

        TextView textViewMessage = view.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        return view;
    }
}

