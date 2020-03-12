package com.e.chatapp.ui.friendlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.e.chatapp.R;

public class FriendlistFragment extends Fragment {

    private FriendlistViewModel friendlistViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        friendlistViewModel =
                ViewModelProviders.of(this).get(FriendlistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_friends_list, container, false);
        final TextView textView = root.findViewById(R.id.text_friend_list);
        friendlistViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}