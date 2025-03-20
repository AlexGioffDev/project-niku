package com.razeft.Backend.dto;

public class CommentDTO {
    private String username;
    private String content;
    private int rating;

    public CommentDTO() {
    }

    public CommentDTO(String username, String content, int rating) {
        this.username = username;
        this.content = content;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
