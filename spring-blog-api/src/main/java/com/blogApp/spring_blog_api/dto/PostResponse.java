package com.blogApp.spring_blog_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
  private List<PostDto> content;
  private int pageNo;
  private int pageSize;
  private long totalElement;
  private int totalPages;
  private boolean last;
}
