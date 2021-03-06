package com.example.cm.cm_web.rest;

import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_repository.service.CMSUserService;
import com.example.cm.cm_web.security.TokenAuthenticationService;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author jvwong
 * @since 10/10/15.
 */
public class AuthRestEndpointTest {

    private MockMvc mockMvc;
    private CMSUser mockUser;
    private CMSUserService mockService;
    private AuthenticationManager mockAuthManager;
    private Authentication authRequest;
    private Long id;

    @Before
    public void setUp() {
        TokenAuthenticationService mockTokenAuthenticationService
                = Mockito.mock(TokenAuthenticationService.class);
        mockService = Mockito.mock(CMSUserService.class);
        mockAuthManager = Mockito.mock(AuthenticationManager.class);
        id = 24L;

        AuthRestEndpoint endpoint = new AuthRestEndpoint(
                mockTokenAuthenticationService, mockService, mockAuthManager
        );

        mockMvc = standaloneSetup(endpoint).build();
        mockUser = new CMSUser(id, "fullname1", "username1", "password1",
                "email1@email.com", "CMSUser");

        authRequest
                = new UsernamePasswordAuthenticationToken(
                mockUser.getUsername(), mockUser.getPassword());

        Mockito.when(mockService.getUser(mockUser.getUsername())).thenReturn(mockUser);
        Mockito.when(mockAuthManager.authenticate(authRequest)).thenReturn(null);
    }


    /*
     * Note that the .equals should be updated in target class
     * */
    @Test
    public void authenticate() throws Exception {
        Gson gson = new Gson();
        String jsonOut = gson.toJson(mockUser);

        mockMvc.perform(post("/rest/auth/")
                .contentType("application/json;charset=UTF-8")
                .content(jsonOut))

                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        Mockito.verify(mockService, Mockito.atLeastOnce()).getUser(mockUser.getUsername());
        Mockito.verify(mockAuthManager, Mockito.atLeastOnce()).authenticate(authRequest);
    }

}
