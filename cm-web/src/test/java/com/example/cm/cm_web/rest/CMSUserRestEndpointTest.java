package com.example.cm.cm_web.rest;

import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_model.domain.JsonPatch;
import com.example.cm.cm_repository.service.CMSImageService;
import com.example.cm.cm_repository.service.CMSUserService;
import com.example.cm.cm_web.form.CMSUserForm;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author jvwong
 * @since 10/10/15.
 */
public class CMSUserRestEndpointTest {
    private final String MIME_JSON = "application/json;charset=UTF-8";

    private MockMvc mockMvc;
    private CMSUser mockUser;
    private CMSUserForm mockUserForm;
    private CMSUserService mockCmsUserService;
    private CMSImageService cmsImageService;
    private PasswordEncoder mockPasswordEncoder;
    private List<CMSUser> cmsUserList;
    private Long id;

    @Before
    public void setUp() {
        mockPasswordEncoder = Mockito.mock(PasswordEncoder.class);
        mockCmsUserService = Mockito.mock(CMSUserService.class);
        cmsImageService = Mockito.mock(CMSImageService.class);

        id = 24L;

        CMSUserRestEndpoint endpoint = new CMSUserRestEndpoint(
                mockCmsUserService, cmsImageService, mockPasswordEncoder);
        mockMvc = standaloneSetup(endpoint).build();

        mockUser = new CMSUser(id, "fullname24", "username24", "password24",
                "email24@email.com", "CMSUser");
        CMSUser cmsUser0 = new CMSUser(1L, "fullname0", "username0", "password0",
                "email0@email.com", "CMSUser");
        CMSUser cmsUser1 = new CMSUser(2L, "fullname1", "username1", "password1",
                "email1@email.com", "CMSUser");
        CMSUser cmsUser2 = new CMSUser(3L, "fullname2", "username2", "password2",
                "email2@email.com", "CMSUser");
        CMSUser cmsUser3 = new CMSUser(4L, "fullname3", "username3", "password3",
                "email3@email.com", "CMSUser");
        CMSUser cmsUser4 = new CMSUser(5L, "fullname4", "username4", "password4",
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
                .andExpect(content().contentType(MIME_JSON))
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

        Mockito.when(mockCmsUserService.cmsUser(mockUser.getUsername()))
                .thenReturn(mockUser);

        mockMvc.perform(get("/rest/users/" + mockUser.getUsername() + "/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MIME_JSON))
                .andExpect(jsonPath("$.username", is(mockUser.getUsername())))
                .andExpect(jsonPath("$.version", is(mockUser.getVersion())))
                .andExpect(jsonPath("$.lastModifiedDate", is(mockUser.getLastModifiedDate())))
                .andExpect(jsonPath("$.fullName", is(mockUser.getFullName())))
                .andExpect(jsonPath("$.role", is(mockUser.getRole())))
        ;

        Mockito.verify(mockCmsUserService, Mockito.atLeastOnce()).cmsUser(mockUser.getUsername());
    }

    /*
     * Create a CMSUser instance
     **/
    @Test
    public void saveCMSUserTest() throws Exception {

        Mockito.when(mockPasswordEncoder.encode(org.mockito.Matchers.anyString()))
                .thenReturn(mockUser.getPassword());
        Mockito.when(mockCmsUserService.save(org.mockito.Matchers.any(CMSUser.class)))
                .thenReturn(mockUser);

        mockUserForm = new CMSUserForm();
        mockUserForm.setFullName(mockUser.getFullName());
        mockUserForm.setUsername(mockUser.getUsername());
        mockUserForm.setPassword(mockUser.getPassword());
        mockUserForm.setPasswordConfirm(mockUser.getPassword());
        mockUserForm.setEmail(mockUser.getEmail());

        Gson gson = new Gson();
        String jsonOut = gson.toJson(mockUserForm);

        mockMvc.perform(post("/rest/users/")
                .contentType(MIME_JSON)
                .content(jsonOut))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MIME_JSON))
                .andExpect(header().string("location", containsString("/services/rest/users/")))
        ;

        Mockito.verify(mockCmsUserService, Mockito.atLeastOnce()).save(org.mockito.Matchers.any(CMSUser.class));
    }

    /*
     * Set the avatar for the CMSUser
     **/
    @Test
    public void setAvatarTest() throws Exception {

        MultipartFile mockMultipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockMultipartFile.isEmpty()).thenReturn(false);

        Mockito.doNothing().when(cmsImageService)
                .uploadAvatar(
                        org.mockito.Matchers.any(String.class),
                        org.mockito.Matchers.any(InputStream.class),
                        org.mockito.Matchers.any(String.class));
        MockMultipartFile mockFile
                = new MockMultipartFile("avatar", "icon.png", "image/png", "asd".getBytes());

        MvcResult mvcResult
                = mockMvc.perform(fileUpload("/rest/users/{username}/avatar/",
                mockUser.getUsername())
                .file(mockFile).contentType("multipart/form-data").accept(MIME_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isAccepted());

        Mockito.verify(cmsImageService,
                Mockito.atLeastOnce()).uploadAvatar(
                org.mockito.Matchers.any(String.class),
                org.mockito.Matchers.any(InputStream.class),
                org.mockito.Matchers.any(String.class));
    }

    /**
     * Delete an existing CMSUser
     */
    @Test
    public void deleteCmsUserTest() throws Exception {

        Mockito.when(mockCmsUserService.exists(mockUser.getUsername()))
                .thenReturn(true);
        Mockito.doNothing().when(mockCmsUserService).delete(mockUser.getUsername());

        mockMvc.perform(delete("/rest/users/{username}/", mockUser.getUsername())
                .accept(MIME_JSON))
                .andExpect(status().isNoContent())
        ;

        Mockito.verify(mockCmsUserService, Mockito.atLeastOnce()).exists(mockUser.getUsername());
        Mockito.verify(mockCmsUserService, Mockito.atLeastOnce()).delete(mockUser.getUsername());
    }


    /**
     * Modify an existing CMSUser via PATCH. Follows JSON patch
     * protocol specification (http://jsonpatch.com/)
     */
    @Test
    public void updateCMSUserTest() throws Exception {

        JsonArray array = new JsonArray();
        JsonObject update = new JsonObject();
        String update_operation = "replace";
        String update_path = "/email";
        String update_value = "updatedEmail@example.org";
        update.addProperty("op", update_operation);
        update.addProperty("path", update_path);
        update.addProperty("value", update_value);
        array.add(update);

        JsonPatch patch
                = new JsonPatch(update_operation,
                new URI(update_path),
                update_value);
        List<JsonPatch> patches = Arrays.asList(patch);

        mockUser.setEmail(update_value);
        Gson gson = new Gson();
        String jsonOut = gson.toJson(array);

        Mockito.when(mockCmsUserService.exists(mockUser.getUsername()))
                .thenReturn(true);
        Mockito.when(mockCmsUserService.update(mockUser.getUsername(), patches))
                .thenReturn(mockUser);

        mockMvc.perform(patch("/rest/users/{username}/", mockUser.getUsername())
                .accept(MIME_JSON)
                .contentType(MIME_JSON)
                .content(jsonOut))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MIME_JSON))
                .andExpect(jsonPath("$.username", is(mockUser.getUsername())))
                .andExpect(jsonPath("$.fullName", is(mockUser.getFullName())))
        ;

        Mockito.verify(mockCmsUserService, Mockito.atLeastOnce()).exists(mockUser.getUsername());
        Mockito.verify(mockCmsUserService, Mockito.atLeastOnce()).update(mockUser.getUsername(), patches);
    }


}
