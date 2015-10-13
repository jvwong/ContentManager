package com.example.cm.cm_repository.service;

import com.example.cm.cm_model.domain.CMSUser;
import org.springframework.data.domain.Page;

/**
 * @author jvowng
 */
public interface CMSUserService {
    Page<CMSUser> cmsUserList(Integer pageNumber, Integer pageSize);
    CMSUser findUser(String username);
}
