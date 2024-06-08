package com.blogApp.spring_blog_api.dto;

import lombok.Data;

@Data
public class SignUpDto {
  private String name;
  private String userName;
  private String email;
  private String password;
}
