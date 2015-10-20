package com.example.cm.cm_jcrrepository.jcr;


import com.example.cm.cm_model.domain.Article;
import com.example.cm.cm_model.domain.Page;
import org.springframework.stereotype.Component;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.time.Instant;
import java.util.Calendar;


/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Component
public class ArticleMapper {

    /**
     * Maps the node to an article. Does not copy the pages.
     *
     * @param node article node
     * @return article
     * @throws RepositoryException if there's a repository exception
     */
    public Article toArticle(Node node) throws RepositoryException {
        Article article = new Article();
        article.setId(node.getName());
        article.setTitle(node.getProperty("title").getString());
        article.setCreatedBy(node.getProperty("createdBy").getString());
        article.setCreatedDate(node.getProperty("createdDate").getDate().toInstant());

        if (node.hasProperty("description")) {
            article.setDescription(node.getProperty("description").getString());
        }

        if (node.hasProperty("keywords")) {
            article.setKeywords(node.getProperty("keywords").getString());
        }

        return article;
    }

    /**
     * Creates an article node for the given article and attaches it to the given parent node.
     *
     * @param article article
     * @param parent parent node
     * @return article node
     * @throws RepositoryException if there's a repository exception
     */
    public Node addArticleNode(Article article, Node parent) throws RepositoryException {
        Node node = parent.addNode(article.getId());
        node.setProperty("title", article.getTitle());
        node.setProperty("createdBy", article.getCreatedBy());

        // JCR requires a Calendar object here for some reason.
        Instant publishDate = article.getCreatedDate();
        if (publishDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(publishDate.toEpochMilli());
            node.setProperty("createdDate", cal);
        }

        String description = article.getDescription();
        if (description != null) {
            node.setProperty("description", description);
        }

        String keywords = article.getKeywords();
        if (keywords != null) {
            node.setProperty("keywords", keywords);
        }

        Node pagesNode = node.addNode("pages", "nt:folder");
//        int numPages = article.getPages().size();
//        for (int i = 0; i < numPages; i++) {
//            Page page = article.getPages().get(i);
//            addPageNode(pagesNode, page, i + 1);
//        }

        return node;
    }

    /**
     * @param pagesNode pages node
     * @param page page
     * @param pageNumber page number
     * @throws RepositoryException if there's a repository exception
     */
    private void addPageNode(Node pagesNode, Page page, int pageNumber) throws RepositoryException {
        Node pageNode = pagesNode.addNode(String.valueOf(pageNumber), "nt:file");
        Node contentNode = pageNode.addNode(Node.JCR_CONTENT, "nt:resource");
        contentNode.setProperty("jcr:data", page.getContent());
    }
}