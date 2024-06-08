package com.blogApp.spring_blog_api.dto;

import lombok.Data;

@Data
public class LoginDto {
  private String usernameOrEmail;
  private String password;
}
