package com.mifel.test.service.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mifel.test.dto.post.PostDto;

public interface PostService {
    PostDto create(PostDto postDto);
    Page<PostDto> getPostsByUserId(Long userId, Pageable pageable);
    PostDto getPostById(Long postId);
    PostDto update(PostDto postDto);
    PostDto delete(Long postId);
}
