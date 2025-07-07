package com.blog.blog.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.blog.blog.dto.BlogRequestDto;
import com.blog.blog.dto.BlogResponseDto;
import com.blog.blog.exception.ResourceNotFoundException;
import com.blog.blog.model.Blog;
import com.blog.blog.repository.BlogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogService implements IBlogService {

    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;

    public Blog toEntity(BlogRequestDto dto) {
        return modelMapper.map(dto, Blog.class);
    }

    public BlogResponseDto toDto(Blog blog) {
        return modelMapper.map(blog, BlogResponseDto.class);
    }

    @Override
    public BlogResponseDto createBlog(BlogRequestDto blogrequest) {
        Blog blog = toEntity(blogrequest);
        Blog savedBlog = blogRepository.save(blog);
        return toDto(savedBlog);
    }

    @Override
    public BlogResponseDto updateBlog(Long id, BlogRequestDto updatedBlog) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id " + id));

        blog.setTitle(updatedBlog.getTitle());
        blog.setContent(updatedBlog.getContent());
        blog.setCategory(updatedBlog.getCategory());
        blog.setTags(updatedBlog.getTags());

        Blog saved = blogRepository.save(blog);
        return toDto(saved);
    }

    @Override
    public void deleteBlog(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id " + id));
        blogRepository.delete(blog);
    }

    @Override
    public Optional<BlogResponseDto> findBlogById(Long id) {
        return blogRepository.findById(id).map(this::toDto);
    }

    @Override
    public List<BlogResponseDto> findAllBlogs() {
        return blogRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<BlogResponseDto> searchBlogsByTerm(String term) {
        return blogRepository.searchTerm(term)
                .stream()
                .map(this::toDto)
                .toList();
    }
}
