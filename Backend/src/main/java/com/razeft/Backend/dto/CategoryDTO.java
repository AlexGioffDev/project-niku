package com.razeft.Backend.dto;

import java.util.List;

public class CategoryDTO {
    private Integer categoryId;
    private String categoryName;
    private Integer totalBooks;
    private List<BookDTO> books;

    public CategoryDTO() {
    }

    public CategoryDTO(Integer categoryId, String categoryName, Integer totalBooks, List<BookDTO> books) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.totalBooks = totalBooks;
        this.books = books;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(Integer totalBooks) {
        this.totalBooks = totalBooks;
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

}
