package com.blogApp.spring_blog_api.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class passwordHashing {
  public static void main(String[] args) {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String pass = "ahmed123";
    String hashed = passwordEncoder.encode(pass);
    System.out.println(hashed);
  }


}
