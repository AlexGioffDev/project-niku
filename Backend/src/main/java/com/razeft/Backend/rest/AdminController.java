package com.razeft.Backend.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razeft.Backend.service.AdminService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> allUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories() {
        return adminService.getCategories();
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<Map<String, Object>> changeUserRole(@PathVariable int id,
            @RequestBody Map<String, String> requestBody) {
        String newRole = requestBody.get("newRole");
        return adminService.EditUserRole(id, newRole);
    }

    @PostMapping("/book/create")
    public ResponseEntity<Map<String, Object>> createBook(@RequestBody Map<String, String> request) {
        String title = (String) request.get("title");
        String author = (String) request.get("author");
        String plot = (String) request.get("plot");
        String coverImage = (String) request.get("coverImage");
        Integer releaseYear = Integer.parseInt(request.get("releaseYear").toString());
        Integer categoryID = Integer.parseInt(request.get("categoryID").toString());

        return adminService.CreateBook(title, author, plot, releaseYear, coverImage, categoryID);
    }

    @DeleteMapping("/book/{id}/delete")
    public ResponseEntity<Map<String, Object>> deleteBook(@PathVariable int id) {
        return adminService.DeleteBook(id);
    }

}
