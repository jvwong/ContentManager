package com.example.cm.cm_docrepository.service;

import com.example.cm.cm_docrepository.repository.ArticleRepository;
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
        return (List<Article>) articleRepository.findAll();
    }

    public Article findOne(Long id){
        return articleRepository.findOne(id);
    }

    /**
     * Should do a bunch of checking to see which method to call.
     * @param article The article to save
     */
    public Article save(Article article){
        return articleRepository.save(article);
    }

    public boolean exists(Long id){
        return articleRepository.exists(id);
    }
}