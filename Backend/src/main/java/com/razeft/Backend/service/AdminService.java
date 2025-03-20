package com.razeft.Backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.razeft.Backend.dto.BookDTO;
import com.razeft.Backend.dto.SingleCategoryDTO;
import com.razeft.Backend.dto.UserDTO;
import com.razeft.Backend.entity.Book;
import com.razeft.Backend.entity.Category;
import com.razeft.Backend.entity.User;
import com.razeft.Backend.repository.BookRepository;
import com.razeft.Backend.repository.CategoryRepository;
import com.razeft.Backend.repository.UserRepository;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String BOOK_CACHE_KEY = "books";

    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Map<String, Object> response = new HashMap<>();

        List<User> allUsers = userRepository.findAll();

        if (allUsers.isEmpty()) {
            response.put("Message", "No Users found");
            return ResponseEntity.status(404).body(response);
        }

        List<UserDTO> usersDTO = allUsers.stream().map(UserDTO::new).collect(Collectors.toList());

        response.put("Users", usersDTO);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> EditUserRole(Integer id, String newRole) {
        Map<String, Object> response = new HashMap<>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not Found"));

        Optional<User> otherUser = userRepository.findById(id);

        if (otherUser.isEmpty()) {
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        if (user.getId() == otherUser.get().getId()) {
            response.put("message", "You can't change your role");
            return ResponseEntity.status(403).body(response);
        }

        if (!newRole.equalsIgnoreCase("USER") && !newRole.equalsIgnoreCase("ADMIN")) {
            response.put("message", "Invalid role");
            return ResponseEntity.status(403).body(response);
        }

        otherUser.get().setRole(newRole);
        userRepository.save(otherUser.get());

        response.put("message", "User role updated!");

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> CreateBook(String title, String author, String plot, Integer releaseYear,
            String coverImage, Integer categoryID) {
        Map<String, Object> response = new HashMap<>();

        Optional<Category> category = categoryRepository.findById(categoryID);

        if (category.isEmpty()) {
            response.put("message", "Category no found!");
            return ResponseEntity.status(404).body(response);
        }

        Book newBook = new Book(title, author, plot, releaseYear, coverImage, category.get());
        bookRepository.save(newBook);

        BookDTO bookDTO = new BookDTO(newBook.getId(), newBook.getTitle(), newBook.getAuthor(),
                newBook.getReleaseYear(), newBook.getPlot(), newBook.getCoverImage(), categoryID,
                newBook.getCategory().getName(),
                null);

        response.put("message", "Book saved");
        response.put("book", bookDTO);

        redisTemplate.delete(BOOK_CACHE_KEY);

        return ResponseEntity.ok(response);

    }

    public ResponseEntity<Map<String, Object>> DeleteBook(Integer id) {
        Map<String, Object> response = new HashMap<>();

        Optional<Book> book = bookRepository.findById(id);

        if (book.isEmpty()) {
            response.put("message", "Book not found");
            return ResponseEntity.status(404).body(response);
        }

        bookRepository.delete(book.get());

        redisTemplate.delete(BOOK_CACHE_KEY);

        response.put("message", "book deleted");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> getCategories() {
        Map<String, Object> response = new HashMap<>();
        List<Category> categories = categoryRepository.findAll();
        List<SingleCategoryDTO> categoriesResponse = categories.stream()
                .map((category) -> new SingleCategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
        response.put("categories", categoriesResponse);
        return ResponseEntity.ok(response);
    }

}
