package com.example.cm.cm_model.domain;

public abstract class Updatable extends DateByAuditedEntity {
    public enum STATUS {
        PENDING,
        LIVE
    }
    private STATUS status;

    public STATUS getStatus() {
        return this.status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
}
