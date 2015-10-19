package com.example.cm.cm_web.web;

import com.example.cm.cm_jcrrepository.repository.ArticleRepository;
import com.example.cm.cm_model.domain.Article;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.hamcrest.Matchers.*;

public class ArticleControllerTest {
    private static final Logger logger = LogManager.getLogger();

    @Test
    public void testArticleList() throws Exception {
        ArticleRepository mockRepository = Mockito.mock(ArticleRepository.class);
        ArticleController controller = new ArticleController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();
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
//
//    /*
//     * Note that the .equals should be updated in target class
//     * */
//    @Test
//    public void testCreateArticle() throws Exception {
//        ArticleRepository mockRepository = Mockito.mock(ArticleRepository.class);
//        Article unsaved = new Article("title1", "description1", "keywords1");
//        Article saved = new Article(24L, "title1", "description1", "keywords1");
//        Mockito.when(mockRepository.save(unsaved)).thenReturn(saved);
//
//        ArticleController controller = new ArticleController(mockRepository);
//        MockMvc mockMvc = standaloneSetup(controller).build();
//
//        mockMvc.perform(post("/articles/create")
//                .param("title", "title1")
//                .param("description", "description1")
//                .param("keywords", "keywords1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/articles/" + saved.getId()));
//
//        Mockito.verify(mockRepository, Mockito.atLeastOnce()).save(unsaved);
//    }

}
