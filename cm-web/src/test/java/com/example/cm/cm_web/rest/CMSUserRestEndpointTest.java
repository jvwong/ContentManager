package com.example.cm.cm_web.rest;

import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_repository.repository.CMSUserRepository;
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
     * Note that the .equals should be updated in target class
     * */
    @Test
    public void cmsUserList() throws Exception {
        List<CMSUser> list = new ArrayList<>();
        list.add(mockUser);

        Mockito.when(mockRepository.findAll()).thenReturn(list);

        mockMvc.perform(get("/rest/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(1)));

        Mockito.verify(mockRepository, Mockito.atLeastOnce()).findAll();
    }

}
