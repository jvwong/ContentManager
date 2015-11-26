package com.example.cm.cm_model.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {

	private Long id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object that) {
		return EqualsBuilder.reflectionEquals(this, that, "id");
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "id");
	}
}
