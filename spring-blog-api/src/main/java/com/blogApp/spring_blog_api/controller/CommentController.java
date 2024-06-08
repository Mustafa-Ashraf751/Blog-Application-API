package com.blogApp.spring_blog_api.controller;

import com.blogApp.spring_blog_api.dto.CommentDto;
import com.blogApp.spring_blog_api.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentController {

  private CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }
@PostMapping("posts/{postId}/comments")
  public ResponseEntity<CommentDto> createComment(@PathVariable(name = "postId") long id,
                                                  @Valid @RequestBody CommentDto commentDto){
    return new ResponseEntity<>(commentService.createComment(id, commentDto), HttpStatus.CREATED);
}

@GetMapping("/posts/{postId}/comments")
public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable(name = "postId") Long postId){
    return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
}

@GetMapping("/posts/{postId}/comments/{commentId}")
  public ResponseEntity<CommentDto> getCommentById(@PathVariable(name = "postId") long postId,
                                                   @PathVariable(name = "commentId") long commentId){
    return ResponseEntity.ok(commentService.getCommentById(postId,commentId));
}

@PutMapping("/posts/{postId}/comments/{id}")
  public ResponseEntity<CommentDto> updateComment(@PathVariable(name = "postId") long postId,
                                                  @PathVariable(name = "id") long id,
                                                  @Valid @RequestBody CommentDto commentDto){
    return new ResponseEntity<>(commentService.updateComment(postId,id,commentDto),HttpStatus.OK);

}

@DeleteMapping("/posts/{postId}/comments/{id}")
  public ResponseEntity<String> deleteComment(@PathVariable(name = "postId") long postId,
                                              @PathVariable(name = "id") long id){
    commentService.deleteComment(postId,id);
    return new ResponseEntity<>("comment deleted successfully",HttpStatus.OK);
}
}
