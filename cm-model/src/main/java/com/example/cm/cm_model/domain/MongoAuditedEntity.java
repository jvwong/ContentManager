package com.example.cm.cm_model.domain;

import com.example.cm.cm_model.converters.InstantConverter;
import com.example.cm.cm_model.serializers.DateInstantDeserializer;
import com.example.cm.cm_model.serializers.DateInstantSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

@EntityListeners(AuditingEntityListener.class)
public class MongoAuditedEntity {

    private String id;

    @Version
    private Long version;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @CreatedBy
    private String author;

    @LastModifiedBy
    private String updater;


    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that, "id");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "id");
    }


    public Long getVersion(){
        return this.version;
    }

    void setVersion(Long version){
        this.version = version;
    }

    @Convert(converter = InstantConverter.class)
    @JsonSerialize(using = DateInstantSerializer.class)
    @JsonDeserialize(using = DateInstantDeserializer.class)
    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Convert(converter = InstantConverter.class)
    @JsonSerialize(using = DateInstantSerializer.class)
    @JsonDeserialize(using = DateInstantDeserializer.class)
    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

}
