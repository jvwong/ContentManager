package com.example.cm.cm_web.web;

import com.example.cm.cm_docrepository.service.ArticleService;
import com.example.cm.cm_model.domain.Article;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ArticleControllerTest {

    private MockMvc mockMvc;
    private Principal mockPrincipal;
    private ArticleService mockArticleService;
    private List<Article> articleList;
    Article mockArticle;
    Long id;

    @Before
    public void setUp() {
        mockPrincipal = Mockito.mock(Principal.class);
        mockArticleService = Mockito.mock(ArticleService.class);
        ArticleController controller = new ArticleController(mockArticleService);
        mockMvc = standaloneSetup(controller).build();
        mockArticle = Mockito.mock(Article.class);
        id = 24L;

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

    @Test
    public void testGetList() throws Exception {

        Mockito.when(mockArticleService.getList()).thenReturn(articleList);
        mockMvc.perform(get("/articles/"))
                .andExpect(model().attributeExists("articleList"))
                .andExpect(model().attribute("articleList", instanceOf(Collection.class)))
                .andExpect(view().name("/articles/articleList"));
    }

    @Test
    public void testCreateArticleForm() throws Exception {
        mockMvc.perform(get("/articles/create"))
                .andExpect(view().name("/articles/articleForm"));
    }

    @Test
    public void testGetArticle() throws Exception {

        Mockito.when(mockArticleService.findOne(id)).thenReturn(mockArticle);
        mockMvc.perform(get("/articles/" + id))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attribute("article", instanceOf(Article.class)))
                .andExpect(view().name("/articles/articlePage"));
    }


    /*
     * Note that the .equals should be updated in target class
     * */
    @Test
    public void testCreateArticle() throws Exception {

        Mockito.when(mockArticleService.save(Matchers.any(Article.class)))
                .thenReturn(mockArticle);

        Mockito.when(mockArticleService.exists(Matchers.any(Long.class)))
                .thenReturn(true);

        Mockito.when(mockArticleService.findOne(Matchers.any(Long.class)))
                .thenReturn(mockArticle);

        Mockito.when(mockArticle.getId())
                .thenReturn(id);

        mockMvc.perform(post("/articles/create")
                .principal(mockPrincipal)
                .param("title", "title1")
                .param("description", "description1")
                .param("keywords", "keywords1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/" + id))
        ;
    }

}
