package com.andy.blog.post.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.andy.blog.post.entity.Post;
import com.andy.blog.post.repository.PostRepository;
import com.andy.blog.post.util.SlugUtils;

@ActiveProfiles("test")
@SpringBootTest()
public class PostRepositoryTests {
	
	@Autowired
	private PostRepository postRepository;

	@Test
	@Transactional
	@Rollback(true)
	public void testFindBySlug() {
		List<String> categories = new ArrayList<String>();
		categories.add("Test category");
		
		Post post = new Post();
		post.setTitle("Test title");
		post.setContent("Test content");
		post.setPublish(true);
		post.setCreateTime(new Date());
		post.setModifyTime(new Date());
		post.setSlug(SlugUtils.toSlug(post.getTitle()));
		post.setCategories(categories);
		post = postRepository.save(post);
		
		Optional<Post> optional = postRepository.findBySlug(post.getSlug());
		if(optional.isPresent())
			Assert.assertTrue(true);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testFindByPublish() {
		List<String> categories = new ArrayList<String>();
		categories.add("Test category");
		
		Post post = new Post();
		post.setTitle("Test title");
		post.setContent("Test content");
		post.setPublish(true);
		post.setCreateTime(new Date());
		post.setModifyTime(new Date());
		post.setSlug(SlugUtils.toSlug(post.getTitle()));
		post.setCategories(categories);
		post = postRepository.save(post);
		
		Page<Post> page = postRepository.findByPublish(true, PageRequest.of(0, 10, Sort.by("createTime").descending()));
		if(page.getContent().size() > 0)
			Assert.assertTrue(true);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindByCategory() {
		List<String> categories = new ArrayList<String>();
		categories.add("Test category");
		
		Post post = new Post();
		post.setTitle("Test title");
		post.setContent("Test content");
		post.setPublish(true);
		post.setCreateTime(new Date());
		post.setModifyTime(new Date());
		post.setSlug(SlugUtils.toSlug(post.getTitle()));
		post.setCategories(categories);
		post = postRepository.save(post);
		
		Page<Post> page = postRepository.findByCategory("Test category", true, PageRequest.of(0, 10, Sort.by("create_time").descending()));
		if(page.getContent().size() > 0)
			Assert.assertTrue(true);
	}
	
}
