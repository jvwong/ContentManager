package com.example.cm.cm_model.domain;

import javax.persistence.Id;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author jvwong
 */
@XmlRootElement(name = "article")
@Entity
@Table(
		name = "Article",
		uniqueConstraints = {}
)
@AttributeOverride(name = "id", column = @Column(name = "Id"))
public class Article {

	private String id;
	private Date publishDate;
	private String author;
	private String title;
	private String description;
	private String keywords;

	public Article(){
		this(null, null, null, null, null, null);
	}

	public Article(
			String id,
			Date publishDate,
			String author,
			String title,
			String description,
			String keywords){
		this.id = id;
		this.publishDate = publishDate;
		this.author = author;
		this.title = title;
		this.description = description;
		this.keywords = keywords;
	}

	@Id
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getPublishDate() {
		return this.publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

//	@JsonIgnore
//	public List<Page> getPages() {
//		return pages;
//	}
//
//	public void setPages(List<Page> pages) {
//		this.pages = pages;
//	}

	@Override
	public String toString() {
		return "[Article: id=" + id
				+ ", title=" + title
				+ ", author=" + author
				+ ", publishDate=" + publishDate
				+ ", description=" + description
				+ ", keywords=" + keywords
//				+ ", numPages=" + (pages == null ? 0 : pages.size())
				+ "]";
	}
}

