package com.e.chatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.chatapp.User_package.Friendlist_item;

import java.util.List;

public class Friend_list_Adapter extends RecyclerView.Adapter<Friend_list_Adapter.ViewHolder> {
    private List<Friendlist_item> mFriendList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View friendView;
        ImageView friendImage;
        TextView friendName;

        public ViewHolder(View view) {
            super(view);
            friendView = view;
            friendImage = view.findViewById(R.id.friend_portrait);
            friendName = view.findViewById(R.id.friend_name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.friendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Friendlist_item friend_item = mFriendList.get(position);
                Toast.makeText(view.getContext(), "You click View"+ friend_item.getFriendName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.friendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Friendlist_item friend_item = mFriendList.get(position);
                Toast.makeText(view.getContext(), "You click View"+ friend_item.getFriendName(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friendlist_item friend_item = mFriendList.get(position);
        holder.friendImage.setImageResource(friend_item.getImageId());
        holder.friendName.setText(friend_item.getFriendName());
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    public Friend_list_Adapter(List<Friendlist_item> friendList) {
        mFriendList = friendList;
    }

}