package com.example.chat_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat_app.MessageActivity;
import com.example.chat_app.Model.Chat;
import com.example.chat_app.Model.User_Info;
import com.example.chat_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private Context context;
    private List<User_Info> users;
    private boolean ischat;
    String theLastMessage;

    public UserAdapter(Context context,List<User_Info> users, boolean ischat){
        this.context=context;
        this.users=users;
        this.ischat=ischat;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        final User_Info user_info = users.get(position);
        holder.username.setText(user_info.getUsername());
        if(user_info.getImageUrl().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(user_info.getImageUrl()).into(holder.profile_image);

        }
        if(ischat){
            lastMessage(user_info.getId(), holder.last_msg);
        }else{
            holder.last_msg.setVisibility(View.GONE);
        }
        if(ischat){
            if(user_info.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
//                holder.img_off.setVisibility(View.GONE);
            }else {
                holder.img_on.setVisibility(View.GONE);
//                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else{
            holder.img_on.setVisibility(View.GONE);
//            holder.img_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid",user_info.getId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
//        private ImageView img_off;
        private ImageView img_on;
        private  TextView last_msg;
        public ViewHolder(View view){
            super(view);
            username=view.findViewById(R.id.username);
            profile_image=view.findViewById(R.id.profile_image);
//            img_off=view.findViewById(R.id.img_off);
            img_on=view.findViewById(R.id.img_on);
            last_msg=view.findViewById(R.id.last_msg);


        }
    }

    //check last message
    private void lastMessage(String userid, TextView last_msg){
        theLastMessage = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)  || chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(userid)){
                        theLastMessage = chat.getMessage();
                    }
                }
                switch (theLastMessage){
                    case "default" :
                        last_msg.setText("No Message");
                        break;
                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage="default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
