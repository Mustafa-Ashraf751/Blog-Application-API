package com.blogApp.spring_blog_api.controller;

import com.blogApp.spring_blog_api.dto.JwtAuthResponse;
import com.blogApp.spring_blog_api.dto.LoginDto;
import com.blogApp.spring_blog_api.dto.SignUpDto;
import com.blogApp.spring_blog_api.entity.Role;
import com.blogApp.spring_blog_api.entity.User;
import com.blogApp.spring_blog_api.repository.RoleRepository;
import com.blogApp.spring_blog_api.repository.UserRepository;
import com.blogApp.spring_blog_api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private AuthenticationManager authenticationManager;
  private UserRepository userRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;

  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  public AuthController(AuthenticationManager authenticationManager,
                        UserRepository userRepository,
                        RoleRepository roleRepository,
                        PasswordEncoder passwordEncoder,
                        JwtTokenProvider jwtTokenProvider) {
    this.authenticationManager = authenticationManager;
    this.userRepository=userRepository;
    this.roleRepository=roleRepository;
    this.passwordEncoder=passwordEncoder;
    this.jwtTokenProvider=jwtTokenProvider;
  }

  @PostMapping("/signin")
  public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
   Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),
           loginDto.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token = jwtTokenProvider.generateToken(authentication);
    return ResponseEntity.ok(new JwtAuthResponse(token));
  }


  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
    //check user is already exist or not
    if(userRepository.existsByUsername(signUpDto.getUserName())){
      return new ResponseEntity<>("Username is already exist",HttpStatus.BAD_REQUEST);
    }
    // check for email exist or not too
    if(userRepository.existsByEmail(signUpDto.getEmail())){
      return new ResponseEntity<>("Email is already taken!",HttpStatus.BAD_REQUEST);
    }
     //create new user
    User user = new User();
    user.setName(signUpDto.getName());
    user.setUsername(signUpDto.getUserName());
    user.setEmail(signUpDto.getEmail());
    user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

    Role roles = roleRepository.findByName("Role_ADMIN").get();
    user.setRoles(Collections.singleton(roles));

    userRepository.save(user);
    return new ResponseEntity<>("User successfully register!",HttpStatus.OK);
  }
}
