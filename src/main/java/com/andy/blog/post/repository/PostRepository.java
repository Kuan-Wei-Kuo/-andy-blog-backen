package com.andy.blog.post.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.andy.blog.post.entity.Post;

public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {

	@Query(value = "SELECT a FROM Post a WHERE a.slug = ?1")
	Optional<Post> findBySlug(String slug);

	@Query(value = "SELECT a FROM Post a WHERE a.slug = ?1 AND a.id != ?2")
	Optional<Post> findBySlugAndIdNotEq(String slug, int id);
	
	@Query(value = "SELECT a FROM Post a WHERE a.publish = ?1")
	Page<Post> findByPublish(boolean publish, Pageable pageable);
	
	@Query(nativeQuery = true, 
			value = "SELECT * FROM post AS a JOIN post_category AS b on b.post_id = a.id WHERE b.category_name = ?1 AND a.publish = ?2",
		    countQuery = "SELECT COUNT(*) FROM post AS a JOIN post_category AS b on b.post_id = a.id WHERE b.category_name = ?1 AND a.publish = ?2")
	Page<Post> findByCategory(String category, boolean publish, Pageable pageable);
	
}
