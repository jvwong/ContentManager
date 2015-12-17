package com.example.cm.cm_repository.service;

import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_model.domain.JsonPatch;
import com.example.cm.cm_repository.repository.CMSUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author jvwong
 */
@Service
public class CMSUserServiceImpl implements CMSUserService {
    private static final Logger logger
            = LoggerFactory.getLogger(CMSUserServiceImpl.class);


    @Autowired
    private CMSUserRepository cmsUserRepository;

    @Override
    @Secured("ROLE_ADMIN")
    public Page<CMSUser> cmsUserList(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest =
                new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "createdDate");
        return cmsUserRepository.findAll(pageRequest);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public CMSUser cmsUser(String username){
        CMSUser cmsUser = cmsUserRepository.findByUsername(username);
        return cmsUser;
    }

    @Override
    public CMSUser getUser(String username){
        CMSUser cmsUser = cmsUserRepository.findByUsername(username);
        return cmsUser;
    }

    @Override
    @Transactional
    public CMSUser save(CMSUser cmsUser){
        CMSUser saved = cmsUserRepository.save(cmsUser);
        return saved;
    }

    @Override
    public boolean exists(String username){
        return cmsUserRepository.findByUsername(username) != null;
    }

    /**
     * Delete the record with the given username if it exists
     * @param username the username for the CMSUser
     */
    @Override
    @Transactional
    public void delete(String username){
        CMSUser user = cmsUserRepository.findByUsername(username);
        cmsUserRepository.delete(user.getId());
    }

    /**
     * Update the record with the given username if it exists.
     *
     * BUG
     * @param username  the username  for the {@link CMSUser}
     * @param patches the List of JsonPatch updates
     * @return Article representation of updated {@link CMSUser}
     */
    @Override
    @Transactional
    public CMSUser update(String username, List<JsonPatch> patches){
        CMSUser user = cmsUserRepository.findByUsername(username);
        Class aClass = user.getClass();

        // TODO - Case: change password?
        // TODO - Case: not changing a @UniqueConstraint for CMSUser?

        for(JsonPatch patch: patches){
            String operation = patch.getOp();
            String[] segments = patch.getPath().toString().substring(1).split("/");

            switch (operation)
            {
                case "replace":
                    for(String path: segments){
                        try
                        {
                            Field field = aClass.getDeclaredField(path);
                            field.setAccessible(true);
                            field.set(user, patch.getValue());
                        }
                        catch (NoSuchFieldException | IllegalAccessException nfe)
                        {
                            // Do nothing
                            logger.error("Error updating field " + path);
                        }
                    }
                    break;
            }

        }
        return cmsUserRepository.save(user);
    }
}