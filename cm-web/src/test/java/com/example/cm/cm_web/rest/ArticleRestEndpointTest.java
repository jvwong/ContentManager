package com.example.cm.cm_web.rest;

import com.example.cm.cm_model.domain.Article;
import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_repository.repository.ArticleRepository;
import com.example.cm.cm_repository.service.ArticleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ArticleRestEndpointTest {
    private static final Logger logger = LogManager.getLogger();

    private static final int PAGE_SIZE = 50;

    private MockMvc mockMvc;
    private CMSUser mockUser;
    private Article mockArticle;
    private ArticleService mockArticleService;
    private List<Article> articleList;

    @Before
    public void setUp() {
        mockArticleService = Mockito.mock(ArticleService.class);
        ArticleRestEndpoint endpoint = new ArticleRestEndpoint(mockArticleService);
        mockMvc = standaloneSetup(endpoint).build();

        mockArticle = new Article(24L, "title1", "descritpion1", "keywords1");
        mockUser = new CMSUser(24L, "fullname1", "username1", "password1",
                "email1@email.com", "CMSUser");

        articleList = new ArrayList<>();
        articleList.add(mockArticle);
    }


    /*
     * Return a paged list of articles
     **/
    @Test
    public void articleListTest() throws Exception {

        int pageNumber = 1;

        Page<Article> mockPage = new PageImpl<>(articleList);
        Mockito.when(mockArticleService.articleList(1)).thenReturn(mockPage);

        mockMvc.perform(get("/rest/articles/?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.content", isA(Collection.class)))
                .andExpect(jsonPath("$.content", hasSize(articleList.size())))
                ;

        Mockito.verify(mockArticleService, Mockito.atLeastOnce()).articleList(pageNumber);
    }

//    /*
//     * Return a given article's details
//     **/
//    @Test
//    public void articleDetailTest() throws Exception {
//
//        Mockito.when(mockRepository.findOne(mockArticle.getId())).thenReturn(mockArticle);
//
//        mockMvc.perform(get("/rest/article/" + mockArticle.getId()))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$", hasSize(1)));
//
//        Mockito.verify(mockRepository, Mockito.atLeastOnce()).findAll();
//    }
}
