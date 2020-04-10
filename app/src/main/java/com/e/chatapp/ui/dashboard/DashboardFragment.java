package com.e.chatapp.ui.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.e.chatapp.Edit_profile;
import com.e.chatapp.Nearby_user;
import com.e.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DashboardFragment extends Fragment {

    private String currentuser;
    private DatabaseReference RootRef;

    private ImageView profilePortrait;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        RootRef = FirebaseDatabase.getInstance().getReference().child("Users");
        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RetrievePhoto();

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


        final SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE);
        final String name = sharedPreferences.getString("username", null);

        final TextView textView_name = root.findViewById(R.id.profile_name);
        textView_name.setText(name);

        final String email = sharedPreferences.getString("email", null);

        final TextView textView_email = root.findViewById(R.id.profile_email);
        textView_email.setText(email);

        return root;
    }

    private void RetrievePhoto() {

        RootRef.child(currentuser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))) {
                    String retrievePortrait = dataSnapshot.child("image").getValue().toString();
                    View v = getLayoutInflater().inflate(R.layout.fragment_dashboard, null);
                    profilePortrait = (ImageView)  v.findViewById(R.id.profilePortrait);

                    Picasso.get().load(retrievePortrait).placeholder(R.drawable.icon_user).into(profilePortrait);
                    getActivity().setContentView(v);
                }
                else {

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
