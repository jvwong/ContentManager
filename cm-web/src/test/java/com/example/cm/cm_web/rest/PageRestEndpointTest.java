package com.example.cm.cm_web.rest;

import com.example.cm.cm_model.domain.Article;
import com.example.cm.cm_model.domain.Page;
import com.example.cm.cm_repository.service.PageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

/**
 * @author jvwong
 */
public class PageRestEndpointTest {
    private static final Logger logger = LogManager.getLogger();

    private MockMvc mockMvc;
    private PageService mockPageService;
    private Article mockArticle;
    private List<Page> pageList;

    @Before
    public void setUp() {
        mockPageService = Mockito.mock(PageService.class);
        PageRestEndpoint endpoint = new PageRestEndpoint(mockPageService);
        mockMvc = standaloneSetup(endpoint).build();

        mockArticle = new Article(0L, "title0", "descritpion0", "keywords0");
        Page mockPage0 = new Page(0L, 0L, "content for page 0");
        Page mockPage1 = new Page(1L, 0L, "content for page 1");
        Page mockPage2 = new Page(2L, 0L, "content for page 2");
        Page mockPage3 = new Page(3L, 0L, "content for page 3");

        pageList = Arrays.asList(mockPage0, mockPage1, mockPage2, mockPage3);
    }


        /*
         * Return pages for a given article
         **/
        @Test
        public void pageListTest() throws Exception {

        int pageNumber = 1;
        int pageSize = 3;
        Long articleId = 0L;


        org.springframework.data.domain.Page<Page> mockPage = new PageImpl<>(pageList);
        Mockito.when(mockPageService.pageList(articleId, pageNumber, pageSize))
                .thenReturn(mockPage);

//        mockMvc.perform(get("/rest/pages/?page=" + pageNumber + "&size=" + pageSize))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.content", isA(Collection.class)))
//                .andExpect(jsonPath("$.content", hasSize(articleList.size())))
//        ;
//
//        Mockito.verify(mockArticleService, Mockito.atLeastOnce())
//                .articleList(pageNumber, pageSize);
        }
}
