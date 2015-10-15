package com.example.cm.cm_repository.repository;

import com.example.cm.cm_model.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jvwong
 */
public interface PageRepository extends JpaRepository<Page, Long>,
        PageRepositoryCustomization{
}
