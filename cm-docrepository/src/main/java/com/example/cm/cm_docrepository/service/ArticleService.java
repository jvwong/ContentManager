package com.example.cm.cm_docrepository.service;

import com.example.cm.cm_model.domain.Article;
import org.springframework.data.domain.Page;
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
    Page<Article> getPagedList(Integer pageNumber, Integer pageSize);
    Page<Article> getPagedListByAuthor(Integer pageNumber, Integer pageSize, String author);

    Article findOne(String id);
    boolean exists(String id);

    @Transactional(readOnly = false)
    Article save(Article article);
}



