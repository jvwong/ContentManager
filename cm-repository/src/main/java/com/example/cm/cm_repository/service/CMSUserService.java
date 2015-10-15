package com.example.cm.cm_repository.service;

import com.example.cm.cm_model.domain.CMSUser;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;

/**
 * @author jvowng
 */
public interface CMSUserService {

    //TODO wtf denies everyone
    @Secured("ADMIN")
    Page<CMSUser> cmsUserList(Integer pageNumber, Integer pageSize);

    //@Secured("ADMIN")
    CMSUser cmsUser(String username);

    CMSUser save(CMSUser cmsUser);
}
