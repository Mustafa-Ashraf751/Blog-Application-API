package com.blogApp.spring_blog_api.controller;

import com.blogApp.spring_blog_api.dto.PostDto;
import com.blogApp.spring_blog_api.dto.PostResponse;
import com.blogApp.spring_blog_api.service.PostService;
import com.blogApp.spring_blog_api.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {
  private PostService postService;
  @Autowired
  public PostController(PostService postService) {
    this.postService = postService;
  }

  // create blog post
  @PostMapping("api/v1/posts")
   public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){

    return new ResponseEntity<>(postService.createPost(postDto),
            HttpStatus.CREATED);
   }

   @GetMapping("api/v1/posts")
  public ResponseEntity<PostResponse> getAllPosts(
          @RequestParam(value = "pageNo",defaultValue = AppConstants.DeFAULT_PAGE_Numer,required = false) int pageNo,
          @RequestParam(value = "pageSize",defaultValue = AppConstants.DefAULT_PAGE_SIZE,required = false) int pageSize,
          @RequestParam(value = "sortBy",defaultValue = AppConstants.DefAULT_SORT_BY,required = false) String sortBy,
          @RequestParam(value = "sortDir",defaultValue = AppConstants.DefAULT_SORT_DIREACTION,required = false) String sortDir
   ){
    return ResponseEntity.ok(postService.getAllPosts(pageNo,pageSize,sortBy,sortDir));
   }

   @GetMapping(value = "api/v1/posts/{id}",produces = "application/vnd.javaguides.v1+json")
  public ResponseEntity<PostDto> getPostByIdV1(@PathVariable(name = "id") long id){
    return ResponseEntity.ok(postService.getPostById(id));
   }

   
   @PreAuthorize("hasRole('ADMIN')")
   @PutMapping({"api/v1/posts/{id}"})
  public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,@PathVariable(name="id") long id){
    PostDto postDto1 = postService.updatePost(postDto,id);
    return new ResponseEntity<>(postDto1,HttpStatus.OK);
   }
  @PreAuthorize("hasRole('ADMIN')")
   @DeleteMapping("api/v1/posts/{id}")
   public ResponseEntity<String> deletepost(@PathVariable(name = "id") long id){
      postService.deletePostById(id);
      return new ResponseEntity<>("The post successfully deleted", HttpStatus.OK);
   }
}
