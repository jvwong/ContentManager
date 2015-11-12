package com.example.cm.cm_model.domain;

/**
 * @author jvwong
 */
public class Page {
	private Long id;
	private Article article;
	private String content;

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Article getArticle() {
		return this.article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}
}
