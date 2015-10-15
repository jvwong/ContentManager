package com.example.cm.cm_web.rest;

import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_repository.repository.CMSUserRepository;
import com.example.cm.cm_repository.service.CMSUserService;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author jvwong
 * @since 10/10/15.
 */
public class CMSUserRestEndpointTest {

    private static final Logger logger = LogManager.getLogger();

    private MockMvc mockMvc;
    private CMSUser mockUser;
    private CMSUserRepository mockRepository;
    private CMSUserService mockCmsUserService;
    private PasswordEncoder mockPasswordEncoder;
    private List<CMSUser> cmsUserList;

    @Before
    public void setUp() {
        mockPasswordEncoder = Mockito.mock(PasswordEncoder.class);
        mockCmsUserService = Mockito.mock(CMSUserService.class);
        mockRepository = Mockito.mock(CMSUserRepository.class);

        CMSUserRestEndpoint endpoint = new CMSUserRestEndpoint(
                mockRepository, mockCmsUserService, mockPasswordEncoder);
        mockMvc = standaloneSetup(endpoint).build();

        mockUser = new CMSUser(24L, "fullname24", "username24", "password24",
                "email24@email.com", "CMSUser");
        CMSUser cmsUser0 = new CMSUser(0L, "fullname0", "username0", "password0",
                "email0@email.com", "CMSUser");
        CMSUser cmsUser1 = new CMSUser(1L, "fullname1", "username1", "password1",
                "email1@email.com", "CMSUser");
        CMSUser cmsUser2 = new CMSUser(2L, "fullname2", "username2", "password2",
                "email2@email.com", "CMSUser");
        CMSUser cmsUser3 = new CMSUser(3L, "fullname3", "username3", "password3",
                "email3@email.com", "CMSUser");
        CMSUser cmsUser4 = new CMSUser(4L, "fullname4", "username4", "password4",
                "email4@email.com", "CMSUser");

        cmsUserList = Arrays.asList(
                cmsUser0,
                cmsUser1,
                cmsUser2,
                cmsUser3,
                cmsUser4);

    }


    /*
     * Return the list of users
     **/
    @Test
    public void cmsUserListTest() throws Exception {
        int pageNumber = 1;
        int pageSize = 3;
        Page<CMSUser> mockPage = new PageImpl<>(cmsUserList);
        Mockito.when(mockCmsUserService.cmsUserList(pageNumber, pageSize)).thenReturn(mockPage);

        mockMvc.perform(get("/rest/users/?page=" + pageNumber + "&size=" + pageSize))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.content", isA(Collection.class)))
                .andExpect(jsonPath("$.content", hasSize(cmsUserList.size())))
        ;

        Mockito.verify(mockCmsUserService, Mockito.atLeastOnce()).cmsUserList(pageNumber, pageSize);
    }

    /*
     * Return a particular user
     **/
    @Test
    public void cmsUserDetailTest() throws Exception {

        Mockito.when(mockRepository.findOne(mockUser.getId()))
                .thenReturn(mockUser);

        mockMvc.perform(get("/rest/users/" + mockUser.getId() + "/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id", is(mockUser.getId().intValue())))
                .andExpect(jsonPath("$.username", is(mockUser.getUsername())))
                .andExpect(jsonPath("$.version", is(mockUser.getVersion())))
                .andExpect(jsonPath("$.lastModifiedDate", is(mockUser.getLastModifiedDate())))
                .andExpect(jsonPath("$.fullName", is(mockUser.getFullName())))
                .andExpect(jsonPath("$.role", is(mockUser.getRole())))
        ;

        Mockito.verify(mockRepository, Mockito.atLeastOnce()).findOne(mockUser.getId());
    }

    /*
     * Create a CMSUser instance
     **/
    @Test
    public void saveCMSUserTest() throws Exception {

        CMSUser savedUser = new CMSUser(24L, "fullname1", "username1", "password1",
                "email1@email.com", "CMSUser");
        Mockito.when(mockPasswordEncoder.encode(mockUser.getPassword()))
                .thenReturn(mockUser.getPassword());
        Mockito.when(mockRepository.save(mockUser)).thenReturn(savedUser);

        Gson gson = new Gson();
        String jsonOut = gson.toJson(mockUser);

        mockMvc.perform(post("/rest/users/")
                .contentType("application/json;charset=UTF-8")
                .content(jsonOut))

                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("location", containsString("/services/rest/user/" + savedUser.getId())))
                ;

        Mockito.verify(mockRepository, Mockito.atLeastOnce()).save(mockUser);
    }

    /*
     * Attempt to save a duplicate user
     **/
    @Test
    public void saveDuplicateUserTest() throws Exception {

        Mockito.when(mockPasswordEncoder.encode(mockUser.getPassword()))
                .thenReturn(mockUser.getPassword());
        Mockito.when(mockRepository.save(mockUser)).thenThrow( new DataIntegrityViolationException(CMSUser.class.toString()));

        Gson gson = new Gson();
        String jsonOut = gson.toJson(mockUser);

        mockMvc.perform(post("/rest/users/")
                .contentType("application/json;charset=UTF-8")
                .content(jsonOut))
                .andExpect(status().isConflict())
        ;

        Mockito.verify(mockRepository, Mockito.atLeastOnce())
                .save(mockUser);
    }

}
