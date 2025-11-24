package com.example.kevin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kevin.R;
import com.example.kevin.models.MessageModel;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<MessageModel> list;
    String myUid;

    private static final int TYPE_TEXT = 1;
    private static final int TYPE_IMAGE = 2;

    public ChatAdapter(Context ctx, ArrayList<MessageModel> list, String myUid) {
        this.context = ctx;
        this.list = list;
        this.myUid = myUid;
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel msg = list.get(position);
        if (msg.getImageUrl() != null && !msg.getImageUrl().isEmpty())
            return TYPE_IMAGE;
        return TYPE_TEXT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TEXT) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_message_text, parent, false);
            return new TextHolder(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.item_message_image, parent, false);
            return new ImageHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {
        MessageModel msg = list.get(position);
        boolean isMine = msg.getSenderId().equals(myUid);

        if (h instanceof TextHolder holder) {
            holder.body.setText(msg.getMessage());
            holder.body.setTextAlignment(isMine ? View.TEXT_ALIGNMENT_VIEW_END : View.TEXT_ALIGNMENT_VIEW_START);
        } else if (h instanceof ImageHolder holder) {
            Glide.with(context)
                    .load(msg.getImageUrl())
                    .into(holder.image);
            holder.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class TextHolder extends RecyclerView.ViewHolder {
        TextView body;

        public TextHolder(@NonNull View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.txtBody); // ← ID correcto del XML
        }
    }

    static class ImageHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgMessage); // ← ID correcto del XML
        }
    }
}
