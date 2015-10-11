package com.example.cm.cm_web.rest;

import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_repository.repository.CMSUserRepository;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

/**
 * @author jvwong
 * @since 10/10/15.
 */
public class CMSUserRestEndpointTest {

    private static final Logger logger = LogManager.getLogger();

    private MockMvc mockMvc;
    private CMSUser mockUser;
    private CMSUserRepository mockRepository;
    private PasswordEncoder mockPasswordEncoder;

    @Before
    public void setUp() {
        mockPasswordEncoder = Mockito.mock(PasswordEncoder.class);
        mockRepository = Mockito.mock(CMSUserRepository.class);

        CMSUserRestEndpoint endpoint = new CMSUserRestEndpoint(
                mockRepository, mockPasswordEncoder);
        mockMvc = standaloneSetup(endpoint).build();

        mockUser = new CMSUser(24L, "fullname1", "username1", "password1",
                "email1@email.com", "CMSUser");
    }


    /*
     * Return the list of users
     **/
    @Test
    public void cmsUserListADMIN() throws Exception {
        List<CMSUser> list = new ArrayList<>();
        list.add(mockUser);

        Mockito.when(mockRepository.findAll()).thenReturn(list);

        mockMvc.perform(get("/rest/user/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(1)));

        Mockito.verify(mockRepository, Mockito.atLeastOnce()).findAll();
    }

    /*
     * Return a particular user
     * This should be admin protected
     **/
    @Test
    public void cmsUserDetail() throws Exception {

        Mockito.when(mockRepository.findOne(mockUser.getId()))
                .thenReturn(mockUser);

        mockMvc.perform(get("/rest/user/" + mockUser.getId()))
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
    public void saveCMSUser() throws Exception {

        CMSUser savedUser = new CMSUser(24L, "fullname1", "username1", "password1",
                "email1@email.com", "CMSUser");
        Mockito.when(mockPasswordEncoder.encode(mockUser.getPassword()))
                .thenReturn(mockUser.getPassword());
        Mockito.when(mockRepository.save(mockUser)).thenReturn(savedUser);

        Gson gson = new Gson();
        String jsonOut = gson.toJson(mockUser);

        mockMvc.perform(post("/rest/user/")
                .contentType("application/json;charset=UTF-8")
                .content(jsonOut))

                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
        ;

        Mockito.verify(mockRepository, Mockito.atLeastOnce()).save(mockUser);
    }

}
