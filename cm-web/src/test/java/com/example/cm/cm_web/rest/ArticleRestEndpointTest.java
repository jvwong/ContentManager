package com.example.cm.cm_web.rest;

import com.example.cm.cm_jcrrepository.service.ArticleService;
import com.example.cm.cm_model.domain.Article;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import org.hamcrest.Matchers;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

public class ArticleRestEndpointTest {

    private static final Logger logger
            = LoggerFactory.getLogger(ArticleRestEndpointTest.class);

    private MockMvc mockMvc;
    private Principal mockPrincipal;
    private ArticleService mockArticleService;
    private List<Article> articleList;

    private final String MIME_JSON = "application/json;charset=UTF-8";
    private final String MIME_XML = "application/xml;";

    @Before
    public void setUp() {
        mockPrincipal = Mockito.mock(Principal.class);
        mockArticleService = Mockito.mock(ArticleService.class);
        ArticleRestEndpoint endpoint = new ArticleRestEndpoint(mockArticleService);
        mockMvc = standaloneSetup(endpoint).build();

        Article mockArticle0 = new Article("title0", "descritpion0", "keywords0");
        Article mockArticle1 = new Article("title1", "descritpion1", "keywords1");
        Article mockArticle2 = new Article("title2", "descritpion2", "keywords2");
        Article mockArticle3 = new Article("title3", "descritpion3", "keywords3");
        Article mockArticle4 = new Article("title4", "descritpion4", "keywords4");

        articleList = Arrays.asList(
                mockArticle0,
                mockArticle1,
                mockArticle2,
                mockArticle3,
                mockArticle4);
    }


    /*
     * Return a paged list of articles
     **/
    @Test
    public void articleListTest() throws Exception {

        Mockito.when(mockArticleService.getList())
                .thenReturn(articleList);


        mockMvc.perform(
                get("/rest/articles/").accept(MIME_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MIME_JSON))
                .andExpect(jsonPath("$", Matchers.isA(Collection.class)))
                .andExpect(jsonPath("$", Matchers.hasSize(articleList.size())))
                ;

        Mockito.verify(mockArticleService, Mockito.atLeastOnce())
                .getList();
    }

    /*
     * Return article details
     **/
    @Test
    public void articleDetailTest() throws Exception {

        String uuid = UUID.randomUUID().toString();

        Mockito.when(mockArticleService.findOne(uuid))
                .thenReturn(articleList.get(0));

        mockMvc.perform(get("/rest/articles/" + uuid + "/")
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

        mockMvc.perform(get("/rest/articles/" + uuid + "/")
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


        Article unsaved = new Article("title30", "descritpion30", "keywords30");
        Article saved = new Article("title30", "descritpion30", "keywords30");
        String uuid = UUID.randomUUID().toString();
        saved.setId(uuid);

        Mockito.when(mockArticleService.save(unsaved)).thenReturn(saved);

        Gson gson = new Gson();
        String jsonOut = gson.toJson(unsaved);

        mockMvc.perform(post("/rest/articles/")
                .principal(mockPrincipal)
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
        Article unsaved = new Article("title30", "descritpion30", "keywords30");
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
}
