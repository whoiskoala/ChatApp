package com.e.chatapp.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.e.chatapp.Edit_profile;
import com.e.chatapp.Nearby_user;
import com.e.chatapp.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,

                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        final TextView textView_edit = root.findViewById(R.id.edit_profile);
        TextView edit = textView_edit.findViewById(R.id.edit_profile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Edit_profile.class);
                startActivity(intent);
            }
        });

        final TextView textView_nearby_user = root.findViewById(R.id.nearby_user);
        TextView nearby = textView_nearby_user.findViewById(R.id.nearby_user);
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Nearby_user.class);
                startActivity(intent);
            }
        });

        final TextView textView_setting = root.findViewById(R.id.setting);
        TextView setting = textView_setting.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Nearby_user.class);
                startActivity(intent);
            }
        });


        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        final String name = sharedPreferences.getString("username", null);

        final TextView textView_name = root.findViewById(R.id.profile_name);
        textView_name.setText(name);

        final String email = sharedPreferences.getString("email", null);

        final TextView textView_email = root.findViewById(R.id.profile_email);
        textView_email.setText(email);

        return root;
    }
}