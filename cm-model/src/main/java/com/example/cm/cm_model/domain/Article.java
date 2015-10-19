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
	private Date publishedDate;
	private String author;
	private String title;
	private String description;
	private String keywords;

	public Article(){
		this(null, null, null, null, null, null);
	}

	public Article(
			String id,
			Date publishedDate,
			String author,
			String title,
			String description,
			String keywords){
		this.id = id;
		this.publishedDate = publishedDate;
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

	public Date getPublishedDate() {
		return this.publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
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
				+ ", publishedDate=" + publishedDate
				+ ", description=" + description
				+ ", keywords=" + keywords
//				+ ", numPages=" + (pages == null ? 0 : pages.size())
				+ "]";
	}
}

