package com.e.chatapp.ui.notifications;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.chatapp.Chat;
import com.e.chatapp.R;
import com.e.chatapp.User_package.Friendlist_item;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsFragment extends Fragment {

    private View root;
    private RecyclerView chatList;

    private DatabaseReference ChatRef, UserRef;
    private FirebaseAuth Auth;
    private String currentuser;

    private String Image = "user_image";

    public NotificationsFragment() {

    }

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        root = inflater.inflate(R.layout.fragment_notifications, container, false);

        Auth = FirebaseAuth.getInstance();
        currentuser = Auth.getCurrentUser().getUid();
        ChatRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentuser);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        chatList = (RecyclerView) root.findViewById(R.id.chat_list_recycler_view);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Friendlist_item> options =
                new FirebaseRecyclerOptions.Builder<Friendlist_item>()
                        .setQuery(ChatRef, Friendlist_item.class)
                        .build();

        FirebaseRecyclerAdapter<Friendlist_item, ChatListHolder> adapter =
                new FirebaseRecyclerAdapter<Friendlist_item, ChatListHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatListHolder chatListHolder, int position, @NonNull Friendlist_item friendlist_item) {
                        final String userID = getRef(position).getKey();
                        final String[] Image = {"user_image"};

                        UserRef.child(userID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    if (dataSnapshot.hasChild("image")) {
                                        Image[0] = dataSnapshot.child("image").getValue().toString();
                                        Picasso.get().load(Image[0]).into(chatListHolder.image);
                                    }
                                    final String Name = dataSnapshot.child("username").getValue().toString();
                                    final String Email = dataSnapshot.child("email").getValue().toString();

                                    chatListHolder.username.setText(Name);
                                    chatListHolder.email.setText("Chat");

                                    chatListHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent chatIntent = new Intent(getContext(), Chat.class);
                                            chatIntent.putExtra("user_ID", userID);
                                            chatIntent.putExtra("user_Name", Name);
                                            chatIntent.putExtra("user_Image", Image[0]);
                                            startActivity(chatIntent);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.friend_list_item, parent, false);
                        return new ChatListHolder(view);
                    }
                };
        chatList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ChatListHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView email, username;

        public ChatListHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.friend_portrait);
            username = itemView.findViewById(R.id.friend_name);
            email = itemView.findViewById(R.id.friend_email);
        }
    }
}