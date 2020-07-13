package com.andy.blog.post.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andy.blog.post.dto.PageDto;
import com.andy.blog.post.entity.Post;
import com.andy.blog.post.model.PostRequest;
import com.andy.blog.post.service.PostService;

@CrossOrigin
@RestController
@RequestMapping("/private")
public class PrivatePostController {

	@Autowired
	private PostService postService;
	
	@GetMapping(value = "/api/posts")
	public List<Post> getPosts() {
		return postService.getPosts();
	}
	
	@GetMapping(value = "/api/posts", params = {"page", "size"})
	public PageDto<Post> getPosts(@RequestParam("page") int page, @RequestParam("size") int size) {
		return convertToDto(postService.getPosts(page, size));
	}

	@GetMapping(value = "/api/posts/{id}")
	public Post getPostById(@PathVariable("id") int id) {
		return postService.getPostById(id);
	}
	
	@PostMapping(value = "/api/posts")
	public Post addPost(@RequestBody PostRequest postRequest) {
		return postService.addPost(postRequest);
	}
	
	@PutMapping(value = "/api/posts/{id}")
	public Post updatePost(@PathVariable("id") int id, @RequestBody PostRequest PostRequest) {
		return postService.updatePost(id, PostRequest);
	}
	
	@DeleteMapping(value = "/api/posts/{id}")
	public Post deletePost(@PathVariable("id") int id) {
		return postService.deletePostById(id);
	}

	private PageDto<Post> convertToDto(Page<Post> page) {
		PageDto<Post> pageDto = new PageDto<Post>();
		pageDto.setSize(page.getSize());
		pageDto.setPage(page.getNumber());
		pageDto.setTotal(page.getTotalPages());
		pageDto.setContent(page.getContent());
		return pageDto;
	}

}
