package com.example.cm.cm_web.rest;

import com.example.cm.cm_docrepository.service.ArticleService;
import com.example.cm.cm_model.domain.Article;
import com.example.cm.cm_model.domain.JsonPatch;
import com.example.cm.cm_web.config.annotation.RestEndpoint;
import com.example.cm.cm_web.exceptions.ResourceConflictException;
import com.example.cm.cm_web.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.Marshaller;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.security.Principal;
import java.util.List;

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

    private final String MIME_JSON_PATCH = "application/json-patch";
    private final String MIME_JSON = "application/json;charset=UTF-8";


    @Inject
    private Marshaller marshaller;
    private ArticleService articleService;

    @Autowired
    public ArticleRestEndpoint(ArticleService articleService)
    {
        this.articleService = articleService;
    }

    @RequestMapping(
            value="/",
            method=RequestMethod.OPTIONS)
    public ResponseEntity<String> getOptions(HttpServletResponse httpResponse)
    {
        httpResponse.setHeader("Allow", "GET, HEAD, POST, PUT, DELETE, OPTIONS, PATCH");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieve the paged list of {@link Article} objects that
     * who's author is the current Principal
     * @return {@link Page} of articles
     */
    @RequestMapping(value="/",
            method=RequestMethod.GET)
    public Page<Article> articleList(
            Principal principal,
            @RequestParam(value="page", defaultValue="1") Integer pageNumber,
            @RequestParam(value="size", defaultValue="10") Integer pageSize)
    {
        return articleService.getPagedListByAuthor(pageNumber, pageSize, principal.getName());
    }

    /**
     * Retrieve a particular article instance
     * @return an {@link Article}
     */
    @RequestMapping(
            value="/{id}/",
            method=RequestMethod.GET
    )
    public Article articleDetail(
            @PathVariable("id") String id,
            Principal principal)
    {
        Article found = articleService.findOne(id);
        if(found == null){
            throw new ResourceNotFoundException(Article.class.getName());
        }

        String author = found.getAuthor();

        if(author != null && !author.equals(principal.getName()))
        {
            throw new AccessDeniedException(Article.class.getName());
        }
        return found;
    }

    /**
     * Save an {@link Article} instance
     * @return the saved {@link Article}  wrapped in a {@link ResponseEntity}
     */
    @RequestMapping(
            value="/",
            method=RequestMethod.POST)
    public ResponseEntity<Article> saveArticle(
            @RequestBody Article article,
            UriComponentsBuilder ucb)
    {
        try
        {
            HttpHeaders headers = new HttpHeaders();

            // DataIntegrityViolationException
            Article saved = articleService.save(article);

            URI locationUri =
                    ucb.path("/services/rest/articles/")
                            .path(String.valueOf(saved.getId()))
                            .build()
                            .toUri()
                    ;
            headers.setLocation(locationUri);
            return  new ResponseEntity<>(saved, headers, HttpStatus.CREATED);


        }
        catch (DataIntegrityViolationException dee)
        {
            throw new ResourceConflictException(Article.class.toString());
        }

    }

    /**
     * Delete an existing {@link Article} instance
     */
    @RequestMapping(
            value="/{id}/",
            method=RequestMethod.DELETE)
    public ResponseEntity<String> deleteArticle(
            @PathVariable("id") String id)
    {
        Boolean exists = articleService.exists(id);

        if(!exists) {
            //404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // delete
        articleService.delete(id);

        //204 No Content
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    /**
     * Update an existing {@link Article} instance using the
     * protocol laid out in (http://jsonpatch.com/)
     */
    @RequestMapping(
            value="/{id}/",
            method=RequestMethod.PATCH)
    public ResponseEntity<Article> updateArticle(
            @PathVariable("id") String id,
            @RequestBody List<JsonPatch> patches)
    {

        Boolean exists = articleService.exists(id);

        if(!exists) {
            //404 Not Found
            throw new ResourceNotFoundException(Article.class.getName());
        }

        // partial update
        Article updated = articleService.update(id, patches);

        //204 No Content
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

}
