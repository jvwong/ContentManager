package com.example.cm.cm_jcrrepository.repository;

import com.example.cm.cm_model.domain.Article;

/**
 * Article data access object.
 * @author jvwong
 */
public interface ArticleRepository extends Dao<Article> {

	/**
	 * @param article article to create or update
	 */
	Article createOrUpdate(Article article);

	/**
	 * Returns the article with the given page loaded.
	 *
	 * @param articleId article ID
	 * @param pageNumber page number
	 * @return article with the given page loaded
	 */
	Article getPage(String articleId, int pageNumber);
}
