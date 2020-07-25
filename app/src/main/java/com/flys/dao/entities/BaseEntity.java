package com.flys.dao.entities;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class BaseEntity implements Serializable {
    @DatabaseField(generatedId = true)
    private Long id;

    public BaseEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
