package com.example.cm.cm_repository.service;

import com.example.cm.cm_model.domain.Article;
import org.springframework.data.domain.Page;

/**
 * @author jvwong
 */
public interface ArticleService {
    Page<Article> articleList(Integer pageNumber);
}


