package com.andy.blog.post.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PostDto {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
	@Getter @Setter
	private int id;

	@Getter @Setter
	private String title;

	@Getter @Setter
	private String content;

	@Getter @Setter
	private String date;

	@Getter @Setter
	private String slug;

	@Getter @Setter
	private List<String> categories;

    public void setSubmissionDate(Date date) {
    	this.date = dateFormat.format(date);
    }
    
}
