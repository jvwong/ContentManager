package com.example.cm.cm_web.form;

import com.example.cm.cm_model.domain.CMSUser;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CMSUserForm {

    private final String DEFAULT_ROLE = "ROLE_CMSUSER";

    private String fullName;

    @NotNull
    @Size(min=5, max=16, message="{username.size}")
    private String username;

    @NotNull
    @Size(min=5, max=16, message="{password.size}")
    private String password;

    @NotNull
    @Size(min=5, max=16, message="{password.size}")
    private String passwordConfirm;

    @NotNull
    @Email
    private String email;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return this.passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @AssertTrue(message="Passwords should match")
    private boolean isValid() {
        return this.password.equals(this.passwordConfirm);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CMSUser toCMSUser() {
        CMSUser user = new CMSUser();
        user.setFullName(this.fullName);
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setRole(DEFAULT_ROLE);
        return user;
    }

    @Override
    public String toString() {
        return  "username: " + this.username +
                "; password: " + this.password +
                "; password confirm: " + this.passwordConfirm +
                "; email: " + this.email +
                "; full name: " + this.fullName
                ;
    }
}
