package com.example.cm.cm_repository.repository;

import com.example.cm.cm_model.domain.CMSUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CMSUserRepository extends JpaRepository<CMSUser, Long>,
										   CMSUserRepositoryCustomization{
}
