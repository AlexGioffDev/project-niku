package com.razeft.Backend.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.razeft.Backend.dto.BookDTO;
import com.razeft.Backend.dto.CategoryDTO;
import com.razeft.Backend.dto.CommentDTO;
import com.razeft.Backend.entity.Book;
import com.razeft.Backend.entity.Category;
import com.razeft.Backend.entity.Comment;
import com.razeft.Backend.entity.User;
import com.razeft.Backend.repository.BookRepository;
import com.razeft.Backend.repository.CategoryRepository;
import com.razeft.Backend.repository.CommentRepository;
import com.razeft.Backend.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String BOOK_CACHE_KEY = "books";

    public ResponseEntity<Map<String, Object>> getAll() {

        Map<String, Object> response = new HashMap<>();
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        if (redisTemplate.hasKey(BOOK_CACHE_KEY)) {
            response.put("Books", ops.get(BOOK_CACHE_KEY));
            return ResponseEntity.ok(response);
        }

        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()) {
            response.put("Message", "There is no books!");
            return ResponseEntity.ok(response);
        }

        List<BookDTO> booksResponse = books.stream()
                .map(book -> new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getReleaseYear(),
                        book.getPlot(),
                        book.getCoverImage(), book.getCategory().getId(), book.getCategory().getName(),
                        book.getComments().stream()
                                .map(comment -> new CommentDTO(comment.getUser().getUsername(), comment.getContent(),
                                        comment.getRating()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());

        ops.set(BOOK_CACHE_KEY, booksResponse, Duration.ofMinutes(10));

        response.put("Books", booksResponse);
        return ResponseEntity.ok(response);

    }

    public ResponseEntity<Map<String, Object>> findById(Integer id) {
        String cacheKey = "book:" + id;
        Map<String, Object> response = new HashMap<>();
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        if (redisTemplate.hasKey(cacheKey)) {
            response.put("Book", ops.get(cacheKey));
            return ResponseEntity.ok(response);
        }

        try {
            Book book = bookRepository.getReferenceById(id);
            BookDTO bookDTO = new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getReleaseYear(),
                    book.getPlot(),
                    book.getCoverImage(), book.getCategory().getId(), book.getCategory().getName(),
                    book.getComments().stream().map(comment -> new CommentDTO(comment.getUser().getUsername(),
                            comment.getContent(), comment.getRating())).collect(Collectors.toList()));

            ops.set(cacheKey, bookDTO, Duration.ofMinutes((10)));
            response.put("Book", bookDTO);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response.put("Message", "Book not found with ID: " + id);
            return ResponseEntity.status(404).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> search(String query) {
        Map<String, Object> response = new HashMap<>();
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        String cacheKey = "search: " + query;

        if (redisTemplate.hasKey(query)) {
            response.put("Books Found", ops.get(cacheKey));
            return ResponseEntity.ok(response);
        }

        List<Book> books = bookRepository.searchByTitleOrAuthor(query);

        if (books.isEmpty()) {
            response.put("Message", "No Books found");
            return ResponseEntity.status(404).body(response);
        }
        List<BookDTO> booksResponse = books.stream()
                .map(book -> new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getReleaseYear(),
                        book.getPlot(),
                        book.getCoverImage(), book.getCategory().getId(), book.getCategory().getName(),
                        book.getComments().stream()
                                .map(comment -> new CommentDTO(comment.getUser().getUsername(), comment.getContent(),
                                        comment.getRating()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());

        ops.set(cacheKey, booksResponse, Duration.ofMinutes(10));

        response.put("Books Found", booksResponse);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> findByCategory(Integer categoryId) {
        String cacheKey = "BooksCategory: " + categoryId;
        List<Book> books = bookRepository.findBooksByCategoryId(categoryId);
        Map<String, Object> response = new HashMap<>();
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        if (redisTemplate.hasKey(cacheKey)) {
            response.put("category", ops.get(cacheKey));
            return ResponseEntity.ok(response);
        }

        if (books.isEmpty()) {
            response.put("message", "No books found for this category");
            return ResponseEntity.status(404).body(response);
        }

        List<BookDTO> booksResponse = books.stream()
                .map(book -> new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getReleaseYear(),
                        book.getPlot(),
                        book.getCoverImage(), book.getCategory().getId(), book.getCategory().getName(),
                        book.getComments().stream()
                                .map(comment -> new CommentDTO(comment.getUser().getUsername(), comment.getContent(),
                                        comment.getRating()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());

        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            response.put("message", "Category not found");
            return ResponseEntity.status(404).body(response);
        }

        CategoryDTO categoryResponse = new CategoryDTO(category.get().getId(), category.get().getName(),
                booksResponse.size(),
                booksResponse);

        ops.set(cacheKey, categoryResponse, Duration.ofMinutes(10));
        response.put("category", categoryResponse);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> addComment(Integer bookId, String content, Integer rating) {
        Map<String, Object> response = new HashMap<>();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not Found"));

        Optional<Book> book = bookRepository.findById(bookId);

        if (book.isEmpty()) {
            response.put("message", "Book not found");
            return ResponseEntity.status(404).body(response);
        }

        Comment comment = new Comment(user, book.get(), content, rating);

        commentRepository.save(comment);

        String bookCacheKey = "book:" + bookId;
        redisTemplate.delete(bookCacheKey);

        response.put("message", "Comment added successfully");
        return ResponseEntity.ok(response);
    }

}
