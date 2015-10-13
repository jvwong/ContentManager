package com.example.cm.cm_web.rest;

import com.example.cm.cm_model.domain.Article;
import com.example.cm.cm_repository.service.ArticleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ArticleRestEndpointTest {
    private static final Logger logger = LogManager.getLogger();

    private static final int PAGE_SIZE = 50;

    private MockMvc mockMvc;
    private ArticleService mockArticleService;
    private List<Article> articleList;

    @Before
    public void setUp() {
        mockArticleService = Mockito.mock(ArticleService.class);
        ArticleRestEndpoint endpoint = new ArticleRestEndpoint(mockArticleService);
        mockMvc = standaloneSetup(endpoint).build();

        Article mockArticle0 = new Article(0L, "title0", "descritpion0", "keywords0");
        Article mockArticle1 = new Article(1L, "title1", "descritpion1", "keywords1");
        Article mockArticle2 = new Article(2L, "title2", "descritpion2", "keywords2");
        Article mockArticle3 = new Article(3L, "title3", "descritpion3", "keywords3");
        Article mockArticle4 = new Article(4L, "title4", "descritpion4", "keywords4");

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

        int pageNumber = 1;
        int pageSize = 3;

        Page<Article> mockPage = new PageImpl<>(articleList);
        Mockito.when(mockArticleService.articleList(pageNumber,pageSize))
                .thenReturn(mockPage);

        mockMvc.perform(get("/rest/articles/?page=" + pageNumber + "&size=" + pageSize))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.content", isA(Collection.class)))
                .andExpect(jsonPath("$.content", hasSize(articleList.size())))
                ;

        Mockito.verify(mockArticleService, Mockito.atLeastOnce())
                .articleList(pageNumber, pageSize);
    }

    /*
     * Return a given article's details
     **/
    @Test
    public void articleDetailTest() throws Exception {

        Mockito.when(mockArticleService.article(articleList.get(0).getId()))
                .thenReturn(articleList.get(0));

        mockMvc.perform(get("/rest/articles/" + articleList.get(0).getId() + "/")
                    .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
        ;

        Mockito.verify(mockArticleService, Mockito.atLeastOnce())
                .article(articleList.get(0).getId());
    }
}
