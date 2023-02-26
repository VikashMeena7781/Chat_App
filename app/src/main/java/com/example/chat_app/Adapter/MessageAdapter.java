package com.example.chat_app.Adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private Context context;
    private List<Chat> chats;
    private String imageurl;
    FirebaseUser firebaseUser;


    public MessageAdapter(Context context,List<Chat> chats,String imageurl){
        this.context=context;
        this.chats=chats;
        this.imageurl=imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==MSG_TYPE_RIGHT){
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
        }
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.show_message.setText(chat.getMessage());
        // get the current time of messaging;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime now = LocalDateTime.now();
//            System.out.println(dtf.format(now));
            holder.last_time_msg.setText(String.valueOf(dtf.format(now)));
        }

        if(imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(imageurl).into(holder.profile_image);
        }

        if(position== chats.size()-1){
            if(chat.isIsseen()){
                holder.text_seen.setText("Seen");
            }else{
                holder.text_seen.setText("Delivered");
            }
        }else{
            holder.text_seen.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;
        public TextView last_time_msg;

        public TextView text_seen;

        public ViewHolder(View view){
            super(view);

            show_message=view.findViewById(R.id.show_message);
            profile_image=view.findViewById(R.id.profile_image);
            text_seen=view.findViewById(R.id.text_seen);
            last_time_msg=view.findViewById(R.id.last_time_msg);

        }
    }
    public int getItemViewType(int position){
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(chats.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return  MSG_TYPE_LEFT;
        }

    }


}

