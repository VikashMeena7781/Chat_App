package com.example.chat_app.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.chat_app.Adapter.UserAdapter;
import com.example.chat_app.Model.Chat;
import com.example.chat_app.Model.ChatList;
import com.example.chat_app.Model.User_Info;
import com.example.chat_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.sql.Timestamp;


public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User_Info> users;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private List<ChatList> user_list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView= view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user_list=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChatList chatList = dataSnapshot.getValue(ChatList.class);
                    user_list.add(chatList);
                }
                for (int i = 0; i < user_list.size(); i++) {
                    for (int j = i+1; j < user_list.size(); j++) {
                        ChatList temp;
                        Timestamp ts1 = Timestamp.valueOf(user_list.get(i).last_time_msg);
                        Timestamp ts2 = Timestamp.valueOf(user_list.get(j).last_time_msg);
                        int x = ts1.compareTo(ts2);
                        if(x<=0){
                            temp=user_list.get(i);
                            user_list.set(i, user_list.get(j));
                            user_list.set(j,temp);
                        }
                    }

                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        reference= FirebaseDatabase.getInstance().getReference("Chats");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                user_list.clear();
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Chat chat = dataSnapshot.getValue(Chat.class);
//                    assert chat!=null;
//                    if(chat.getSender().equals(firebaseUser.getUid())){
//                        user_list.add(chat.getReceiver());
//                    }
//                    if(chat.getReceiver().equals(firebaseUser.getUid())){
//                        user_list.add(chat.getSender());
//                    }
//                }
//                readChat();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        return view;
    }



    private void chatList() {
        users = new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Users_Data");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    User_Info user_info = dataSnapshot.getValue(User_Info.class);
//                    for(ChatList chatList : user_list){
//                        assert user_info != null;
//                        if(user_info.getId().equals(chatList.getId())){
//                            users.add(user_info);
//                        }
//                    }
                for (ChatList chatList : user_list){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        User_Info user_info = dataSnapshot.getValue(User_Info.class);
                        assert user_info!=null;
                        if(chatList.getId().equals(user_info.getId())){
                            users.add(user_info);
                        }
                    }
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    User_Info user_info = dataSnapshot.getValue(User_Info.class);
//                    for(ChatList chatList : user_list){
//                        if(user_info.getId().equals(chatList.getId())){
//                            if(users.size()!=0){
//                                for(User_Info user_info1 : users){
//                                    if(!user_info.getId().equals(user_info1.getId())){
//                                        users.add(user_info);
//                                    }
//                                }
//                            }else{
//                                users.add(user_info);
//                            }
//                        }
//                    }
                }
                userAdapter = new UserAdapter(getContext(),users,true);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void readChat() {
//        users= new ArrayList<>();
//        reference=FirebaseDatabase.getInstance().getReference("Users_Data");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                users.clear();
//
//                //display one user from chat
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    User_Info user_info = dataSnapshot.getValue(User_Info.class);
//                    for(String id : user_list){
//                        if(user_info.getId().equals(id)){
//                            if(users.size()!=0){
//                                for(User_Info user_info1 : users){
//                                    if(!user_info.getId().equals(user_info1.getId())){
//                                        users.add(user_info);
//                                    }
//                                }
//                            }else{
//                                users.add(user_info);
//                            }
//                        }
//                    }
//                }
//                userAdapter = new UserAdapter(getContext(),users,true);
//                recyclerView.setAdapter(userAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
}