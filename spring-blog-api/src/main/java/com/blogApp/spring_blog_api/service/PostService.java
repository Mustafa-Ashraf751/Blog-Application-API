package com.blogApp.spring_blog_api.service;

import com.blogApp.spring_blog_api.dto.PostDto;
import com.blogApp.spring_blog_api.dto.PostResponse;

public interface PostService {
  PostDto createPost(PostDto postDto);
  PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,
                           String sortDir);

  PostDto getPostById(long id);

  PostDto updatePost(PostDto postDto ,long id);

  void deletePostById(long id);
}
