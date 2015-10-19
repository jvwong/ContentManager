//package com.example.cm.cm_web.rest;
//
//import com.example.cm.cm_model.domain.Article;
//import com.example.cm.cm_repository.service.ArticleService;
//import com.example.cm.cm_web.config.annotation.RestEndpoint;
//import com.example.cm.cm_web.exceptions.ResourceConflictException;
//import com.example.cm.cm_web.exceptions.ResourceNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.net.URI;
//
///**
// * @author jvwong
// */
//@RestEndpoint
//@RequestMapping(value="/rest/articles")
//public class ArticleRestEndpoint {
//
//    private ArticleService articleService;
//
//    @Autowired
//    public ArticleRestEndpoint(ArticleService articleService){
//        this.articleService = articleService;
//    }
//
//    /**
//     * Retrieve the list of
//     * @return a page of articles wrapped in {@link Page} object
//     */
//    @RequestMapping(value="/", method= RequestMethod.GET)
//    public Page<Article> articleList(
//            @RequestParam(value="page", defaultValue="1") Integer pageNumber,
//            @RequestParam(value="size", defaultValue="10") Integer pageSize
//    ){
//        return articleService.articleList(pageNumber, pageSize);
//    }
//
//    /**
//     * Retrieve a particular article instance
//     * @return an {@link Article}
//     */
//    @RequestMapping(
//            value="/{id}/",
//            method=RequestMethod.GET
//    )
//    public Article articleDetail(
//            @PathVariable("id") Long id){
//        Article found = articleService.article(id);
//        if(found == null){
//            throw new ResourceNotFoundException(Article.class.getName());
//        }
//        return found;
//    }
//
//    /**
//     * Save an {@link Article} instance
//     * @return the saved {@link Article}  wrapped in a {@link ResponseEntity}
//     */
//    @RequestMapping(value="/", method= RequestMethod.POST)
//    public ResponseEntity<Article> saveArticle(
//            @RequestBody Article article,
//            UriComponentsBuilder ucb
//    ){
//        try {
//            HttpHeaders headers = new HttpHeaders();
//
//            // DataIntegrityViolationException
//            Article saved = articleService.save(article);
//            URI locationUri =
//                    ucb.path("/services/rest/articles/")
//                            .path(String.valueOf(saved.getId()))
//                            .build()
//                            .toUri();
//            headers.setLocation(locationUri);
//            return  new ResponseEntity<>(saved, headers, HttpStatus.CREATED);
//
//
//        } catch (DataIntegrityViolationException dee) {
//            throw new ResourceConflictException(Article.class.toString());
//        }
//
//    }
//
//
//}
