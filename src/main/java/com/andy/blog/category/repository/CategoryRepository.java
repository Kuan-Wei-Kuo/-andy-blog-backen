package com.andy.blog.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.andy.blog.category.entity.Category;
import com.andy.blog.category.entity.CategoryPK;

public interface CategoryRepository extends JpaRepository<Category, CategoryPK> {

	@Query(nativeQuery = true, value = "SELECT category_name, COUNT(*) AS post_count FROM post_category GROUP BY category_name")
	public List<Object[]> getCategoryCounts();
	
}
