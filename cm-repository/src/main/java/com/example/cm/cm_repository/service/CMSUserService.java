package com.example.cm.cm_repository.service;

import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_model.domain.JsonPatch;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jvowng
 */
public interface CMSUserService {

    @Secured("ROLE_ADMIN")
    Page<CMSUser> cmsUserList(Integer pageNumber, Integer pageSize);

    @Secured("ROLE_ADMIN")
    CMSUser cmsUser(String username);

    CMSUser getUser(String username);

    @Transactional(readOnly = false)
    CMSUser save(CMSUser cmsUser);

    boolean exists(String id);

    @Transactional(readOnly = false)
    void delete(String id);

    @Transactional(readOnly = false)
    CMSUser update(String username, List<JsonPatch> patches);
}
