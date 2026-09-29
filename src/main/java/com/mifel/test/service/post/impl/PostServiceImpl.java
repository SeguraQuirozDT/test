package com.mifel.test.service.post.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mifel.test.dto.post.PostDto;
import com.mifel.test.entity.post.Post;
import com.mifel.test.repository.post.PostRepository;
import com.mifel.test.service.post.PostService;

@Service
public class PostServiceImpl implements PostService {
	@Autowired
	private PostRepository postRepository;

	@Override
	public PostDto create(PostDto postDto) {
		Post post = Post.builder().title(postDto.getTitle()).body(postDto.getBody()).path(postDto.getPath())
				.createdBy(postDto.getCreatedBy()).build();
		post = postRepository.save(post);
		return PostDto.builder().postId(post.getId()).title(post.getTitle()).body(post.getBody()).path(post.getPath())
				.createdBy(post.getCreatedBy()).build();
	}

	@Override
	public Page<PostDto> getPostsByUserId(Long userId, Pageable pageable) {
		Page<Post> posts = postRepository.findByCreatedByAndDeletedIsFalse(userId, pageable);
		List<PostDto> postDtos = new ArrayList<>();
		posts.forEach(p -> {
			postDtos.add(PostDto.builder().title(p.getTitle()).postId(p.getId()).body(p.getBody()).path(p.getPath())
					.deleted(p.isDeleted()).createdBy(p.getCreatedBy()).modifiedBy(p.getModifiedBy())
					.created(p.getCreated()).modified(p.getModified()).build());
		});
		return new PageImpl<PostDto>(postDtos, pageable, posts.getTotalElements());
	}

	@Override
	public PostDto getPostById(Long postId) {
		Optional<Post> optPost = postRepository.findByIdAndDeletedIsFalse(postId);
		if (optPost.isPresent()) {
			Post post = optPost.get();
			return PostDto.builder().title(post.getTitle()).postId(post.getId()).body(post.getBody())
					.path(post.getPath()).deleted(post.isDeleted()).createdBy(post.getCreatedBy())
					.modifiedBy(post.getModifiedBy()).created(post.getCreated()).modified(post.getModified()).build();
		}
		return null;
	}

	@Override
	public PostDto update(PostDto postDto) {
		Optional<Post> optPost = postRepository.findByIdAndDeletedIsFalse(postDto.getPostId());
		if (optPost.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
		}
		Post post = optPost.get();
		post = post.toBuilder().title(postDto.getTitle()).body(postDto.getBody()).path(postDto.getPath()).build();
		post = postRepository.save(post);
		return PostDto.builder().title(post.getTitle()).postId(post.getId()).body(post.getBody()).path(post.getPath())
				.deleted(post.isDeleted()).createdBy(post.getCreatedBy()).modifiedBy(post.getModifiedBy())
				.created(post.getCreated()).modified(post.getModified()).build();
	}

	@Override
	public PostDto delete(Long postId) {
		Optional<Post> optPost = postRepository.findByIdAndDeletedIsFalse(postId);
		if (optPost.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
		}
		Post post = optPost.get();
		post.setDeleted(true);
		post = postRepository.save(post);
		return PostDto.builder().title(post.getTitle()).postId(post.getId()).body(post.getBody()).path(post.getPath())
				.deleted(post.isDeleted()).createdBy(post.getCreatedBy()).modifiedBy(post.getModifiedBy())
				.created(post.getCreated()).modified(post.getModified()).build();
	}

}
