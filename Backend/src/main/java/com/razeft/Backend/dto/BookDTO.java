package com.razeft.Backend.dto;

import java.util.List;

public class BookDTO {
    private Integer id;
    private String title;
    private String author;
    private int releaseYear;
    private String plot;
    private String coverImage;
    private Integer categoryId; // Aggiunto ID della categoria
    private String categoryName;
    private List<CommentDTO> comments;

    public BookDTO() {
    }

    public BookDTO(Integer id, String title, String author, int releaseYear, String plot, String coverImage,
            Integer categoryId, String categoryName, List<CommentDTO> comments) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
        this.plot = plot;
        this.coverImage = coverImage;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
}
