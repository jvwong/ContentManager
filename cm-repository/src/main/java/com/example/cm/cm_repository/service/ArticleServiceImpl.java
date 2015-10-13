package com.example.cm.cm_repository.service;

import com.example.cm.cm_model.domain.Article;
import com.example.cm.cm_repository.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jvwong
 */

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Page<Article> articleList(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest =
                new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "createdDate");
        return articleRepository.findAll(pageRequest);
    }

    public Article article(Long id){
        return articleRepository.findOne(id);
    }
}