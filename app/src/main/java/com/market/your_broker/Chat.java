package com.market.your_broker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chat extends Fragment {

    private RecyclerView recyclerView;
//    private ChatListAdapter adapter;
    private DatabaseReference chatListRef;
    private FirebaseAuth mAuth;

    public Chat() {
        // Required empty public constructor
    }
//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
//
//        mAuth = FirebaseAuth.getInstance();
//        String currentUserId = mAuth.getCurrentUser().getUid();
//        chatListRef = FirebaseDatabase.getInstance().getReference().child("ChatList").child(currentUserId);
//
//        recyclerView = view.findViewById(R.id.chatListRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter = new ChatListAdapter();
//        recyclerView.setAdapter(adapter);
//
//        loadChatList();
//
        return view;
    }
//
//    private void loadChatList() {
//        chatListRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<String> userIds = new ArrayList<>();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    userIds.add(dataSnapshot.getKey());
//                }
//                adapter.setUserIds(userIds);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });
//    }
}
