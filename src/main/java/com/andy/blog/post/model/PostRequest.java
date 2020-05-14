package com.andy.blog.post.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PostRequest {

	@Getter @Setter
	private String title;

	@Getter @Setter
	private String content;

	@Getter @Setter
	private boolean publish;

	@Getter @Setter
	private List<String> categories;

}
