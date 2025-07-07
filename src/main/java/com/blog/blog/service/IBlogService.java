package com.blog.blog.service;

import java.util.List;
import java.util.Optional;

import com.blog.blog.dto.BlogRequestDto;
import com.blog.blog.dto.BlogResponseDto;

public interface IBlogService {

    BlogResponseDto createBlog(BlogRequestDto blogrequest);

    BlogResponseDto updateBlog(Long id, BlogRequestDto updatedBlog);

    void deleteBlog(Long id);

    Optional<BlogResponseDto> findBlogById(Long id);

    List<BlogResponseDto> findAllBlogs();

    List<BlogResponseDto> searchBlogsByTerm(String term);
}
