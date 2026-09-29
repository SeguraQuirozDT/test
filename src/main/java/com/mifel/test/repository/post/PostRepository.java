package com.mifel.test.repository.post;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mifel.test.entity.post.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
	Page<Post> findByCreatedByAndDeletedIsFalse(Long userId, Pageable pageable);
    Optional<Post> findByIdAndDeletedIsFalse(Long postId);
}
