package com.example.cm.cm_web.web;

import com.example.cm.cm_model.domain.Article;

public class ArticleForm {
	
	private String title;
	private String author;
	private String description;
	private String keywords;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Article toArticle() {
		Article article = new Article();
		article.setTitle(this.title);
		article.setDescription(this.description);
		article.setKeywords(this.keywords);
		return article;
	}
}
