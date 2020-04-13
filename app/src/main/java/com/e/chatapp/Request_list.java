package com.e.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.chatapp.User_package.Friendlist_item;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Request_list extends AppCompatActivity {

    private RecyclerView requestList;
    private DatabaseReference RequestRef,UserRef, ContactRef;
    private FirebaseAuth Auth;
    private String currentuser;

    public Request_list(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        RequestRef = FirebaseDatabase.getInstance().getReference().child("Request");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ContactRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        Auth = FirebaseAuth.getInstance();
        currentuser = Auth.getCurrentUser().getUid();
        requestList = (RecyclerView) findViewById(R.id.request_recycler_view);
        requestList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<Friendlist_item> options =
                new FirebaseRecyclerOptions.Builder<Friendlist_item>()
                .setQuery(RequestRef.child(currentuser), Friendlist_item.class)
                .build();

        FirebaseRecyclerAdapter<Friendlist_item, RequestListViewHolder> adapter =
                new FirebaseRecyclerAdapter<Friendlist_item, RequestListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final RequestListViewHolder holder, final int position, @NonNull final Friendlist_item model) {
//                        holder.itemView.findViewById(R.id.btn_accept).setVisibility(View.VISIBLE);
//                        holder.itemView.findViewById(R.id.btn_cancel).setVisibility(View.VISIBLE);
                        final String user_ID = getRef(position).getKey();
                        DatabaseReference getTypeRef = getRef(position).child("request_type").getRef();
                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    System.out.println("333333333333333333333333");
                                    String type = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                                    if (type.equals("received")){
                                        System.out.println("222222222222222222222222222 "+type);
                                        UserRef.child(user_ID).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild("image")){
                                                    String RequestUserImage = dataSnapshot.child("image").getValue().toString();

                                                    Picasso.get().load(RequestUserImage).placeholder(R.drawable.icon_user).into(holder.image);
                                                }
                                                final String RequestUserName = dataSnapshot.child("username").getValue().toString();
                                                String RequestUserEmail = dataSnapshot.child("email").getValue().toString();

                                                holder.username.setText(RequestUserName);
                                                holder.email.setText("friend Request");

                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        CharSequence options[] = new  CharSequence[]{
                                                                "Accept","Cancel"
                                                        };
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Request_list.this);
                                                        builder.setTitle(RequestUserName+" request to be your friend");

                                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if (which == 0){
                                                                    ContactRef.child(currentuser).child(user_ID).child("Contacts")
                                                                            .setValue("Friends").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()){
                                                                                RequestRef.child(currentuser).child(user_ID)
                                                                                        .removeValue()
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()){
                                                                                                    RequestRef.child(user_ID).child(currentuser)
                                                                                                            .removeValue()
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    if (task.isSuccessful()){
                                                                                                                        Toast.makeText(Request_list.this, "Save as friend", Toast.LENGTH_LONG).show();
                                                                                                                    }
                                                                                                                }
                                                                                                            });
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                                if (which == 1){
                                                                    RequestRef.child(currentuser).child(user_ID)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()){
                                                                                        RequestRef.child(user_ID).child(currentuser)
                                                                                                .removeValue()
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()){
                                                                                                            Toast.makeText(Request_list.this, "Cancel request", Toast.LENGTH_LONG).show();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                                        builder.show();
                                                    }
                                                });
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public RequestListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_list_item,viewGroup,false);
                        RequestListViewHolder holder = new RequestListViewHolder (view);
                        return holder;
                    }
                };
        requestList.setAdapter(adapter);

        adapter.startListening();
    }

    public static class RequestListViewHolder extends RecyclerView.ViewHolder{
        TextView username, email;
        ImageView image;
        Button btn_accept, btn_cancel;

        public RequestListViewHolder (@NonNull View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.friend_name);
            email = itemView.findViewById(R.id.friend_email);
            image = itemView.findViewById(R.id.friend_portrait);
            btn_accept = itemView.findViewById(R.id.btn_accept);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
        }
    }

}
