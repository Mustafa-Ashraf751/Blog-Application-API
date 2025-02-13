package com.blogApp.spring_blog_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
  private long id;
  // name should not be null or empty
  @NotEmpty(message = "Name should not be null or empty")
  private String name;
  @NotEmpty(message = "Email should not be null or empty")
  @Email
  private String email;
  @NotEmpty
  @Size(min = 10,message = "Comment body must be minimum 10 characters")
  private String body;
}
