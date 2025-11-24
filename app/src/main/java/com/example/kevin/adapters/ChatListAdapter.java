package com.example.kevin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kevin.R;
import com.example.kevin.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.UserVH> {

    public interface OnUserClick {
        void onClick(User user);
    }

    private final List<User> users;
    private final OnUserClick listener;

    public ChatListAdapter(List<User> users, OnUserClick listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_preview, parent, false);
        return new UserVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserVH holder, int position) {
        User u = users.get(position);
        holder.name.setText(u.getName());
        holder.lastMsg.setText(u.getEmail()); // mostramos el correo como preview
        holder.time.setText(""); // opcional: puedes mostrar "En línea" o dejar vacío
        holder.unread.setText(""); // opcional: puedes mostrar "Nuevo" si quieres

        if (!u.getPhotoUrl().isEmpty()) {
            Picasso.get().load(u.getPhotoUrl()).placeholder(R.drawable.profile_placeholder).into(holder.photo);
        } else {
            holder.photo.setImageResource(R.drawable.profile_placeholder);
        }

        holder.itemView.setOnClickListener(v -> listener.onClick(u));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserVH extends RecyclerView.ViewHolder {
        TextView name, lastMsg, time, unread;
        ImageView photo;

        UserVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtChatName);
            lastMsg = itemView.findViewById(R.id.txtLastMsg);
            time = itemView.findViewById(R.id.txtTime);
            unread = itemView.findViewById(R.id.txtUnread);
            photo = itemView.findViewById(R.id.imgPhoto);
        }
    }
}
