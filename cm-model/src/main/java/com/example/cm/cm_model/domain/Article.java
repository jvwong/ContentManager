package com.example.cm.cm_model.domain;


import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author jvwong
 */


@NodeEntity
public class Article extends DateByAuditedEntity{

    private String title;
    private String description;
    private String keywords;

    @RelatedTo(type="HAS_PAGE")
    private Collection<Page> pages = new LinkedHashSet<>();

    public Article(){
        this(null, null, null, null);
    }

    public Article(
            String title,
            String description,
            String keywords,
            Collection<Page> pages){
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.pages = pages;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Collection<Page> getPages() {
        return this.pages;
    }

    public void setPages(Collection<Page> pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "[Article: id=" + this.getId()
                + ", createdBy=" + this.getCreatedBy()
                + ", createdDate=" + this.getCreatedDate()
                + ", title=" + this.title
                + ", description=" + this.description
                + ", keywords=" + this.keywords
                + ", numPages=" + (pages == null ? 0 : pages.size())
                + "]";
    }


}

