package com.example.cm.cm_web.rest;

import com.example.cm.cm_model.domain.Article;
import com.example.cm.cm_repository.service.ArticleService;
import com.example.cm.cm_web.config.annotation.RestEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author jvwong
 */
@RestEndpoint
@RequestMapping(value="/rest/articles")
public class ArticleRestEndpoint {

    private ArticleService articleService;

    @Autowired
    public ArticleRestEndpoint(ArticleService articleService){
        this.articleService = articleService;
    }

    /**
     * Retrieve the list of
     * @return a page of articles wrapped in {@link Page} object
     */
    @RequestMapping(value="", method= RequestMethod.GET)
    public Page<Article> articleList(
            @RequestParam(value="page", defaultValue="1") Integer pageNumber,
            @RequestParam(value="size", defaultValue="10") Integer pageSize
    ){
        return articleService.articleList(pageNumber, pageSize);
    }
}
