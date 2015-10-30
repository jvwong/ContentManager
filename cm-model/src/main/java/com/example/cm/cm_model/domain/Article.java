package com.example.cm.cm_model.domain;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author jvwong
 */
public class Article extends DateByAuditedEntity{

    private String title;
    private String description;
    private String keywords;
    private List<Page> pages;

    public Article(){
        this(null, null, null);
    }

    public Article(
            String title,
            String description,
            String keywords){
        this.title = title;
        this.description = description;
        this.keywords = keywords;
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

    @XmlElement
    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
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

