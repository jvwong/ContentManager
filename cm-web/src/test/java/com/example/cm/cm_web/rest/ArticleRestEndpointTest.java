package com.example.cm.cm_web.rest;

import com.example.cm.cm_docrepository.service.ArticleService;
import com.example.cm.cm_model.domain.Article;
import com.example.cm.cm_model.domain.JsonPatch;
import com.google.gson.*;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.hamcrest.Matchers.*;

public class ArticleRestEndpointTest {

    private static final Logger logger
            = LoggerFactory.getLogger(ArticleRestEndpointTest.class);

    private MockMvc mockMvc;
    private Principal mockPrincipal;
    private ArticleService mockArticleService;
    private List<Article> articleList;
    private Article mockArticle;
    private String mockUsername;

    private final String MIME_JSON = "application/json;charset=UTF-8";
    private final String MIME_XML = "application/xml;";
    private final String MIME_JSON_PATCH = "application/json-patch";

    @Before
    public void setUp() {
        mockPrincipal = Mockito.mock(Principal.class);
        mockUsername = "mockUser";
        mockArticle = Mockito.mock(Article.class);
        mockArticleService = Mockito.mock(ArticleService.class);
        ArticleRestEndpoint endpoint = new ArticleRestEndpoint(mockArticleService);
        mockMvc = standaloneSetup(endpoint).build();

        Article mockArticle0 = new Article("title0", "descritpion0", "keywords0", null);
        Article mockArticle1 = new Article("title1", "descritpion1", "keywords1", null);
        Article mockArticle2 = new Article("title2", "descritpion2", "keywords2", null);
        Article mockArticle3 = new Article("title3", "descritpion3", "keywords3", null);
        Article mockArticle4 = new Article("title4", "descritpion4", "keywords4", null);

        articleList = Arrays.asList(
                mockArticle0,
                mockArticle1,
                mockArticle2,
                mockArticle3,
                mockArticle4);
    }


    /*
     * Return the paged list of Articles
     **/
    @Test
    public void articleListTest() throws Exception {
        int pageNumber = 1;
        int pageSize = 3;


        Page<Article> mockPage = new PageImpl<>(articleList);

        Mockito.when(mockPrincipal.getName()).thenReturn(mockUsername);
        Mockito.when(mockArticleService
                        .getPagedListByAuthor(pageNumber, pageSize, mockUsername))
                        .thenReturn(mockPage);

        mockMvc.perform(get("/rest/articles/?page={pageNumber}&size={pageSize}", pageNumber, pageSize)
                .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MIME_JSON))
                .andExpect(jsonPath("$.content", isA(Collection.class)))
                .andExpect(jsonPath("$.content", hasSize(articleList.size())))
        ;

        Mockito.verify(mockArticleService, Mockito.atLeastOnce())
                .getPagedListByAuthor(pageNumber, pageSize, mockUsername);
    }

    /*
     * Return article details. Deny access if the principal and author
     * are not identical
     **/
    @Test
    public void articleDetailTest() throws Exception {

        String uuid = UUID.randomUUID().toString();
        Article someArticle = articleList.get(0);


        Mockito.when(mockArticleService.findOne(uuid))
                .thenReturn(someArticle);

        Mockito.when(mockArticle.getAuthor())
                .thenReturn(someArticle.getAuthor());

        Mockito.when(mockPrincipal.getName())
                .thenReturn(someArticle.getAuthor());


        mockMvc.perform(get("/rest/articles/{uuid}/", uuid)
                .principal(mockPrincipal)
                    .accept(MIME_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MIME_JSON))
        ;

        Mockito.verify(mockArticleService, Mockito.atLeastOnce())
                .findOne(uuid);
    }

    /*
     * Retrieve a non-existent article
     **/
    @Test
    public void articleDetailFailTest() throws Exception {

        String uuid = UUID.randomUUID().toString();

        Mockito.when(mockArticleService.findOne(uuid))
                .thenReturn(null);

        mockMvc.perform(get("/rest/articles/{uuid}/", uuid)
                .accept(MIME_JSON))
                .andExpect(status().isNotFound())
        ;

        Mockito.verify(mockArticleService, Mockito.atLeastOnce())
                .findOne(uuid);
    }

    /*
     * Create a CMSUser instance
     **/
    @Test
    public void saveArticleTest() throws Exception {


        Article unsaved = new Article("title30", "descritpion30", "keywords30", null);
        Article saved = new Article("title30", "descritpion30", "keywords30", null);
        String uuid = UUID.randomUUID().toString();
        saved.setId(uuid);

        Mockito.when(mockArticleService.save(unsaved)).thenReturn(saved);

        Gson gson = new Gson();
        String jsonOut = gson.toJson(unsaved);

        mockMvc.perform(post("/rest/articles/")
                .accept(MIME_JSON)
                .contentType(MIME_JSON)
                .content(jsonOut))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MIME_JSON))
                .andExpect(header().string(
                        "location",
                        Matchers.containsString(
                                "/services/rest/articles/" + saved.getId())
                        )
                )
        ;

        Mockito.verify(mockArticleService, Mockito.atLeastOnce()).save(unsaved);
    }

    /*
     * Attempt to save a duplicate
     **/
    @Test
    public void saveDuplicateArticleTest() throws Exception {
        Article unsaved = new Article("title30", "descritpion30", "keywords30", null);
        Mockito.when(mockArticleService.save(unsaved))
                .thenThrow(new DataIntegrityViolationException(Article.class.toString()));

        Gson gson = new Gson();
        String jsonOut = gson.toJson(unsaved);

        mockMvc.perform(post("/rest/articles/")
                .principal(mockPrincipal)
                .contentType("application/json;charset=UTF-8")
                .content(jsonOut))
                .andExpect(status().isConflict())
        ;

        Mockito.verify(mockArticleService, Mockito.atLeastOnce())
                .save(unsaved);
    }


    /**
     * Delete an existing Article
     */
    @Test
    public void deleteArticleTest() throws Exception {
        Article saved = new Article("title30", "descritpion30", "keywords30", null);
        String uuid = UUID.randomUUID().toString();
        saved.setId(uuid);

        Mockito.when(mockArticleService.exists(uuid))
                .thenReturn(true);
        Mockito.doNothing().when(mockArticleService).delete(saved.getId());

        mockMvc.perform(delete("/rest/articles/{uuid}/", uuid)
                .accept(MIME_JSON))
                .andExpect(status().isNoContent())
        ;

        Mockito.verify(mockArticleService, Mockito.atLeastOnce()).exists(uuid);
        Mockito.verify(mockArticleService, Mockito.atLeastOnce()).delete(uuid);
    }

    @Test
    public void failingDeleteArticleTest() throws Exception {
        Article saved = new Article("title30", "descritpion30", "keywords30", null);
        String uuid = UUID.randomUUID().toString();
        saved.setId(uuid);

        Mockito.when(mockArticleService.exists(uuid))
                .thenReturn(false);

        mockMvc.perform(delete("/rest/articles/{uuid}/", uuid)
                .accept(MIME_JSON))
                .andExpect(status().isNotFound())
        ;

        Mockito.verify(mockArticleService, Mockito.atLeastOnce()).exists(uuid);
        Mockito.verify(mockArticleService, Mockito.never()).delete(uuid);
    }


    /**
     * Modify an existing Article via PATCH. Follows JSON patch
     * protocol specification (http://jsonpatch.com/)
     */
    @Test
    public void updateArticleTest() throws Exception {
        Article saved = new Article("title30", "descritpion30", "keywords30", null);
        String uuid = UUID.randomUUID().toString();
        saved.setId(uuid);

        JsonArray array = new JsonArray();
        JsonObject update = new JsonObject();
        String update_operation = "replace";
        String update_path = "/title";
        String update_value = "updatedTitle";
        update.addProperty("op", update_operation);
        update.addProperty("path", update_path);
        update.addProperty("value", update_value);
        array.add(update);

        JsonPatch patch
                = new JsonPatch(update_operation,
                new URI(update_path),
                update_value);
        List<JsonPatch> patches = Arrays.asList(patch);

        saved.setTitle(update_value);
        Gson gson = new Gson();
        String jsonOut = gson.toJson(array);

        Mockito.when(mockArticleService.exists(uuid))
                .thenReturn(true);
        Mockito.when(mockArticleService.update(saved.getId(), patches))
                .thenReturn(saved);

        mockMvc.perform(patch("/rest/articles/{id}/", uuid)
                    .accept(MIME_JSON)
                    .contentType(MIME_JSON)
                    .content(jsonOut))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MIME_JSON))
                .andExpect(jsonPath("$.id", is(saved.getId())))
                .andExpect(jsonPath("$.title", is(saved.getTitle())))
        ;

        Mockito.verify(mockArticleService, Mockito.atLeastOnce()).exists(uuid);
        Mockito.verify(mockArticleService, Mockito.atLeastOnce()).update(uuid, patches);
    }

}
