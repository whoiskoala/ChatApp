package com.e.chatapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.e.chatapp.User_package.Friendlist_item;

import java.util.ArrayList;
import java.util.List;

public class Nearby_user extends AppCompatActivity {

    private List<Friendlist_item> friendList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_user);

        
        initItems();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Friend_list_Adapter adapter = new Friend_list_Adapter(friendList);
        recyclerView.setAdapter(adapter);

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    private void initItems() {
        for (int i = 0; i < 10; i++) {
            Friendlist_item portrait = new Friendlist_item("User1", R.drawable.icon_user);
            friendList.add(portrait);
            Friendlist_item portrait2 = new Friendlist_item("User2", R.drawable.icon_user);
            friendList.add(portrait2);
        }
    }
}
