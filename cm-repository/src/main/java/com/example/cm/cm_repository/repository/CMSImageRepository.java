package com.example.cm.cm_repository.repository;

import com.example.cm.cm_model.domain.CMSImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CMSImage data access object.
 * @author jvwong
 */
public interface CMSImageRepository extends JpaRepository<CMSImage, Long>,
                                            CMSImageRepositoryCustomization {
}
