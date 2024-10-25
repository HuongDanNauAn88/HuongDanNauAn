package com.example.huongdannauan.model;

import java.util.List;
import java.util.Map;

public class Comment {
    private String content;
    private String date;
    private int likes;
    private int userId;
    private List<Reply> replies;

    public Comment() {
        // Required empty constructor for Firebase
    }

    // Getters and setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public List<Reply> getReplies() { return replies; }
    public void setReplies(List<Reply> replies) { this.replies = replies; }
}
