package com.andy.blog.post.controller;


import java.util.List;
import java.util.Optional;

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

import com.andy.blog.model.Message;
import com.andy.blog.post.dto.PageDto;
import com.andy.blog.post.entity.Post;
import com.andy.blog.post.exception.PostException;
import com.andy.blog.post.model.PostRequest;
import com.andy.blog.post.service.PostService;

@CrossOrigin
@RestController
@RequestMapping(value = "/priv")
public class PrivPostController {

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
		Optional<Post> optional = postService.getPostById(id);
		return optional.get();
	}
	
	@PostMapping(value = "/api/posts")
	public Post addPost(@RequestBody PostRequest postRequest) {
		Optional<Post> optional = postService.addPost(postRequest);
		return optional.get();
	}
	
	@PutMapping(value = "/api/posts/{id}")
	public Post updatePost(@PathVariable("id") int id, @RequestBody PostRequest PostRequest) {
		Optional<Post> optional =  postService.updatePost(id, PostRequest);
		return optional.get();
	}
	
	@DeleteMapping(value = "/api/posts/{id}")
	public Message deletePost(@PathVariable("id") int id) {
		Message message = new Message();
		message.setStatusCode(500);
		message.setMessage("Fail");
		try {
			postService.deletePostById(id);
			message.setStatusCode(200);
			message.setMessage("Delete Post Successfull");
		} catch (PostException e) {
			message.setStatusCode(404);
			message.setMessage("Invalid ID[" + id + "]");
		}
		return message;
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
