package com.blogApp.spring_blog_api.service;

import com.blogApp.spring_blog_api.dto.CommentDto;

import java.util.List;

public interface CommentService {
  CommentDto createComment(long postId,CommentDto commentDto);

  List<CommentDto> getCommentsByPostId(long postId);
  CommentDto getCommentById(long postId,long CommentId);
  CommentDto updateComment(long postId,long commentId,CommentDto commentRequest);

  void deleteComment(long postId,long commentId);
}
