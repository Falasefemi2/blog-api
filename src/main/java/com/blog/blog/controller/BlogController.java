package com.blog.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blog.dto.BlogRequestDto;
import com.blog.blog.dto.BlogResponseDto;
import com.blog.blog.exception.ResourceNotFoundException;
import com.blog.blog.response.ApiResponse;
import com.blog.blog.service.IBlogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
@Slf4j
public class BlogController {

    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    private static final String BLOG_NOT_FOUND = "Blog not found";
    private static final String BLOG_UPDATED = "Blog updated successfully";
    private static final String BLOG_DELETED = "Blog deleted successfully";
    private static final String BLOG_FETCHED = "Blog fetched successfully";
    private static final String BLOGS_FETCHED = "Blogs fetched successfully";

    private final IBlogService blogService;

    @PostMapping
    public ResponseEntity<ApiResponse<BlogResponseDto>> createBlog(@Valid @RequestBody BlogRequestDto blog) {
        try {
            BlogResponseDto newBlog = blogService.createBlog(blog);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Blog added successfully", newBlog));
        } catch (Exception e) {
            log.error("Error creating blog: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(INTERNAL_SERVER_ERROR, null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogResponseDto>> updateBlog(
            @PathVariable Long id,
            @Valid @RequestBody BlogRequestDto blog) {
        try {
            BlogResponseDto updatedBlog = blogService.updateBlog(id, blog);
            return ResponseEntity.ok(new ApiResponse<>(BLOG_UPDATED, updatedBlog));
        } catch (ResourceNotFoundException e) {
            log.warn("Blog not found for update: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(BLOG_NOT_FOUND, null));
        } catch (Exception e) {
            log.error("Error updating blog with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(INTERNAL_SERVER_ERROR, null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBlog(@PathVariable Long id) {
        try {
            blogService.deleteBlog(id);
            return ResponseEntity.ok(new ApiResponse<>(BLOG_DELETED, null));
        } catch (ResourceNotFoundException e) {
            log.warn("Blog not found for deletion: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(BLOG_NOT_FOUND, null));
        } catch (Exception e) {
            log.error("Error deleting blog with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(INTERNAL_SERVER_ERROR, null));
        }
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<ApiResponse<BlogResponseDto>> getBlogById(@PathVariable Long blogId) {
        try {
            BlogResponseDto blog = blogService.findBlogById(blogId)
                    .orElseThrow(() -> new ResourceNotFoundException(BLOG_NOT_FOUND + ": " + blogId));
            return ResponseEntity.ok(new ApiResponse<>(BLOG_FETCHED, blog));
        } catch (ResourceNotFoundException e) {
            log.warn("Blog not found: {}", blogId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(BLOG_NOT_FOUND, null));
        } catch (Exception e) {
            log.error("Error fetching blog with id {}: {}", blogId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(INTERNAL_SERVER_ERROR, null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BlogResponseDto>>> getAllBlogs() {
        try {
            List<BlogResponseDto> blogs = blogService.findAllBlogs();
            return ResponseEntity.ok(new ApiResponse<>(BLOGS_FETCHED, blogs));
        } catch (Exception e) {
            log.error("Error fetching all blogs: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(INTERNAL_SERVER_ERROR, null));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BlogResponseDto>>> searchBlogs(
            @RequestParam String term) {
        try {
            if (term == null || term.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Search term cannot be empty", null));
            }

            List<BlogResponseDto> blogs = blogService.searchBlogsByTerm(term.trim());
            return ResponseEntity.ok(new ApiResponse<>(BLOGS_FETCHED, blogs));
        } catch (Exception e) {
            log.error("Error searching blogs with term '{}': {}", term, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(INTERNAL_SERVER_ERROR, null));
        }
    }
}
