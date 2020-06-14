package com.andy.blog.post.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.andy.blog.BlogServerApplication;
import com.andy.blog.exception.RestException;
import com.andy.blog.post.entity.Post;
import com.andy.blog.post.model.PostRequest;
import com.andy.blog.post.service.PostService;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BlogServerApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PostServiceTests {
	
	@Autowired
	private PostService postService;
	
	@Test
	public void testAddPost() {
		try {
			List<String> categories = new ArrayList<String>();
			categories.add("Test category");
			
			PostRequest postRequest = new PostRequest();
			postRequest.setTitle("Test title");
			postRequest.setContent("Test content");
			postRequest.setPublish(true);
			postRequest.setCategories(categories);

			Optional<Post> optional = postService.addPost(postRequest);
			if(optional.isPresent())
				Assert.assertTrue(true);
			else
				Assert.assertTrue(false);
		} catch (RestException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddPostDuplicate() {
		List<String> categories = new ArrayList<String>();
		categories.add("Test category");
		
		PostRequest postRequest = new PostRequest();
		postRequest.setTitle("Test title");
		postRequest.setContent("Test content");
		postRequest.setPublish(true);
		postRequest.setCategories(categories);
		
		Assert.assertThrows(RestException.class, () -> {postService.addPost(postRequest);});
	}
	
	@Test
	public void testUpdatePost() {
		try {
			List<String> categories = new ArrayList<String>();
			categories.add("Test category");
			
			PostRequest postRequest = new PostRequest();
			postRequest.setTitle("Test title");
			postRequest.setContent("Test content2");
			postRequest.setPublish(true);
			postRequest.setCategories(categories);
			
			Optional<Post> optional = postService.updatePost(1, postRequest);
			if(optional.isPresent()) {
				Post post = optional.get();
				if(Objects.equals(post.getContent(), "Test content2"))
					Assert.assertTrue(true);
			} else {
				Assert.assertTrue(false);
			}
		} catch (RestException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetPosts() {
		List<Post> posts = postService.getPosts();
		Assert.assertEquals(1, posts.size());
	}

	@Test
	public void testGetPostById() {
		Optional<Post> optional = postService.getPostById(1);
		if(optional.isPresent())
			Assert.assertTrue(true);
		else
			Assert.assertTrue(false);
	}

	@Test
	public void testGetPostBySlug() {
		Optional<Post> optional = postService.getPostBySlug("test-title");
		if(optional.isPresent())
			Assert.assertTrue(true);
		else
			Assert.assertTrue(false);
	}
	
	@Test
	public void testGetPostPage() {
		Page<Post> page = postService.getPosts(0, 10);
		Assert.assertEquals(1, page.getContent().size());
	}

	@Test
	public void testGetPostsByPublish() {
		Page<Post> page = postService.getPostsByPublish(0, 10, true);
		Assert.assertEquals(1, page.getContent().size());
	}

	@Test
	public void testPostsByCategoryAndPublish() {
		Page<Post> page = postService.getPostsByCategoryAndPublish("Test category", 0, 10, true);
		Assert.assertEquals(1, page.getContent().size());
	}

	@Test
	public void testZDeletePost() {
		try {
			postService.deletePostById(1);
			List<Post> posts = postService.getPosts();
			Assert.assertEquals(0, posts.size());
		} catch (RestException e) {
			e.printStackTrace();
		}
	}
	
}
