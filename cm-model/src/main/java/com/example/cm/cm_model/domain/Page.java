package com.example.cm.cm_model.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jvwong
 */
@XmlRootElement(name = "page")
@Entity
@Table(
		name = "Page",
		uniqueConstraints = {}
)
@AttributeOverride(name = "id", column = @Column(name = "Id"))
public class Page {
	private Long id;
	private String content;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
