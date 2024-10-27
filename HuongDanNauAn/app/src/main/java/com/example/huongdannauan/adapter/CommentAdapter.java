package com.example.huongdannauan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.huongdannauan.R;
import com.example.huongdannauan.model.Comment;
import com.example.huongdannauan.model.Reply;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;
    private Context context;

    public CommentAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        // Gán dữ liệu bình luận
        holder.userNameTextView.setText("User " + comment.getUserId());
        holder.dateTextView.setText(comment.getDate());
        holder.contentTextView.setText(comment.getContent());
        holder.likesTextView.setText(String.valueOf(comment.getLikes()));

        // Gán các bình luận trả lời
        holder.repliesLayout.removeAllViews(); // Xóa các bình luận cũ nếu có
        if(comment.getReplies()!=null){
            for (Reply reply : comment.getReplies()) {
                View replyView = LayoutInflater.from(context).inflate(R.layout.reply_item, holder.repliesLayout, false);

                // Ánh xạ dữ liệu
                TextView replyContent = replyView.findViewById(R.id.replyContentTextView);
                TextView replyDate = replyView.findViewById(R.id.replyDateTextView);
                TextView replyLikes = replyView.findViewById(R.id.replyLikesTextView);

                replyContent.setText(reply.getContent());
                replyDate.setText(reply.getDate());
                replyLikes.setText(String.valueOf(reply.getLikes()));

                holder.repliesLayout.addView(replyView);
            }
        }

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView userNameTextView, dateTextView, contentTextView, likesTextView;
        LinearLayout repliesLayout;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            likesTextView = itemView.findViewById(R.id.likesTextView);
            repliesLayout = itemView.findViewById(R.id.repliesLayout);
        }
    }
}
