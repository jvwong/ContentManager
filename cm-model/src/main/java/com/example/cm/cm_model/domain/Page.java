package com.example.cm.cm_model.domain;

import javax.persistence.Entity;

/**
 * @author jvwong
 */
//@Entity
public class Page {
	private String content;

	public String getContent() { return content; }

	public void setContent(String content) {
		this.content = content;
	}
}
