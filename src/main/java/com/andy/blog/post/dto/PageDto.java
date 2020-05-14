package com.andy.blog.post.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PageDto<T> {
	
	@Getter @Setter
	private int page;

	@Getter @Setter
	private int size;

	@Getter @Setter
	private int total;

	@Getter @Setter
	private List<T> content;
	
}
