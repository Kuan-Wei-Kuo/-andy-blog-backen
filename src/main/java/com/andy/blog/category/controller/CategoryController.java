package com.andy.blog.category.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andy.blog.category.entity.CategoryCount;
import com.andy.blog.category.service.CategoryService;

@RestController
@CrossOrigin
@RequestMapping("/public/api/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public List<CategoryCount> getCategoryCounts() {
		return categoryService.getCategoryCounts();
	}
	
}
