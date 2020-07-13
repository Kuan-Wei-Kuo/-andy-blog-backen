package com.andy.blog.post.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.andy.blog.exception.DuplicateException;
import com.andy.blog.exception.NotFoundException;
import com.andy.blog.post.entity.Post;
import com.andy.blog.post.model.PostRequest;
import com.andy.blog.post.repository.PostRepository;
import com.andy.blog.post.util.SlugUtils;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	public Page<Post> getPosts(int page, int size) {
		return postRepository.findAll(PageRequest.of(page, size, Sort.by("createTime").descending()));
	}

	public Page<Post> getPostsByPublish(int page, int size, boolean publish) {
		return postRepository.findByPublish(publish, PageRequest.of(page, size, Sort.by("createTime").descending()));
	}

	public Page<Post> getPostsByCategoryAndPublish(String category, int page, int size, boolean publish) {
		return postRepository.findByCategory(category, publish, PageRequest.of(page, size, Sort.by("create_time").descending()));
	}

	public List<Post> getPosts() {
		List<Post> posts = new ArrayList<>();
		
		Iterable<Post> iterable = postRepository.findAll();
		iterable.forEach(post -> posts.add(post));
		
		return posts;
	}

	public Post getPostById(int id) {
		Optional<Post> optional = postRepository.findById(id);
		
		if (!optional.isPresent())
			throw new NotFoundException(String.format("Not found post, post id: ", id));
		
		return optional.get();
	}

	public Post getPostBySlug(String slug) {
		Optional<Post> optional = postRepository.findBySlug(slug);
		
		if (!optional.isPresent())
			throw new NotFoundException(String.format("Not found post, post slug: ", slug));
		
		return optional.get();
	}

	public Post addPost(PostRequest postRequest) {
		String slug = SlugUtils.toSlug(postRequest.getTitle());

		Optional<Post> optional = postRepository.findBySlug(slug);
		if(optional.isPresent())
			throw new DuplicateException(String.format("Duplicat post title, title: ", postRequest.getTitle()));
		
		Post post = new Post();
		post.setTitle(postRequest.getTitle());
		post.setContent(postRequest.getContent());
		post.setCategories(postRequest.getCategories());
		post.setPublish(postRequest.isPublish());
		post.setModifyTime(new Date());
		post.setCreateTime(new Date());
		post.setSlug(slug);
		
		post = postRepository.save(post);
		
		return post;
	}

	public Post updatePost(int id, PostRequest postRequest) {
		Post post = getPostById(id);

		String slug = SlugUtils.toSlug(postRequest.getTitle());
		
		Optional<Post> optional = postRepository.findBySlugAndIdNotEq(slug, id);
		
		if(optional.isPresent())
			throw new DuplicateException(String.format("Duplicat post title, title: ", postRequest.getTitle()));
		
		post.setTitle(postRequest.getTitle());
		post.setContent(postRequest.getContent());
		post.setPublish(postRequest.isPublish());
		post.setCategories(postRequest.getCategories());
		post.setModifyTime(new Date());
		post.setSlug(slug);
		
		post = postRepository.save(post);
		
		return post;
	}

	public Post deletePostById(int id) {
		Post post = getPostById(id);
		
		postRepository.delete(post);
		
		return post;
	}

}
