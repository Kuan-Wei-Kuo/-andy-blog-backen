package com.andy.blog.post.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andy.blog.post.dto.PageDto;
import com.andy.blog.post.dto.PostDto;
import com.andy.blog.post.entity.Post;
import com.andy.blog.post.service.PostService;

@CrossOrigin
@RestController
@RequestMapping("/public")
public class PublicPostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping(value = "/api/posts", params = {"page", "size"})
	public PageDto<PostDto> getPosts(@RequestParam("page") int page, @RequestParam("size") int size) {
		return convertToDto(postService.getPostsByPublish(page, size, true));
	}
	
	@GetMapping(value = "/api/posts", params = {"category", "page", "size"})
	public PageDto<PostDto> getPostsByCategory(@RequestParam("category") String category, @RequestParam("page") int page, @RequestParam("size") int size) {
		return convertToDto(postService.getPostsByCategoryAndPublish(category, page, size, true));
	}
	
	@GetMapping(value = "/api/posts/{slug}")
	public PostDto getPostBySlug(@PathVariable("slug") String slug) {
		return convertToDto(postService.getPostBySlug(slug));
	}
	
	private PageDto<PostDto> convertToDto(Page<Post> page) {
		PageDto<PostDto> pageDto = new PageDto<PostDto>();
		pageDto.setSize(page.getSize());
		pageDto.setPage(page.getNumber());
		pageDto.setTotal(page.getTotalPages());
		List<PostDto> dtos = new ArrayList<PostDto>();
		page.getContent().forEach(post -> dtos.add(convertToDto(post)));
		pageDto.setContent(dtos);
		return pageDto;
	}

	private PostDto convertToDto(Post post) {
	    PostDto postDto = modelMapper.map(post, PostDto.class);
	    postDto.setSubmissionDate(post.getCreateTime());
	    return postDto;
	}
	
}
