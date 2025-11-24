package com.example.kevin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kevin.R;
import com.example.kevin.models.Message;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TEXT = 1;
    private static final int TYPE_IMAGE = 2;

    public final ArrayList<Message> messageList = new ArrayList<>();
    private final String myUid;
    private final Context context;

    public MessageAdapter(Context context, String myUid) {
        this.context = context;
        this.myUid = myUid;
    }

    @Override
    public int getItemViewType(int position) {
        Message m = messageList.get(position);
        if (m.getImageUrl() != null && !m.getImageUrl().isEmpty()) {
            return TYPE_IMAGE;
        }
        return TYPE_TEXT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TEXT) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_text, parent, false);
            return new TextHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_image, parent, false);
            return new ImageHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message msg = messageList.get(position);

        if (holder instanceof TextHolder) {
            ((TextHolder) holder).bind(msg);
        } else if (holder instanceof ImageHolder) {
            ((ImageHolder) holder).bind(msg);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void addMessage(Message m) {
        messageList.add(m);
        notifyItemInserted(messageList.size() - 1);
    }

    private String formatTime(long time) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(new Date(time));
    }

    class TextHolder extends RecyclerView.ViewHolder {

        TextView txtSender, txtBody, txtTime;

        public TextHolder(@NonNull View itemView) {
            super(itemView);

            txtSender = itemView.findViewById(R.id.txtSender);
            txtBody = itemView.findViewById(R.id.txtBody);
            txtTime = itemView.findViewById(R.id.txtTime);
        }

        public void bind(Message m) {
            txtSender.setText(m.getSenderName());
            txtBody.setText(m.getText());
            txtTime.setText(formatTime(m.getTimestamp()));
        }
    }

    class ImageHolder extends RecyclerView.ViewHolder {

        TextView txtSenderImg, txtTimeImg;
        ImageView imgMessage;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);

            txtSenderImg = itemView.findViewById(R.id.txtSenderImg);
            txtTimeImg = itemView.findViewById(R.id.txtTimeImg);
            imgMessage = itemView.findViewById(R.id.imgMessage);
        }

        public void bind(Message m) {
            txtSenderImg.setText(m.getSenderName());
            txtTimeImg.setText(formatTime(m.getTimestamp()));

            if (m.getImageUrl() != null && !m.getImageUrl().isEmpty()) {
                Picasso.get().load(m.getImageUrl()).into(imgMessage);
            }
        }
    }
}
