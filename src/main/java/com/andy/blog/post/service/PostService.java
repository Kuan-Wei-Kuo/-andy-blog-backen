package com.andy.blog.post.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

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

	private ConcurrentHashMap<Integer, Post> postMap = new ConcurrentHashMap<Integer, Post>();

	@PostConstruct
	public void init() {
		Iterable<Post> iterable = postRepository.findAll();
		iterable.forEach(post -> postMap.put(post.getId(), post));
	}

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
		return new ArrayList<Post>(postMap.values());
	}

	public Optional<Post> getPostById(int id) {
		if (!hasPost(id))
			throw new NotFoundException(String.format("Not found post, post id: ", id));
		return Optional.ofNullable(postMap.get(id));
	}

	public Optional<Post> getPostBySlug(String slug) {
		Iterator<Post> it = postMap.values().iterator();
		while (it.hasNext()) {
			Post post = it.next();
			if (post.getSlug().equals(slug))
				return Optional.ofNullable(post);
		}
		throw new NotFoundException(String.format("Not found post, post slug: ", slug));
	}

	public Optional<Post> addPost(PostRequest postRequest) {
		if(hasTitle(postRequest.getTitle()))
			throw new DuplicateException(String.format("Duplicat post title, title: ", postRequest.getTitle()));
		
		String slug = SlugUtils.toSlug(postRequest.getTitle());
		
		Post post = new Post();
		post.setTitle(postRequest.getTitle());
		post.setContent(postRequest.getContent());
		post.setCategories(postRequest.getCategories());
		post.setPublish(postRequest.isPublish());
		post.setModifyTime(new Date());
		post.setCreateTime(new Date());
		post.setSlug(slug);
		
		post = postRepository.save(post);
		
		postMap.put(post.getId(), post);
		
		return Optional.ofNullable(post);
	}

	public Optional<Post> updatePost(int id, PostRequest postRequest) {
		if (!hasPost(id))
			throw new NotFoundException(String.format("Not found post, post id: ", id));
		
		Post post = postMap.get(id);
		
		if(!Objects.equals(post.getTitle(), postRequest.getTitle())) {
			if(hasTitle(postRequest.getTitle()))
				throw new DuplicateException(String.format("Duplicat post title, title: ", postRequest.getTitle()));
		}
		
		String newSlug = SlugUtils.toSlug(postRequest.getTitle());
		post.setTitle(postRequest.getTitle());
		post.setContent(postRequest.getContent());
		post.setPublish(postRequest.isPublish());
		post.setCategories(postRequest.getCategories());
		post.setModifyTime(new Date());
		post.setSlug(newSlug);
		
		post = postRepository.save(post);
		
		postMap.put(post.getId(), post);
		
		return Optional.ofNullable(post);
	}

	public Optional<Post> deletePostById(int id) {
		if (!hasPost(id))
			throw new NotFoundException(String.format("Not found post, post id: ", id));
		Post post = postMap.get(id);
		postRepository.delete(post);
		postMap.remove(id);
		return Optional.ofNullable(post);
	}
	
	private boolean hasPost(int id) {
		return postMap.containsKey(id);
	}
	
	private boolean hasTitle(String title) {
		Collection<Post> posts = postMap.values();
		Iterator<Post> it = posts.iterator();
		while(it.hasNext()) {
			Post post = it.next();
			if(post.getTitle().equals(title)) {
				return true;
			}
		}
		return false;
	}

}
