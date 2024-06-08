package com.blogApp.spring_blog_api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
  private long id;
 // validate title shouldn't be empty and has at least 2 chars
  @NotEmpty
  @Size(min = 2,message = "Post title should have at least 2 characters")
  private String title;
  @NotEmpty
  @Size(min=10,message = "Post description should have at least 10 characters")
  private String description;
  @NotEmpty
  private String content;
  private Set<CommentDto> comments;
}
