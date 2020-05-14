package com.andy.blog.category.entity;

import lombok.Getter;
import lombok.Setter;

public class CategoryCount {
	
	@Getter @Setter
	private String name;

	@Getter @Setter
	private int count;

	public CategoryCount() {
		super();
	}
	
	public CategoryCount(String name, int count) {
		super();
		this.name = name;
		this.count = count;
	}
	
	
}
