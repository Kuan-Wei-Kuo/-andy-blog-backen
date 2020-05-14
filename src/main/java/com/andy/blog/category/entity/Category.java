package com.andy.blog.category.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_category")
@IdClass(CategoryPK.class)
public class Category {

	@Id
	@Column(name = "post_id")
	@Getter @Setter
	public String postId;
	
	@Id
	@Column(name = "category_name")
	@Getter @Setter
	public String categoryName;
	
}
