package com.example.cm.cm_jcrrepository.service;

import com.example.cm.cm_model.domain.Article;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jvwong
 */
@Service
@Transactional(readOnly = true)
public interface ArticleService {

    List<Article> getList();
    Article findOne(String id);
    boolean exists(String id);

    @Transactional(readOnly = false)
    Article save(Article article);
}



