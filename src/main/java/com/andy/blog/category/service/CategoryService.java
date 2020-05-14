package com.andy.blog.category.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andy.blog.category.entity.CategoryCount;
import com.andy.blog.category.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<CategoryCount> getCategoryCounts() {
		List<Object[]> countObjects = categoryRepository.getCategoryCounts();
		List<CategoryCount> categoryCounts = new ArrayList<CategoryCount>();
		countObjects.forEach(objects -> categoryCounts.add(new CategoryCount(objects[0].toString(), Integer.valueOf(objects[1].toString()))));
		return categoryCounts;
	}
	
}
