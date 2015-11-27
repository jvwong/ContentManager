package com.example.cm.cm_model.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.URI;

/**
 * A Json Patch object
 */
public class JsonPatch {

    private String op;
    private URI path;
    private Object value;

    public JsonPatch(){
        this(null, null, null);
    }

    public JsonPatch(String op, URI path, Object value){
        this.op = op;
        this.path = path;
        this.value = value;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public URI getPath() {
        return path;
    }

    public void setPath(URI path) {
        this.path = path;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that, "op", "path", "value");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "op", "path", "value");
    }

    @Override
    public String toString() {
        return "[patch: op=" + this.getOp()
                + ", path=" + this.getPath().toString()
                + ", value=" + this.getValue().toString()
                + "]";
    }
}
