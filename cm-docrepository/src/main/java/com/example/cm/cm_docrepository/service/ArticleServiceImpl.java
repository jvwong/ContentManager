package com.example.cm.cm_docrepository.service;

import com.example.cm.cm_docrepository.repository.ArticleRepository;
import com.example.cm.cm_model.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        article.setId(UUID.randomUUID().toString());
        article.setCreatedDate(Instant.now());

        return articleRepository.save(article);
    }

    public boolean exists(String id){
        return articleRepository.exists(id);
    }
}