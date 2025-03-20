package com.razeft.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.razeft.Backend.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
