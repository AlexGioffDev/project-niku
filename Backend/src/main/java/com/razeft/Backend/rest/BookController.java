package com.razeft.Backend.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razeft.Backend.service.BookService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getByID(@PathVariable Integer id) {
        return bookService.findById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchBooks(
            @RequestParam(required = false) String query) {
        return bookService.search(query);
    }

    @GetMapping("/category")
    public ResponseEntity<Map<String, Object>> searchByCategory(@RequestParam Integer category) {
        return bookService.findByCategory(category);
    }

    @PostMapping("/{bookId}/comment")
    public ResponseEntity<Map<String, Object>> addComment(@PathVariable Integer bookId,
            @RequestBody Map<String, Object> requestBody) {

        String content = (String) requestBody.get("content");
        Integer rating = (Integer) requestBody.get("rating");
        return bookService.addComment(bookId, content, rating);
    }

}
