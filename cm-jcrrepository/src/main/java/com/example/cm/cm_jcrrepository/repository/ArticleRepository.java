package com.example.cm.cm_jcrrepository.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.cm.cm_model.domain.Article;

/**
 * Article data access object.
 * @author jvwong
 */

public interface ArticleRepository extends MongoRepository<Article, String> { }
