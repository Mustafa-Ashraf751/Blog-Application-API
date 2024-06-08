package com.blogApp.spring_blog_api.repository;

import com.blogApp.spring_blog_api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {

}
