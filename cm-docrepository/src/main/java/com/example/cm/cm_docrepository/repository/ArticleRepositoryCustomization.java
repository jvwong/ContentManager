package com.example.cm.cm_docrepository.repository;

import com.example.cm.cm_model.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Custom methods added to jpa
 */
public interface ArticleRepositoryCustomization {
    Page<Article> findByAuthor(String author, Pageable pageRequest);
}
