package com.razeft.Backend.config;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.razeft.Backend.entity.Book;
import com.razeft.Backend.entity.Comment;
import com.razeft.Backend.entity.User;
import com.razeft.Backend.repository.BookRepository;
import com.razeft.Backend.repository.CommentRepository;
import com.razeft.Backend.repository.UserRepository;

@Configuration
public class AdminInitializer {
        @Bean
        @Order(1)
        CommandLineRunner createAdminUser(UserRepository userRepository) {
                return args -> {
                        userRepository.findByUsername("admin").ifPresentOrElse(
                                        admin -> System.out.println("Admin user already exists"),
                                        () -> {
                                                User newAdmin = new User("admin",
                                                                new BCryptPasswordEncoder(12).encode("admin123"),
                                                                "ADMIN");
                                                userRepository.save(newAdmin);
                                                System.out.println("Admin created");
                                        });

                        userRepository.findByUsername("razeft").ifPresentOrElse(
                                        user -> System.out.println("User 'razeft' already exists"),
                                        () -> {
                                                User otherUser = new User("razeft",
                                                                new BCryptPasswordEncoder(12).encode("Shizumi91"),
                                                                "USER");
                                                userRepository.save(otherUser);
                                                System.out.println("Created a new normal user");
                                        });
                };
        }

        @Bean
        @Order(2)
        CommandLineRunner createComments(UserRepository userRepository, BookRepository bookRepository,
                        CommentRepository commentRepository) {
                return args -> {
                        User user = userRepository.findByUsername("razeft")
                                        .orElseThrow(() -> new RuntimeException("User not Found"));
                        Book book = bookRepository.findById(1)
                                        .orElseThrow(() -> new RuntimeException("Book not found"));

                        Comment comment_one = new Comment(user, book,
                                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                                        4);
                        Comment second = new Comment(user, book,
                                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                                        5);

                        commentRepository.saveAll(List.of(comment_one, second));
                        System.out.println("Create two comments for book with id 1");
                };
        }
}