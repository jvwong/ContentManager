package com.example.cm.cm_docrepository.service;

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
    Article findOne(Long id);
    boolean exists(Long id);

    @Transactional(readOnly = false)
    Article save(Article article);
}



