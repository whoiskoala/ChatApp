package com.e.chatapp.ui.friendlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.chatapp.Friend_list_Adapter;
import com.e.chatapp.R;
import com.e.chatapp.User_package.Friendlist_item;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendlistFragment extends Fragment {

    private FriendlistViewModel friendlistViewModel;
    private List<Friendlist_item> friendList = new ArrayList<>();

    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        friendlistViewModel =
//                ViewModelProviders.of(this).get(FriendlistViewModel.class);
        assert inflater != null;
        View root = inflater.inflate(R.layout.fragment_friends_list, container, false);

        super.onCreate(savedInstanceState);

        initItems();
        RecyclerView recyclerView =(RecyclerView) root.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        Friend_list_Adapter adapter = new Friend_list_Adapter(friendList);
        recyclerView.setAdapter(adapter);

//        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
//            @Override
//            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
//                return new ClassicsHeader(context);
//            }
//        });
        return root;
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