package com.andy.blog.post.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import com.andy.blog.exception.RestException;
import com.andy.blog.post.entity.Post;
import com.andy.blog.post.model.PostRequest;
import com.andy.blog.post.service.PostService;

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class PostServiceTests {
	
	@Autowired
	private PostService postService;
	
	@Test
	@Order(1)
	public void testAddPost() {
		List<String> categories = new ArrayList<String>();
		categories.add("Test category");
		
		PostRequest postRequest = new PostRequest();
		postRequest.setTitle("Test title");
		postRequest.setContent("Test content");
		postRequest.setPublish(true);
		postRequest.setCategories(categories);

		Post post = postService.addPost(postRequest);

		assertEquals(post.getTitle(), postRequest.getTitle());
		assertEquals(post.getContent(), postRequest.getContent());
		assertEquals(post.isPublish(), postRequest.isPublish());
	}
	
	@Test
	@Order(1)
	public void testAddPostDuplicate() {
		List<String> categories = new ArrayList<String>();
		categories.add("Test category");
		
		PostRequest postRequest = new PostRequest();
		postRequest.setTitle("Test title");
		postRequest.setContent("Test content");
		postRequest.setPublish(true);
		postRequest.setCategories(categories);
		
		assertThrows(RestException.class, () -> {
			postService.addPost(postRequest);
		});
	}
	
	@Test
	@Order(2)
	public void testUpdatePost() {
		List<String> categories = new ArrayList<String>();
		categories.add("Test category");
		
		PostRequest postRequest = new PostRequest();
		postRequest.setTitle("Test title");
		postRequest.setContent("Test content2");
		postRequest.setPublish(true);
		postRequest.setCategories(categories);
		
		Post post = postService.updatePost(1, postRequest);
		
		assertEquals(post.getTitle(), postRequest.getTitle());
		assertEquals(post.getContent(), postRequest.getContent());
		assertEquals(post.isPublish(), postRequest.isPublish());
	}
	
	@Test
	@Order(3)
	public void testGetPosts() {
		List<Post> posts = postService.getPosts();
		assertEquals(1, posts.size());
	}

	@Test
	@Order(3)
	public void testGetPostById() {
		Post post = postService.getPostById(1);
		assertNotNull(post);
	}

	@Test
	@Order(3)
	public void testGetPostBySlug() {
		Post post = postService.getPostBySlug("test-title");
		assertNotNull(post);
	}
	
	@Test
	@Order(3)
	public void testGetPostPage() {
		Page<Post> page = postService.getPosts(0, 10);
		assertEquals(1, page.getContent().size());
	}

	@Test
	@Order(3)
	public void testGetPostsByPublish() {
		Page<Post> page = postService.getPostsByPublish(0, 10, true);
		assertEquals(1, page.getContent().size());
	}

	@Test
	@Order(3)
	public void testPostsByCategoryAndPublish() {
		Page<Post> page = postService.getPostsByCategoryAndPublish("Test category", 0, 10, true);
		assertEquals(1, page.getContent().size());
	}

	@Test
	@Order(4)
	public void testDeletePost() {
		postService.deletePostById(1);
		
		List<Post> posts = postService.getPosts();
		assertEquals(0, posts.size());
	}
	
}
