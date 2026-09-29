package com.mifel.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mifel.test.dto.post.PostDto;
import com.mifel.test.security.CustomUserDetails;
import com.mifel.test.security.UserUtil;
import com.mifel.test.service.post.PostService;


@RequestMapping(path = "/api/posts")
@RestController
public class PostController {
	
	@Autowired
	private PostService postService;

	@PreAuthorize("hasAuthority('USER')")
	@PostMapping(path = "/create", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<PostDto> create(@RequestBody  PostDto postDto) {
		CustomUserDetails loggedInUser = UserUtil.getCurrentLoggedInUser();
		postDto.setCreatedBy(loggedInUser.getId());
		return ResponseEntity.ok(postService.create(postDto));
	}

	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	@GetMapping
	public ResponseEntity<Page<PostDto>> getPosts(@PageableDefault(page = 0, size = 30) @SortDefault.SortDefaults({
			@SortDefault(sort = "created", direction = Sort.Direction.DESC) }) Pageable pageable) {
		CustomUserDetails loggedInUser = UserUtil.getCurrentLoggedInUser();
		return ResponseEntity.ok(postService.getPostsByUserId(loggedInUser.getId(), pageable));
	}

	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	@GetMapping(path = "/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable(name = "postId") Long postId) {
		return ResponseEntity.ok(postService.getPostById(postId));
	}

	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	@GetMapping(path = "/users/{userId}")
	public ResponseEntity<Page<PostDto>> getPostsByUserId(@PathVariable(name = "userId") Long userId,
			@PageableDefault(page = 0, size = 30) @SortDefault.SortDefaults({
					@SortDefault(sort = "created", direction = Sort.Direction.DESC) }) Pageable pageable) {
		return ResponseEntity.ok(postService.getPostsByUserId(userId, pageable));
	}

	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	@PutMapping(path = "/update", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<PostDto> update(@RequestBody  PostDto postDto) {
		return ResponseEntity.ok(postService.update(postDto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping(path = "/delete/{postId}", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<PostDto> delete(@PathVariable(name = "postId") Long postId) {
		return ResponseEntity.ok(postService.delete(postId));
	}
	
}
