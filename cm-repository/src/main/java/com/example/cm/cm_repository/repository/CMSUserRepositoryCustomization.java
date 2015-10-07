package com.example.cm.cm_repository.repository;

import com.example.cm.cm_model.domain.CMSUser;

public interface CMSUserRepositoryCustomization{
	CMSUser findByUsername(String username);
}
