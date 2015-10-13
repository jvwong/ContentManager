package com.example.cm.cm_repository.service;

import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_repository.repository.CMSUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jvwong
 */
@Service
@Transactional
public class CMSUserServiceImpl implements CMSUserService {

    @Autowired
    private CMSUserRepository cmsUserRepository;

    public Page<CMSUser> cmsUserList(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest =
                new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "createdDate");
        return cmsUserRepository.findAll(pageRequest);
    }

    public CMSUser findUser(String username){

        CMSUser cmsUser = cmsUserRepository.findByUsername(username);
        return cmsUser;
    }
}