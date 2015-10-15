package com.example.cm.cm_model.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jvwong
 */
@XmlRootElement(name = "article")
@Entity
@Table(name = "Article", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"title", "createdBy"})
})
@AttributeOverride(name = "id", column = @Column(name = "ArticleId"))
public class Article extends DateByAuditedEntity {

	private String title;
	private String description;
	private String keywords;

	public Article(){
		this(null, null, null);
	}

	public Article(String title,
				   String description,
				   String keywords){
		this(null, title, description, keywords);
	}

	public Article(Long id,
				   String title,
				   String description,
				   String keywords){
		this.setId(id);
		this.title = title;
		this.description = description;
		this.keywords = keywords;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	@Override
	public boolean equals(Object that) {
		return EqualsBuilder.reflectionEquals(this, that, "title", "description", "keywords");
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "title", "description", "keywords");
	}

	@Override
	public String toString() {
		return "[Article: id=" + this.getId()
				+ ", createdBy=" + this.getCreatedBy()
				+ ", createdDate=" + this.getCreatedDate()
				+ ", title=" + this.title
				+ ", description=" + this.description
				+ ", keywords=" + this.keywords
				+ "]";
	}
}
