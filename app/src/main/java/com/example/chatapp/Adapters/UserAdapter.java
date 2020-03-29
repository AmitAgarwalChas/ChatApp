package com.example.chatapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Activities.ChattingActivity;
import com.example.chatapp.R;
import com.example.chatapp.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    ArrayList<User> muser;
    Context mcontext;

    public UserAdapter(Context context, ArrayList<User> user){
        mcontext = context;
        muser = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.user_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.handleName.setText(muser.get(position).getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ChattingActivity.class);
                intent.putExtra("userId", muser.get(position).getId());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return muser.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView handleName;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            handleName = (TextView) itemView.findViewById(R.id.txt_username);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_user_item);
        }
    }
}
