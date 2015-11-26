package com.example.cm.cm_web.rest;

import com.example.cm.cm_docrepository.service.ArticleService;
import com.example.cm.cm_model.domain.Article;
import com.example.cm.cm_web.config.annotation.RestEndpoint;
import com.example.cm.cm_web.exceptions.ResourceConflictException;
import com.example.cm.cm_web.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.Marshaller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.security.Principal;


/**
 * @author jvwong
 */
@RestEndpoint
@RequestMapping(value="/rest/articles")
public class ArticleRestEndpoint {
    private static final Logger logger
            = LoggerFactory.getLogger(ArticleRestEndpoint.class);

    private final String DEFAULT_PAGE_AS_STRING = "0";
    private final String DEFAULT_SIZE_AS_STRING = "10";


    @Inject
    private Marshaller marshaller;
    private ArticleService articleService;

    @Autowired
    public ArticleRestEndpoint(ArticleService articleService){
        this.articleService = articleService;
    }

    @RequestMapping(
            value="/",
            method=RequestMethod.OPTIONS
    )
    public ResponseEntity<String> getOptions(HttpServletResponse httpResponse){
        httpResponse.setHeader("Allow", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /**
     * Retrieve the paged list of
     * @return a page of articles wrapped in {@link Page} object
     */
    @RequestMapping(value="/", method=RequestMethod.GET)
    public Page<Article> articleList(
            @RequestParam(value="page", defaultValue="1") Integer pageNumber,
            @RequestParam(value="size", defaultValue="10") Integer pageSize
    ){
        return articleService.getPagedList(pageNumber, pageSize);
    }

    /**
     * Retrieve a particular article instance
     * @return an {@link Article}
     */
    @RequestMapping(
            value="/{id}/",
            method=RequestMethod.GET
    )
    public Article articleDetail(@PathVariable("id") String id){
        Article found = articleService.findOne(id);
        if(found == null){
            throw new ResourceNotFoundException(Article.class.getName());
        }
        return found;
    }

    /**
     * Save an {@link Article} instance
     * @return the saved {@link Article}  wrapped in a {@link ResponseEntity}
     */
    @RequestMapping(
            value="/",
            method=RequestMethod.POST
    )
    public ResponseEntity<Article> saveArticle(
            Principal principal,
            @RequestBody Article article,
            UriComponentsBuilder ucb
    ){
        try {
            HttpHeaders headers = new HttpHeaders();

            // DataIntegrityViolationException
            article.setCreatedBy(principal.getName());
            Article saved = articleService.save(article);

            URI locationUri =
                    ucb.path("/services/rest/articles/")
                            .path(String.valueOf(saved.getId()))
                            .build()
                            .toUri()
                    ;
            headers.setLocation(locationUri);
            return  new ResponseEntity<>(saved, headers, HttpStatus.CREATED);


        } catch (DataIntegrityViolationException dee) {
            throw new ResourceConflictException(Article.class.toString());
        }

    }
}
