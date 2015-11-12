package com.example.cm.cm_docrepository.repository;

import com.example.cm.cm_model.domain.Article;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * Article data access object.
 * @author jvwong
 */

public interface ArticleRepository  extends GraphRepository<Article> { }
