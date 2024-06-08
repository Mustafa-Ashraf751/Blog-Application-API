package com.blogApp.spring_blog_api.service.imple;

import com.blogApp.spring_blog_api.dto.CommentDto;
import com.blogApp.spring_blog_api.entity.Comment;
import com.blogApp.spring_blog_api.entity.Post;
import com.blogApp.spring_blog_api.exception.BlogAppException;
import com.blogApp.spring_blog_api.exception.ResourceNotFoundException;
import com.blogApp.spring_blog_api.repository.CommentRepository;
import com.blogApp.spring_blog_api.repository.PostRepository;
import com.blogApp.spring_blog_api.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImple implements CommentService {

  private CommentRepository commentRepository;
  private PostRepository postRepository;
  private ModelMapper mapper;

  @Autowired
  public CommentServiceImple(CommentRepository commentRepository,PostRepository postRepository,ModelMapper mapper) {
    this.commentRepository = commentRepository;
    this.postRepository=postRepository;
    this.mapper=mapper;
  }

  @Override
  public CommentDto createComment(long postId, CommentDto commentDto) {
    Comment comment = mapToComment(commentDto);
    // retrieve post by id
    Post post = postRepository.findById(postId).
            orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
    // set post to comment
    comment.setPost(post);
    // save the comment to the database
    Comment newComment = commentRepository.save(comment);
    return mapToDto(newComment);
  }

  @Override
  public List<CommentDto> getCommentsByPostId(long postId) {
    // retrieve comments by postId
    List<Comment> comments = commentRepository.findByPostId(postId);
    // covert it to commentsDto to send it to user
    return comments.stream()
            .map(comment -> mapToDto(comment))
            .collect(Collectors.toList());

  }

  @Override
  public CommentDto getCommentById(long postId,long commentId) {
    Comment comment = findPostAndComment(postId,commentId);
    return mapToDto(comment);
  }

  @Override
  public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {
    Comment comment = findPostAndComment(postId,commentId);
    // modify the entity
    comment.setName(commentRequest.getName());
    comment.setBody(commentRequest.getBody());
    comment.setEmail(commentRequest.getEmail());

    // save the entity to database
   return mapToDto(commentRepository.save(comment));

  }

  @Override
  public void deleteComment(long postId, long commentId) {
    Comment comment = findPostAndComment(postId,commentId);
    // delete the comment
    commentRepository.delete(comment);

  }

  public Comment findPostAndComment(long postId,long commentId){
    // retrieve post by id
    Post post = postRepository.findById(postId).
            orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
    // retrieve comment by id
    Comment comment = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment",
            "id",commentId));
    // check if comment belong to the post
    if(!comment.getPost().getId().equals(post.getId())){
      throw new BlogAppException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
    }
    return comment;
  }

  private CommentDto mapToDto(Comment comment){
    CommentDto commentDto = mapper.map(comment,CommentDto.class);
//    CommentDto commentDto = new CommentDto();
//    commentDto.setId(comment.getId());
//    commentDto.setName(comment.getName());
//    commentDto.setBody(comment.getBody());
//    commentDto.setEmail(comment.getEmail());
    return commentDto;
  }

  private Comment mapToComment(CommentDto commentDto){
    Comment comment = mapper.map(commentDto,Comment.class);
//    Comment comment = new Comment();
//    comment.setId(commentDto.getId());
//    comment.setName(commentDto.getName());
//    comment.setEmail(commentDto.getEmail());
//    comment.setBody(commentDto.getBody());
    return comment;
  }
}
