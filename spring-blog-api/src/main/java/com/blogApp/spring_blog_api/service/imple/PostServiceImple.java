package com.blogApp.spring_blog_api.service.imple;

import com.blogApp.spring_blog_api.dto.PostDto;
import com.blogApp.spring_blog_api.entity.Post;
import com.blogApp.spring_blog_api.dto.PostResponse;
import com.blogApp.spring_blog_api.exception.ResourceNotFoundException;
import com.blogApp.spring_blog_api.repository.PostRepository;
import com.blogApp.spring_blog_api.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImple implements PostService {

  private PostRepository postRepository;
  private ModelMapper mapper;

  @Autowired
  public PostServiceImple(PostRepository postRepository,ModelMapper mapper) {
    this.postRepository = postRepository;
    this.mapper = mapper;
  }

  @Override
  public PostDto createPost(PostDto postDto) {
    //covert Dto to entity
    Post post = mapToEntity(postDto);
    //save it to the database
    Post newPost = postRepository.save(post);
    // covert it again to back it to user
    PostDto postResponse = MapToDto(newPost);
    return postResponse;
  }

  @Override
  public PostResponse getAllPosts(int pageNo,int pageSize,String sortBy,String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
            :Sort.by(sortBy).descending();
    // create the pageable class
    Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
    Page<Post> posts = postRepository.findAll(pageable);
   // get content from the class
    List<Post> listOfPost = posts.getContent();
    List<PostDto> content = listOfPost.stream().map(post -> MapToDto(post)).
           collect(Collectors.toList());
    PostResponse postResponse = new PostResponse();
    postResponse.setContent(content);
    postResponse.setPageNo(posts.getNumber());
    postResponse.setPageSize(posts.getSize());
    postResponse.setTotalElement(posts.getTotalElements());
    postResponse.setTotalPages(posts.getTotalPages());
    postResponse.setLast(posts.isLast());

    return postResponse;
  }

  @Override
  public PostDto getPostById(long id) {
    Post post = postRepository.findById(id).
    orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
    return MapToDto(post);
  }

  @Override
  public PostDto updatePost(PostDto postDto,long id) {
    // get the post by id first
    Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
    //update the post with new data
    post.setTitle(postDto.getTitle());
    post.setDescription(postDto.getDescription());
    post.setContent(postDto.getContent());
    // saving the new post to database
    Post newPost = postRepository.save(post);
    return MapToDto(newPost);
  }

  @Override
  public void deletePostById(long id) {
    // find the required post
    Post post = postRepository.findById(id).
            orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
    // delete it
    postRepository.delete(post);
  }

  private PostDto MapToDto(Post post){
    PostDto postDto = mapper.map(post,PostDto.class);
//    PostDto postDto = new PostDto();
//    postDto.setId(post.getId());
//    postDto.setTitle(post.getTitle());
//    postDto.setDescription(post.getDescription());
//    postDto.setContent(post.getContent());
    return postDto;
  }

  private Post mapToEntity(PostDto postDto){
    Post post = mapper.map(postDto,Post.class);
//    Post post = new Post();
//    post.setTitle(postDto.getTitle());
//    post.setDescription(postDto.getDescription());
//    post.setContent(postDto.getContent());
    return post;
  }
}
