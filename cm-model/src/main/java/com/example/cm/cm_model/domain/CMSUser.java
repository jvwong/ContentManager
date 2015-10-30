package com.example.cm.cm_model.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "CMSUser", uniqueConstraints = {
 	 	@UniqueConstraint(name = "username", columnNames = {"username"}),
 		@UniqueConstraint(name = "email", columnNames = {"email"})
})
@AttributeOverride(name = "id", column = @Column(name = "Id"))
public class CMSUser extends DateAuditedEntity{

	private String fullName;
	private String username;
    private String password;
	private String email;
	private String role;

	public CMSUser(){
		this(null, null, null, null, null);
	}

	public CMSUser(String fullName,
				   String username,
				   String password,
				   String email,
				   String role){
		this(null, fullName, username, password, email, role);
	}

	public CMSUser(String id,
				   String fullName,
				   String username,
				   String password,
				   String email,
				   String role){
		this.setId(id);
		this.fullName = fullName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/* for serialization */
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	/* for de-serialization */
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore
	public String getEmail() {
		return email;
	}

	@JsonProperty
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object that) {
		return EqualsBuilder.reflectionEquals(this, that, "username", "email");
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "username", "email");
	}

	@Override
	public String toString() {
		return "[CMSUser: id=" + this.getId()
				+ ", createdDate=" + this.getCreatedDate()
				+ ", password=" + this.password
				+ ", fullName=" + this.fullName
				+ ", role=" + this.role
				+ ", username=" + this.username
				+ ", email=" + this.email
				+ "]";
	}
}
