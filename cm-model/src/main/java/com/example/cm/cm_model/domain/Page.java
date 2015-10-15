package com.example.cm.cm_model.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jvwong
 */
@Entity
@Table(name = "Page", uniqueConstraints = {})
@AttributeOverride(name = "id", column = @Column(name = "PageId"))
public class Page extends DateByAuditedEntity {
	private String content;
	private Long articleId;

	public Page(){
		this(null, null);
	}

	public Page(Long articleId,
				String content){
		this(null, articleId, content);
	}
	public Page(Long id,
				Long articleId,
				String content){
		this.setId(id);
		this.articleId = articleId;
		this.content = content;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
