package com.example.cm.cm_jcrrepository.service;

import com.example.cm.cm_jcrrepository.repository.ArticleRepository;
import com.example.cm.cm_model.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @author jvwong
 */

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getList() {
        return articleRepository.findAll();
    }

    public Article findOne(String id){
        return articleRepository.get(id);
    }

    /**
     * Should do a bunch of checking to see which method to call.
     * @param article The article to save
     */
    public void save(Article article){
        article.setId(UUID.randomUUID().toString());
        article.setCreatedDate(Instant.now());

        articleRepository.create(article);
    }

    public boolean exists(String id){
        return articleRepository.exists(id);
    }
}