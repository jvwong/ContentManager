package com.example.cm.cm_web.web;

import com.example.cm.cm_jcrrepository.service.ArticleService;
import com.example.cm.cm_model.domain.Article;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

public class ArticleControllerTest {
    private static final Logger logger = LogManager.getLogger();

    private MockMvc mockMvc;
    private Principal mockPrincipal;
    private ArticleService mockArticleService;
    private List<Article> articleList;
    Article mockArticle;

    @Before
    public void setUp() {
        mockPrincipal = Mockito.mock(Principal.class);
        mockArticleService = Mockito.mock(ArticleService.class);
        ArticleController controller = new ArticleController(mockArticleService);
        mockMvc = standaloneSetup(controller).build();
        mockArticle = Mockito.mock(Article.class);

        Article mockArticle0 = new Article("0", "title0", "descritpion0", "keywords0");
        Article mockArticle1 = new Article("1", "title1", "descritpion1", "keywords1");
        Article mockArticle2 = new Article("2", "title2", "descritpion2", "keywords2");
        Article mockArticle3 = new Article("3", "title3", "descritpion3", "keywords3");
        Article mockArticle4 = new Article("4", "title4", "descritpion4", "keywords4");

        articleList = Arrays.asList(
                mockArticle0,
                mockArticle1,
                mockArticle2,
                mockArticle3,
                mockArticle4);
    }

    @Test
    public void testArticleList() throws Exception {

        Mockito.when(mockArticleService.getList()).thenReturn(articleList);
        mockMvc.perform(get("/articles/"))
                .andExpect(model().attributeExists("articleList"))
                .andExpect(model().attribute("articleList", instanceOf(Collection.class)))
                .andExpect(view().name("/articles/articleList"));
    }

//    @Test
//    public void testCreateArticleForm() throws Exception {
//        ArticleRepository mockRepository = Mockito.mock(ArticleRepository.class);
//        ArticleController controller = new ArticleController(mockRepository);
//        MockMvc mockMvc = standaloneSetup(controller).build();
//        mockMvc.perform(get("/articles/create"))
//                .andExpect(view().name("/articles/articleForm"));
//    }

    /*
     * Note that the .equals should be updated in target class
     * */
    @Test
    public void testCreateArticle() throws Exception {

        String uuid = UUID.randomUUID().toString();
        Mockito.doNothing()
                .when(mockArticleService)
                .save(Matchers.any(Article.class));

        Mockito.when(mockArticleService.exists(Matchers.any(String.class)))
                .thenReturn(true);

        Mockito.when(mockArticleService.findOne(Matchers.any(String.class)))
                .thenReturn(mockArticle);

        Mockito.when(mockArticle.getId())
                .thenReturn(uuid);

        mockMvc.perform(post("/articles/create")
                .principal(mockPrincipal)
                .param("title", "title1")
                .param("description", "description1")
                .param("keywords", "keywords1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/" + uuid))
        ;
    }

}
