package com.example.cm.cm_docrepository.service;

import com.example.cm.cm_docrepository.repository.ArticleRepository;
import com.example.cm.cm_model.domain.Article;
import com.example.cm.cm_model.domain.JsonPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jvwong
 */

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger
            = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getList() {
        return articleRepository.findAll();
    }

    public Page<Article> getPagedList(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest =
                new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "createdDate");
        return articleRepository.findAll(pageRequest);
    }

    public Page<Article> getPagedListByAuthor(
            Integer pageNumber,
            Integer pageSize,
            String author) {
        PageRequest pageRequest =
                new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "createdDate");
        return articleRepository.findByAuthor(author, pageRequest);
    }



    public Article findOne(String id){
        return articleRepository.findOne(id);
    }

    /**
     * Should do a bunch of checking to see which method to call.
     * @param article The article to save
     */
    public Article save(Article article){
        return articleRepository.save(article);
    }

    public boolean exists(String id){
        return articleRepository.exists(id);
    }

    /**
     * Delete the record with the given id if it exists
     * @param id the id for the article
     */
    public void delete(String id){
        articleRepository.delete(id);
    }

    /**
     * Update the record with the given id if it exists
     * @param id the id for the article
     * @return Article representation of updated {@link Article}
     */
    public Article update(String id, JsonPatch patch){
        Article article = articleRepository.findOne(id);

        String operation = patch.getOp();
        switch (operation)
        {
            case "update":
                logger.debug("update ", patch.toString());
                break;
        }

        articleRepository.save(article);
        return article;
    }
}