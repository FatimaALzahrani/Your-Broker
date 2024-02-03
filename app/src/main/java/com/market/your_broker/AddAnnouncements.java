package com.market.your_broker;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;

import android.widget.Toast;

public class AddAnnouncements extends Fragment {
    EditText EDname_of_announcement, EDurl_of_the_announcement_dimensions, EDurl_of_the_announcement_image, EDprice_of_announcement, EDtype_of_announcement;
    EditText EDcomposition_of_announcement, EDdurability_of_announcement,EDcolor_of_announcement,EDnum;
    String name_of_announcement, url_of_the_announcement_dimensions, url_of_the_announcement_image, price_of_announcement, type_of_announcement;
    String composition_of_announcement, durability_of_announcement,color_of_announcement,num;
    ProgressBar progressBar;
    Button Add;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Detail product;
    public AddAnnouncements() {
        // Required empty public constructor
    }
    public static AddAnnouncements newInstance(String param1, String param2) {
        AddAnnouncements fragment = new AddAnnouncements();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_announcements, container, false);
        return view;
    }
}