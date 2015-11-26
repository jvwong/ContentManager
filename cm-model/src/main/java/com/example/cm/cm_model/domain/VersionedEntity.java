package com.example.cm.cm_model.domain;

import org.springframework.data.annotation.Version;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class VersionedEntity extends BaseEntity{

	private Long version;

	/**
	 *
	 * @return Long Version
	 */
	@Version
	public Long getVersion(){
		return this.version;
	}

	/**
	 * Declare package private
	 * @param version The version
	 */
	void setVersion(Long version){
		this.version = version;
	}
}
